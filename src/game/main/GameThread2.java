package game.main;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * поток с игрой. Запускается один раз, если приложение сворачивают - приостанавливается.
 * Created by lgor on 02.04.14.
 */
public abstract class GameThread2 extends Thread {

    private volatile Object monitor;
    private volatile boolean reallyWait=false;
    private volatile boolean mustWait=true;
    private volatile SurfaceHolder holder;

    protected GameThread2(Object monitor){
        this.monitor = monitor;
        this.start();
        this.setWaiting();
    }

    /**
     * main loop there
     */
    public abstract void main();

    /**
     * @param canvas, на котором мы рисуем. Этот метод вызывается, если вызвать repaint()
     */
    public abstract void paint(Canvas canvas);

    @Override
    public void run() {
        Log.d("mylog", "run started");
        checkPause();
        Log.d("mylog", "after checkPause");
        main();
    }

    protected final void repaint(){
        Canvas canvas=null;
        try {                       // получаем объект Canvas и выполняем отрисовку
            canvas = holder.lockCanvas(null);
            if (canvas != null)
                synchronized (holder) {
                    paint(canvas);
                }
        } finally {
            if (canvas != null) {   // отрисовка выполнена. выводим результат на экран
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * если извне потоку посоветовали остановиться, то он остановится, только когда мы изнутри вызовем эту функцию
     * @return true if thread was paused;
     */
    protected final boolean checkPause(){
        Thread.yield();         //без этого приложение может нас радовать чёрным экраном довольно долгое время
                                //если в игровом цикле нет ничего, нагружающего телефон, а иначе работает и без yield
        boolean paused=false;
        synchronized (monitor){
            reallyWait = mustWait;
            while (mustWait){
                try {
                    paused = true;
                    monitor.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return paused;
    }

    public final void resume(SurfaceHolder holder){
        this.holder = holder;
        mustWait = false;
    }

    public final void setWaiting(){
        synchronized (monitor){
            mustWait = true;
        }
    }

    public boolean isWaiting(){
        return reallyWait;
    }
}
