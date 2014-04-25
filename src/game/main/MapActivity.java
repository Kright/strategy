package game.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceView;
import game.main.gamelogic.GameSession;

/**
 * активити самой игры
 * Created by lgor on 13.01.14.
 */
public class MapActivity extends Activity {

    public static volatile Typeface font;

    private static volatile GameThread thread = null;
    private static final Object monitor = new Object();

    private SurfaceView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (font != null) {
            font = Typeface.createFromAsset(getAssets(), "fonts/oleoscriptbold.ttf");
        }
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        view = new SurfaceView(this);
        setContentView(view);

        synchronized (monitor) {
            if (thread == null) {
                GameSession session = new GameSession(getResources());
                session.createNewWorld(240, 240);
                thread = new GameThread(monitor, session);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (monitor) {
            thread.resume(view.getHolder());
            view.setOnTouchListener(thread);
            monitor.notifyAll();
        }
    }

    @Override
    protected void onPause() {
        thread.setWaiting();
        while (!thread.isWaiting()) {
            Thread.yield();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
