package game.main;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import game.main.gamelogic.GameSession;
import game.main.utils.Touch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * поток с игрой. Запускается один раз, если приложение сворачивают - приостанавливается.
 * Created by lgor on 02.04.14.
 */
public class GameThread extends Thread implements View.OnTouchListener{

    public volatile FileOutputStream fos;
    public volatile FileInputStream fis;

    private OtherInfo other;
    private final Object monitor;
    private volatile boolean reallyWait=false;
    private volatile boolean mustWait=true;
    private volatile SurfaceHolder holder;
    private  GameSession session;

    protected GameThread(Object monitor,Resources res){
        this.monitor = monitor;

        loadGame();
        session.initRes(res);

        session.setCallback(this);
        this.start();
        this.setWaiting();
    }

    protected GameThread(Object monitor, GameSession session){
        this.monitor = monitor;
        this.session = session;
        saveGame();

        session.setCallback(this);
        this.start();
        this.setWaiting();
    }

    @Override
    public final void run() {
        checkPause();
            saveGame();
        session.run();
    }

    private void saveGame(){
        ObjectOutputStream oos;

        try
        {

            File f = new File("session");
            if(f.exists()) f.delete();
            f.setWritable(true);
            FileOutputStream fos = new FileOutputStream(f);

            oos = new ObjectOutputStream(fos);
            oos.writeObject(other);
            other = new OtherInfo();
            oos.writeObject(other);
            oos.writeBytes("lalala");
            //oos.writeObject(session);
            oos.writeBytes("popopo");
            oos.flush();
            oos.close();
            fos.close();
        }
        catch (FileNotFoundException e) {}
        catch (IOException ex){}
        catch (NullPointerException nux) {}
    }

    private void loadGame(){
        ObjectInputStream ois;
        File f = new File("session");
        f.setReadable(true);

        try
        {
            FileInputStream fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);

            other = (OtherInfo)ois.readObject();
            OtherInfo o2 = (OtherInfo)ois.readObject();
            String s = ois.readUTF();
            String s1 = (String)ois.readObject();
            //session = (GameSession)ois.readObject();
            ois.close();
            fis.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            String s = ex.toString();
            System.out.print(s);
        }
        catch (NullPointerException nux) {}
        catch (ClassNotFoundException cex){}
    }

    public final boolean repaint(){
        boolean success=false;
        Canvas canvas=null;
        try {                       // получаем объект Canvas и выполняем отрисовку
            canvas = holder.lockCanvas(null);
            if (canvas != null)
                synchronized (holder) {
                    session.paint(canvas);
                    success = true;
                }
        } finally {
            if (canvas != null) {   // отрисовка выполнена. выводим результат на экран
                holder.unlockCanvasAndPost(canvas);
            }
        }
        return success;
    }

    /**
     * если извне потоку посоветовали остановиться, то он остановится, только когда мы изнутри вызовем эту функцию
     * @return true if thread was paused;
     */
    public final boolean checkPause(){
        Thread.yield();         //без этого приложение может нас радовать чёрным экраном довольно долгое время
                                //если в игровом цикле нет ничего, нагружающего телефон, а иначе работает и без yield
        boolean paused=false;
        synchronized (monitor){
            reallyWait = mustWait;
            if(mustWait) saveGame();
            while (mustWait){
                try {
                    paused = true;
                    monitor.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        if (paused){
            session.resume();
        }
        return paused;
    }

    public final void resume(SurfaceHolder holder, Resources res){
        loadGame();
        session.initRes(res);
        this.holder = holder;
        mustWait = false;
    }

    public final void setWaiting(){
        synchronized (monitor){
            mustWait = true;
        }
    }

    public final boolean isWaiting(){
        return reallyWait;
    }

    /*
     * обработка нажатий на экран
     */
    private final List<Touch> touches = new ArrayList<Touch>();
    private Touch prev;

    public final List<Touch> getTouches() {
        List<Touch> copy;
        synchronized (touches) {
            copy = new ArrayList<Touch>(touches);
            touches.clear();
        }
        return copy;
    }

    @Override
    public final boolean onTouch(View v, MotionEvent event) {
        prev = Touch.getTouches(event, prev);
        synchronized (touches) {
            touches.add(prev);
        }
        return true;
    }
}
