package game.main.openGL;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import game.main.utils.FPS;
import game.main.utils.Vector2f;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

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

    private FloatBuffer fb, fb2, fb3;

    ShaderSprite shaderSprite;
    MapShader mapShader2;

    TextureSprite grass;
    MapCameraGL.Grid grid;

    public GLRenderer(DrawingContext drawingContext) {
        this.drawingContext = drawingContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        drawingContext.loadTextures();
        shaderSprite = new ShaderSprite();
        mapShader2 = new MapShader();
        Shader.releaseCompiler();

        fb = ByteBuffer.allocateDirect(4 * 2 * 6).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.position(0);
        float l = 0.5f;
        fb.put(new float[]{-l, -l, l, -l, l, l, -l, -l, l, l, -l, l});

        l = 0.3f;
        fb3 = ByteBuffer.allocateDirect(4 * 2 * 6).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb3.position(0);
        fb3.put(new float[]{-l, -l, l, -l, l, l, -l, -l, l, l, -l, l});

        TextureSprite sprite = grass = drawingContext.getSprite("grass");
        fb2 = ByteBuffer.allocateDirect(4 * 2 * 6).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb2.position(0);

        fb2.put(new float[]{sprite.xLeft, sprite.yBottom, sprite.xRight, sprite.yBottom, sprite.xRight, sprite.yTop,
                sprite.xLeft, sprite.yBottom, sprite.xRight, sprite.yTop, sprite.xLeft, sprite.yTop});

        int[] max = new int[1];
        GLES20.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, max, 0);
        Log.e("", "maxTextureSize = " + max[0]);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        screenRatio = (float) width / height;
        screenHeight = height;
        screenWidth = width;
        glClearColor(0, 0, 0, 1);

        //для того, чтобы нормально рисовались текстуты с прозрачностью. Надеюсь, будет работать;
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        drawingContext.camera.scale(0.2f, screenRatio);
        grid = drawingContext.camera.getTestGrid();
        //grid.tableLoad(grass);
        float l = 0.6f;
        grid.mapTableLoad(drawingContext.camera.getIterator(-l, -l, l, l));
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClearColor((float) (counter % 2), 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);

        /*
        shaderSprite.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, drawingContext.textureHandle);
        glUniform1i(shaderSprite.uSampler, 0);

        fb2.position(0);
        glVertexAttribPointer(shaderSprite.aTexturePosition, 2, GL_FLOAT, false, 8, fb2);
        glEnableVertexAttribArray(shaderSprite.aTexturePosition);
        {
            fb.position(0);
            glVertexAttribPointer(shaderSprite.aPosition, 2, GL_FLOAT, false, 8, fb);
            glEnableVertexAttribArray(shaderSprite.aPosition);
            {
                glDrawArrays(GL_TRIANGLES, 0, 6);
            }
            glDisableVertexAttribArray(shaderSprite.aPosition);
        }

        {
            fb3.position(0);
            glVertexAttribPointer(shaderSprite.aPosition, 2, GL_FLOAT, false, 8, fb3);
            glEnableVertexAttribArray(shaderSprite.aPosition);
            {
                glDrawArrays(GL_TRIANGLES, 0, 6);
            }
            glDisableVertexAttribArray(shaderSprite.aPosition);
        }
        glDisableVertexAttribArray(shaderSprite.aTexturePosition);

        */
        for (int i = 0; i < 1; i++) {
            drawGrass();
        }

        counter++;
        fps.get();
        if (counter % 100 == 0) {
            Log.e("", "fps = " + fps.get());
        }

        drawingContext.repainted = true;
    }

    FPS fps = new FPS();
    int counter = 0;

    void drawGrass() {
        mapShader2.use();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, drawingContext.textureHandle);

        glUniform1i(mapShader2.uSampler, 0);
        glUniform2f(mapShader2.uTexSize, grass.xRight - grass.xLeft, grass.yBottom - grass.yTop);
        Vector2f size = grid.getSpriteSize();
        glUniform2f(mapShader2.uSize, size.x, -size.y);

        {
            grid.aExtPos.position(0);
            glVertexAttribPointer(mapShader2.aExtendedPosition, 4, GL_FLOAT, false, 16, grid.aExtPos);
            glEnableVertexAttribArray(mapShader2.aExtendedPosition);
            {
                grid.vertPos.position(0);
                glVertexAttribPointer(mapShader2.aTexturePosition, 2, GL_FLOAT, false, 8, grid.vertPos);
                glEnableVertexAttribArray(mapShader2.aTexturePosition);
                {
                    glDrawArrays(GL_TRIANGLES, 0, 6 * grid.rectsNum);
                }
                glDisableVertexAttribArray(mapShader2.aTexturePosition);
            }
            glDisableVertexAttribArray(mapShader2.aExtendedPosition);
        }
    }
}
