package game.main;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import game.main.GUI.SessionThread;

/**
 * Created by lgor on 13.01.14.
 */
public class MapView extends SurfaceView implements SurfaceHolder.Callback {

    private SessionThread thread;

    public MapView(Context context) {
        super(context);
        getHolder().addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new SessionThread(getHolder(), getResources());
        thread.setRunning(true);
        thread.start();
        setOnTouchListener(thread);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        thread = null;
    }
}
