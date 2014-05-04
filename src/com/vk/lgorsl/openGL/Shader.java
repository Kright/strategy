package com.vk.lgorsl.openGL;

import android.opengl.GLES20;
import android.util.Log;

import static android.opengl.GLES20.*;

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

    public Shader(String vertexCode, String fragmentCode) {
        this(loadShader(GL_VERTEX_SHADER, vertexCode), loadShader(GL_FRAGMENT_SHADER, fragmentCode));
    }

    public void use() {
        GLES20.glUseProgram(program);
    }

    public static int loadShader(int type, String code) {
        int shaderID = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderID, code);
        glCompileShader(shaderID);
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
}
