package game.main.openGL;

import android.opengl.GLSurfaceView;
import game.main.gamelogic.world.World;
import game.main.utils.Touch;
import game.main.utils.TouchBuffer;

import java.util.List;

/**
 * Здесь весь игровой контекст, а заодно есть реализация Runnable
 * Created by lgor on 29.04.14.
 */
public class GLGameSession implements Runnable {

    /**
     * координаты нажатия - в пикселях
     */
    public final TouchBuffer touches = new TouchBuffer();

    protected volatile boolean running = false;
    protected volatile boolean finished = false;

    protected volatile World world;

    protected final GLSurfaceView view;
    protected final DrawingContext drawingContext;


    public GLGameSession(GLSurfaceView view, DrawingContext drawingContext) {
        this.view = view;
        this.drawingContext = drawingContext;
        view.setOnTouchListener(touches);
    }

    @Override
    public void run() {
        int i = 0;
        while (!finished) {
            while (running) {
                repaint();
                List<Touch> tt = touches.getTouches();
                while (!tt.isEmpty()) {
                    GLActivity.myLog(tt.remove(0));
                }
                if (i++ % 100 == 0) {
                    GLActivity.myLog("I'm working : " + i / 100);
                }
                sleep(10);
            }
            sleep(50);
        }
        GLActivity.myLog("I'm finished");
    }

    public void setRunning(boolean running) {
        this.running = running;
        GLActivity.myLog("set Running");
    }

    public void finish() {
        finished = true;
        running = false;
    }

    /**
     * фактически, блокирующий вызов
     */
    public void repaint() {
        view.queueEvent(new Runnable() {
            public void run() {
                drawingContext.repainted = false;
                view.requestRender();
            }
        });
        while (running && !drawingContext.repainted) {
            Thread.yield();
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            //nothing
        }
    }
}
