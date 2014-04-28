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
public class GLActivity extends Activity{

    private GLSurfaceView viewGL;
    private GLRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        viewGL = new GLSurfaceView(this);
        viewGL.setPreserveEGLContextOnPause(true);
        viewGL.setEGLContextClientVersion(2);

        renderer = new GLRenderer();
        viewGL.setRenderer(renderer);
        viewGL.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setContentView(viewGL);
    }

    @Override
    protected void onResume() {
        viewGL.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        viewGL.onPause();
        super.onPause();
    }
}
