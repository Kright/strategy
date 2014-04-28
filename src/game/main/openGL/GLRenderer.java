package game.main.openGL;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Implementation of methods - initialization and drawing, which called in graphics thread.
 * Created by lgor on 27.04.14.
 */
class GLRenderer implements GLSurfaceView.Renderer {

    /**
     * screen coordinates:
     * left & right:    -+screenRatio
     * up & down:       +-1.0
     */
    private float screenRatio;
    private int screenWidth, screenHeight;

    private final DrawingContext drawingContext;

    public GLRenderer(DrawingContext drawingContext) {
        this.drawingContext = drawingContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        screenRatio = (float) width / height;
        screenHeight = height;
        screenWidth = width;
        GLES20.glClearColor(0, 0, 0, 1);

        //для того, чтобы нормально рисовались текстуты с прозрачностью. Надеюсь, будет работать;
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor((float) Math.random(), 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        drawingContext.repainted = true;
    }
}
