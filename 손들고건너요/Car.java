package kr.ac.hallym.skeleton;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by yuhogeun on 2017. 11. 20..
 */

public class Car {
    private MainActivity main;
    private static final int COORDS_PER_VERTEX = 3;
    private static final float triCoords[] ={

            -2.5f,  0.5f,   -1.0f, //back
            2.5f,   0.5f,   -1.0f,
            2.5f,   0.0f,  -1.0f,
            -2.5f,  0.5f,   -1.0f,
            2.5f,   -0.0f,  -1.0f,
            -2.5f,  -0.0f,  -1.0f,

            -2.5f,  -0.0f,  1.0f, //front
            2.5f,   -0.0f,  1.0f,
            2.5f,   0.5f,   1.0f,
            -2.5f,  -0.0f,  1.0f,
            2.5f,   0.5f,   1.0f,
            -2.5f,  0.5f,   1.0f,

            -2.5f,  -0.0f,  -1.0f, //bottom
            2.5f,   -0.0f,  -1.0f,
            2.5f,   -0.0f,  1.0f,
            -2.5f,  -0.0f,  -1.0f,
            2.5f,   -0.0f,  1.0f,
            -2.5f,  -0.0f,  1.0f,

            2.5f,   0.5f,   -1.0f, //top
            -2.5f,  0.5f,   -1.0f,
            -2.5f,  0.5f,   1.0f,
            2.5f,   0.5f,   -1.0f,
            -2.5f,  0.5f,   1.0f,
            2.5f,   0.5f,   1.0f,

            2.5f,   -0.0f,  -1.0f, //right
            2.5f,   0.5f,   -1.0f,
            2.5f,   0.5f,   1.0f,
            2.5f,   -0.0f,  -1.0f,
            2.5f,   0.5f,   1.0f,
            2.5f,   -0.0f,  1.0f,

            -2.5f,  0.5f,   1.0f, //left
            -2.5f,  0.5f,   -1.0f,
            -2.5f,  -0.0f,  -1.0f,
            -2.5f,  0.5f,   1.0f,
            -2.5f,  -0.0f,  -1.0f,
            -2.5f,  -0.0f,  1.0f,
            ////////////////////////

            -1.0f,  1.0f,   -1.0f, //back
            1.0f,   1.0f,   -1.0f,
            1.0f,   0.5f,  -1.0f,
            -1.0f,  1.0f,   -1.0f,
            1.0f,   0.5f,  -1.0f,
            -1.0f,  0.5f,  -1.0f,

            -1.0f,  0.5f,  1.0f, //front
            1.0f,   0.5f,  1.0f,
            1.0f,   1.0f,   1.0f,
            -1.0f,  0.5f,  1.0f,
            1.0f,   1.0f,   1.0f,
            -1.0f,  1.0f,   1.0f,

            1.0f,   1.0f,   -1.0f, //top
            -1.0f,  1.0f,   -1.0f,
            -1.0f,  1.0f,   1.0f,
            1.0f,   1.0f,   -1.0f,
            -1.0f,  1.0f,   1.0f,
            1.0f,   1.0f,   1.0f,

            1.0f,   0.5f,  -1.0f, //right
            1.0f,   1.0f,   -1.0f,
            1.0f,   1.0f,   1.0f,
            1.0f,   0.5f,  -1.0f,
            1.0f,   1.0f,   1.0f,
            1.0f,   0.5f,  1.0f,

            -1.0f,  1.0f,   1.0f, //left
            -1.0f,  1.0f,   -1.0f,
            -1.0f,  0.5f,  -1.0f,
            -1.0f,  1.0f,   1.0f,
            -1.0f,  0.5f,  -1.0f,
            -1.0f,  0.5f,  1.0f,



    };
    static float vertexUVs[] =
            {
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f,

                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f,   0.0f,
                    1.0f,  1.0f,
                    1.0f,  0.0f

            };


    private int tri;


    private final int triVertexCount = triCoords.length / COORDS_PER_VERTEX;

    private float triColor[] = { 0.8f, 0.6f, 0.2f, 0.0f};
    private FloatBuffer triBuffer,texBuffer;

    private int simpleVertexShader;
    private int simpleFragmentShader;

    private int triProgram, triPositionParam, triColorParam;
    private int[] textureID = new int[1];

    public Car(MainActivity activity, Bitmap bitmap){
        main = activity;

        ByteBuffer bb = ByteBuffer.allocateDirect(triCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        triBuffer = bb.asFloatBuffer();
        triBuffer.put(triCoords);
        triBuffer.position(0);

        bb = ByteBuffer.allocateDirect(vertexUVs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        texBuffer = bb.asFloatBuffer();
        texBuffer.put(vertexUVs);
        texBuffer.position(0);

        triProgram = GLES20.glCreateProgram();
        simpleVertexShader = main.loadShader(GLES20.GL_VERTEX_SHADER, R.raw.texture_vertex);
        simpleFragmentShader = main.loadShader(GLES20.GL_FRAGMENT_SHADER,R.raw.texture_frag);

        GLES20.glAttachShader(triProgram, simpleVertexShader);
        GLES20.glAttachShader(triProgram, simpleFragmentShader);
        GLES20.glLinkProgram(triProgram);
        //GLES20.glUseProgram(triProgram);

        triPositionParam = GLES20.glGetAttribLocation(triProgram,"vPosition");
        triColorParam = GLES20.glGetAttribLocation(triProgram, "vTexCoord");
        tri = GLES20.glGetUniformLocation(triProgram,"MVP");

        GLES20.glGenTextures(1, textureID, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST_MIPMAP_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
    }

    public void draw(float[] per,float[] view,float[] model){
        GLES20.glUseProgram(triProgram);

        GLES20.glEnableVertexAttribArray(triPositionParam);
        GLES20.glVertexAttribPointer(triPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false,0,triBuffer);

        GLES20.glEnableVertexAttribArray(triColorParam);
        GLES20.glVertexAttribPointer(triColorParam, 2, GLES20.GL_FLOAT,
                false, 0, texBuffer);

        float[] mvp = new float[16];
        Matrix.setIdentityM(mvp,0);
        Matrix.multiplyMM(mvp,0,per,0,view,0);
        Matrix.multiplyMM(mvp, 0, mvp, 0, model, 0);
        GLES20.glUniformMatrix4fv(tri, 1,false, mvp,0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,triVertexCount);
    }
}
