package game.main;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import game.main.gamelogic.GameSession;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * поток с игрой. Запускается один раз, если приложение сворачивают - приостанавливается.
 * Created by lgor on 02.04.14.
 */
public class GameThread extends Thread implements View.OnTouchListener{

    protected final GameSession session;
    protected volatile boolean reallyWait=false;
    protected volatile boolean mustWait=true;

    private final Object monitor;
    private volatile SurfaceHolder holder;

    protected GameThread(Object monitor, GameSession session){
        this.monitor = monitor;
        this.session = session;
        session.setCallback(this);
        this.start();
        this.setWaiting();
    }

    @Override
    public final void run() {
        checkPause();
        session.run();
    }

    public boolean repaint(){
        boolean success=false;
        Canvas canvas=null;
        if (mustWait){      // если потоку советуют остановиться, рисование будет пропущено
            return false;
        }
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
            if (mustWait){
                session.pause();
                while (mustWait){
                    try {
                        paused = true;
                        monitor.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        if (paused){
            session.resume();
        }
        return paused;
    }

    public void resume(SurfaceHolder holder){
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
