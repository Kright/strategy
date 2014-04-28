package game.main.openGL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * class represents compiled shader program.
 * store OpenGL id numbers of shader program and pixel and vertex shaders
 * Created by lgor on 27.04.14.
 */
public class Shader {

    //handlers
    public final int program, pixel, vertex;

    public Shader(int vertex, int pixel) {
        this.vertex = vertex;
        this.pixel = pixel;
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertex);
        GLES20.glAttachShader(program, pixel);
        GLES20.glLinkProgram(program);
    }

    public void use() {
        GLES20.glUseProgram(program);
    }

    public static int loadShader(int type, String code) {
        int shaderID = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderID, code);
        GLES20.glCompileShader(shaderID);
        int[] ii = new int[1];
        GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, ii, 0);
        if (ii[0] == 0) {
            Log.e("myLog", "shader dont' compuled :( \n shader code :\n" + code + "\nerror info :\n" +
                    GLES20.glGetShaderInfoLog(shaderID));
            GLES20.glDeleteShader(shaderID);
        }
        return shaderID;
    }

    /**
     * must be called after all shaders compiling
     */
    public static void releaseCompiler() {
        GLES20.glReleaseShaderCompiler();
    }

    /*
     * didn't tested
     */
    public static int loadTexture(Resources resources, final int resourceId) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);
        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId, options);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        bitmap.recycle();
        return textureHandle[0];
    }
}
