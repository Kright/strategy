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

    private static volatile GameThread2 session = null;
    private static final Object monitor = new Object();

    private SurfaceView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (font != null) {
            font = Typeface.createFromAsset(getAssets(), "fonts/oleoscriptbold.ttf");
        }
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        /*
        setContentView(new MapView(this));
        */
        view = new SurfaceView(this);
        setContentView(view);

        synchronized (monitor) {
            if (session == null) {
                session = new GameSession(monitor, getResources());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (monitor) {
            session.resume(view.getHolder());
            view.setOnTouchListener(session);
            monitor.notifyAll();
        }
    }

    @Override
    protected void onPause() {
        session.setWaiting();
        while (!session.isWaiting()){
            Thread.yield();
        }
        super.onPause();
    }
}
