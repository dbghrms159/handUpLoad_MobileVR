package kr.ac.hallym.skeleton;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by yuhogeun on 2017. 11. 17..
 */

public class tri
{
    private MainActivity main;
    private static final int COORDS_PER_VERTEX = 3;
    private static final float triCoords[] ={
//            0.0f,0.6f,-1.0f,
//            -0.5f, -0.3f, -1.0f,
//            0.5f, -0.3f, -1.0f,
//
//            0.0f,   0.6f,    -1.0f,
//            0.5f,   -0.3f,  -1.0f,
//            1.0f,   1.0f,    -1.0f
            -1.0f,  1.0f,   -1.0f,
            -1.0f,  -1.0f,  -1.0f,
            1.0f,   -1.0f,  -1.0f,
            -1.0f,  1.0f,   -1.0f,
            1.0f,   -1.0f,  -1.0f,
            1.0f,   1.0f,   -1.0f

    };

    private int tri;


    private final int triVertexCount = triCoords.length / COORDS_PER_VERTEX;

    private float triColor[] = { 0.8f, 0.6f, 0.2f, 0.0f};
    private FloatBuffer triBuffer;

    private int simpleVertexShader;
    private int simpleFragmentShader;

    private int triProgram, triPositionParam, triColorParam;

    public tri(MainActivity activity){
        main = activity;
        simpleVertexShader = main.loadShader(GLES20.GL_VERTEX_SHADER, R.raw.mvp_vertex);
        simpleFragmentShader = main.loadShader(GLES20.GL_FRAGMENT_SHADER,R.raw.simple_fragment);
        //버퍼 할당
        //GL 프로그램 생성
        //셰이더 파라미터 얻기
        //버퍼할당


//        ByteBuffer bb = ByteBuffer.allocateDirect(obj.positions.length * 4);
//        Log.i("te",obj.positions.length+"");
//        bb.order(ByteOrder.nativeOrder());
//
//        triBuffer = bb.asFloatBuffer();
//        triBuffer.put(obj.positions);
//        triBuffer.position(0);


        triProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(triProgram, simpleVertexShader);
        GLES20.glAttachShader(triProgram, simpleFragmentShader);
        GLES20.glLinkProgram(triProgram);
        GLES20.glUseProgram(triProgram);

        triPositionParam = GLES20.glGetAttribLocation(triProgram,"a_Position");
        GLES20.glEnableVertexAttribArray(triPositionParam);
        triColorParam = GLES20.glGetUniformLocation(triProgram, "u_Color");
        tri = GLES20.glGetUniformLocation(triProgram,"u_MVP");
    }

    public void draw(float[] per,float[] view){
        GLES20.glUseProgram(triProgram);

        float[] mvp = new float[16];
        Matrix.setIdentityM(mvp,0);
        Matrix.multiplyMM(mvp,0,per,0,view,0);
        GLES20.glUniformMatrix4fv(tri, 1,false, mvp,0);

        GLES20.glVertexAttribPointer(triPositionParam,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,0,triBuffer);

        GLES20.glUniform4fv(triColorParam,1,triColor,0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,triVertexCount);

    }
}
