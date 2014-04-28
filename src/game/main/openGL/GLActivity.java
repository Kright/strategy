package game.main.openGL;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Ещё одно, альтернативное активити.
 * Будет использовано для того, чтобы устроить рисование игры через openGL
 * Created by lgor on 27.04.14.
 */
public class GLActivity extends Activity {

    private GLSurfaceView viewGL;
    private GLRenderer renderer;
    private GLGameSession gameSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawingContext drawingContext = new DrawingContext(getResources());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        viewGL = new GLSurfaceView(this);
        viewGL.setPreserveEGLContextOnPause(true);
        viewGL.setEGLContextClientVersion(2);

        renderer = new GLRenderer(drawingContext);
        viewGL.setRenderer(renderer);
        viewGL.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        setContentView(viewGL);

        if (gameSession == null) {
            gameSession = new GLGameSession(viewGL, drawingContext);
            Thread t = new Thread(gameSession);
            t.setDaemon(true);
            t.start();
        }
        /* странное решение, но так работает.   Я не против сделать по-другому
         * В данный момент демон остаётся жив, если свернуть приложение, и втихаря работает дальше. Забавная возможность,
         * но мне нужно нечно иное
         */
    }

    @Override
    protected void onResume() {
        viewGL.onResume();
        gameSession.setRunning(true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        viewGL.onPause();
        gameSession.setRunning(false);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
