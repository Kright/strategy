package game.main.openGL;

import android.opengl.GLES20;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * простейший шейдер, принимает коориднаты вершин, их текстурные координаты, текстуру и рисует это.
 * Created by lgor on 30.04.14.
 */
public class ShaderSprite extends Shader {

    final static String vertexShaderCode = "" +
            "attribute vec2 aPos;" +
            "attribute vec2 aTexPos;" +
            "varying vec2 vTexPos;" +
            "void main(){" +
            "   vTexPos = aTexPos;" +
            "   gl_Position = vec4(aPos, 0.0, 1.0);" +
            "}";

    final static String pixelShaderCode = "" +
            "uniform sampler2D uSampler;" +
            "varying mediump vec2 vTexPos;" +
            "void main(){" +
            "   gl_FragColor = texture2D(uSampler, vTexPos);" +
            "}";

    public final int uSampler;
    public final int aPosition, aTexturePosition;

    public ShaderSprite() {
        super(loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode),
                loadShader(GLES20.GL_FRAGMENT_SHADER, pixelShaderCode));

        uSampler = glGetUniformLocation(program, "uSampler");

        aPosition = glGetAttribLocation(program, "aPos");
        aTexturePosition = glGetAttribLocation(program, "aTexPos");
    }
}
