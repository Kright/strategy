package game.main.openGL;

import android.opengl.GLES20;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * простейший шейдер, принимает коориднаты вершин, их текстурные координаты, текстуру и рисует это.
 * Created by lgor on 30.04.14.
 */
public class MapShader extends Shader {

    final static String vertexShaderCode = "" +
            "uniform vec2 uTranslate;" +
            "uniform vec2 uTexSize;" +          //размер изображений на текстуре
            "uniform vec2 uSize;" +             //видимый размер спрайта
            "attribute vec4 aExtPos; " +
            "attribute vec2 aTexPos;" +
            "varying vec2 vTexPos;" +
            "void main(){" +
            "   gl_Position = vec4(uTranslate + aExtPos.xy + aExtPos.zw * uSize, 0.0, 1.0);" +
            "   vTexPos = aTexPos + uTexSize * aExtPos.zw; " +
            "}";

    final static String pixelShaderCode = "" +
            "uniform sampler2D uSampler;" +
            "varying vec2 vTexPos;" +
            "void main(){" +
            "   gl_FragColor =  gl_FragColor = texture2D(uSampler, vTexPos);" +
            "}";

    public final int uSize, uTexSize, uSampler, uTranslate;
    public final int aExtendedPosition, aTexturePosition;

    public MapShader() {
        super(loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode),
                loadShader(GLES20.GL_FRAGMENT_SHADER, pixelShaderCode));

        uSize = glGetUniformLocation(program, "uSize");
        uTexSize = glGetUniformLocation(program, "uTexSize");
        uSampler = glGetUniformLocation(program, "uSampler");
        uTranslate = glGetUniformLocation(program, "uTranslate;");

        aExtendedPosition = glGetAttribLocation(program, "aExtPos");
        aTexturePosition = glGetAttribLocation(program, "aTexPos");
    }
}
