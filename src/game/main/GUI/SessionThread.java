package game.main.GUI;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import game.main.gamelogic.GameSession;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgor on 17.01.14.
 */
public class SessionThread extends Thread implements View.OnTouchListener {

    private boolean runFlag = false;
    private final SurfaceHolder surfaceHolder;
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
            session.doLogic(getTouches());
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
    }

    private final List<Touch> touches = new ArrayList<Touch>();
    private Touch prev;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        prev = Touch.getTouches(event, prev);
        synchronized (touches) {
            touches.add(prev);
        }
        return true;
    }

    private List<Touch> getTouches() {
        List<Touch> copy;
        synchronized (touches) {
            copy = new ArrayList<Touch>(touches);
            touches.clear();
        }
        return copy;
    }

    protected final void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
