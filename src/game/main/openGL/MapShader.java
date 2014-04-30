package game.main.openGL;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * хитрый шейдер.
 * Собственно, хитрость кроется в том, что можно изменить uniforms, которые отвечают за размер спрайтов и их сдвиг при
 * рисовании, и загрузить новые позиции на текстуре (aTexPos),
 * А aExtPos можно использовать много-много раз даже для спрайтов разного размера до тех пор, пока пользователь не
 * подвинет камеру. (В аExtPos пакуются два вектора - собственно позиция, и второй - с единичками и нулями по осям.
 * Created by lgor on 01.05.14.
 */
public class MapShader extends Shader {

    public final static String vertexCode = "" +
            "uniform vec2 uSpriteSize; " +
            "uniform vec2 uOffset;" +
            "uniform vec2 uMult;" +
            "attribute vec4 aExtPos;" +     //на самом деле, сюда упаковано 2 двухмерных вектора
            "attribute vec2 aTexPos; " +
            "varying vec2 vTexPos;" +

            "void main(){" +
            "   vTexPos = aTexPos + uSpriteSize * aExtPos.zw;" +
            "   gl_Position = vec4(aExtPos.xy + aExtPos.zw * uMult + uOffset, 0.0, 1.0);" +
            "}";

    public final static String pixelCode = "" +
            "uniform sampler2D uSampler;" +
            "varying mediump vec2 vTexPos;" +
            "void main(){" +
            "   gl_FragColor = texture2D(uSampler, vTexPos);" +
            "}";

    public final int uSampler, uSpriteSize, uOffset, uMult;
    public final int aExtPos, aTexPos;

    public MapShader() {
        super(vertexCode, pixelCode);

        uSampler = glGetUniformLocation(program, "uSampler");
        uSpriteSize = glGetUniformLocation(program, "uSpriteSize");
        uOffset = glGetUniformLocation(program, "uOffset");
        uMult = glGetUniformLocation(program, "uMult");

        aExtPos = glGetAttribLocation(program, "aExtPos");
        aTexPos = glGetAttribLocation(program, "aTexPos");
    }
}
