package game.main.openGL;

import android.opengl.GLSurfaceView;
import android.util.Log;
import game.main.gamelogic.world.World;
import game.main.utils.TouchBuffer;

/**
 * Created by lgor on 29.04.14.
 */
public class GLGameSession implements Runnable {

    public final TouchBuffer touches = new TouchBuffer();

    protected volatile boolean running = true;

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
        int i=0;
        while (running) {
            repaint();
            if (i++ % 100 == 0){
                Log.e("", "I'm working daemon! :"+ i/100);
                }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
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
}
