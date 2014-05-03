package game.main.openGL;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

/**
 * Ещё одно, альтернативное активити.
 * Будет использовано для рисования игры через openGL
 * Created by lgor on 27.04.14.
 */
public class GLActivity extends Activity {

    private GLSurfaceView viewGL;
    private GLRenderer renderer;
    private GLGameSession gameSession;

    public GLActivity() {
        myLog("Activity constructed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawingContext drawingContext = new DrawingContext(getResources());

        viewGL = new GLSurfaceView(this);

        myLog("onCreate");
        if (gameSession == null) {
            myLog("gameSession is null, will create");
            gameSession = new GLGameSession(viewGL, drawingContext);
            Thread t = new Thread(gameSession);
            t.start();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        viewGL.setPreserveEGLContextOnPause(true);
        viewGL.setEGLContextClientVersion(2);

        renderer = new GLRenderer(drawingContext);
        viewGL.setRenderer(renderer);
        viewGL.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        setContentView(viewGL);
    }

    @Override
    protected void onResume() {
        myLog("onResume");
        viewGL.onResume();
        gameSession.setRunning(true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        myLog("onPause");
        viewGL.onPause();
        gameSession.setRunning(false);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        myLog("I'll back");
        gameSession.finish();
        super.onBackPressed();
    }

    public static void myLog(Object o) {
        Log.d("execution", o.toString());
    }
}
