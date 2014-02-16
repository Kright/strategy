package game.main.GUI;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import game.main.gamelogic.GameSession;
import game.main.input.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgor on 17.01.14.
 */
public class SessionThread extends Thread implements View.OnTouchListener {

    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private final GameSession session;

    public SessionThread(SurfaceHolder surfaceHolder, Resources resources) {
        this.surfaceHolder = surfaceHolder;
        session = GameSession.initGameSession(resources);
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    private boolean first=true;

    @Override
    public final void run() {
        Canvas canvas;
        while (runFlag && session.notFinished) {
            Touch[] t = getTouches();
            session.doLogic(t);
            if (session.maySkipRender() && !first){
                delay(20);
                continue;
            }
            first = false;
            canvas = null;
            try {                       // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas != null)
                    synchronized (surfaceHolder) {
                        session.render(canvas);
                    }
            } finally {
                if (canvas != null) {   // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        //session.save();
        surfaceHolder = null;
    }

    private final List<Touch> touches = new ArrayList<Touch>();

    Touch prev;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        prev = Touch.getTouches(event, prev);
        synchronized (touches) {
            touches.add(prev);
        }
        return true;
    }

    private Touch[] getTouches() {
        synchronized (touches) {
            int size = touches.size();
            if (size == 0)
                return null;
            Touch[] result = new Touch[size];
            for (int i = 0; i < size; i++) {
                result[i] = touches.get(i);
            }
            touches.clear();
            return result;
        }
    }

    protected final void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
