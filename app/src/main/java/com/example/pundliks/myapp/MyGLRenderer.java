package com.example.pundliks.myapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyRenderer";

    private float xpos = 0.0f;
    private float increment = 0.01f;
    private MyRectangle mRect;
/*

    static float shapeCoords[] = {
            -aa, 1.0f, 0.0f,   // top left
            -aa, -1.0f, 0.0f,   // bottom left
            1.8f, -1.0f, 0.0f,   // bottom right
            1.8f, 1.0f, 0.0f}; // top right
    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices
*/
/*
    static float shapeCoords[] = {
            -2.0f, 1.0f, 0.0f,
            -2.0f, -1.0f, 0.0f,
            -1.3333f, -1.0f, 0.0f,
            -1.3333f, 1.0f, 0.0f,
            -0.6667f, 1.0f, 0.0f,
            -0.6667f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.6667f, 1.0f, 0.0f,
            0.6667f, -1.0f, 0.0f,
            1.3333f, -1.0f, 0.0f,
            1.3333f, 1.0f, 0.0f,
            2.0f, 1.0f, 0.0f,
            2.0f, -1.0f, 0.0f};
    private short drawOrder[] = {0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8, 10, 11}; // order to draw vertices
*/
/*
    static final float aa = 4/7;
    static float shapeCoords[] = {
            -2.0f, 1.0f, 0.0f,
            -2.0f, -1.0f, 0.0f,
            -(aa/2) - (2*aa), -1.0f, 0.0f,
            -(aa/2) - (2*aa), 1.0f, 0.0f,
            -(aa/2) - aa, 1.0f, 0.0f,
            -(aa/2) - aa, -1.0f, 0.0f,
            -aa/2, -1.0f, 0.0f,
            -aa/2, 1.0f, 0.0f,
            aa/2, 1.0f, 0.0f,
            aa/2, -1.0f, 0.0f,
            (aa/2) + aa, -1.0f, 0.0f,
            (aa/2) + aa, 1.0f, 0.0f,
            (aa/2) + (2*aa), 1.0f, 0.0f,
            (aa/2) + (2*aa), -1.0f, 0.0f,
            2.0f, -1.0f, 0.0f,
            2.0f, 1.0f, 0.0f};
    private short drawOrder[] = {0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8, 10, 11, 12, 13, 14, 12, 14, 15}; // order to draw vertices
*/
    static final float aa = 0.571429f;
    static float shapeCoords[] = {
            -3.142858f, 1.0f, 0.0f,
            -3.142858f, -1.0f, 0.0f,
            -2.571429f, -1.0f, 0.0f,
            -2.571429f, 1.0f, 0.0f,
            -2.0f, 1.0f, 0.0f,
            -2.0f, -1.0f, 0.0f,
            -1.4285725f, -1.0f, 0.0f,
            -1.4285725f, 1.0f, 0.0f,    //-- end of rect 1
            -0.8571435f, 1.0f, 0.0f,
            -0.8571435f, -1.0f, 0.0f,
            -0.2857145f, -1.0f, 0.0f,
            -0.2857145f, 1.0f, 0.0f,    //-- end of rect 2
            0.2857145f, 1.0f, 0.0f,
            0.2857145f, -1.0f, 0.0f,
            0.8571435f, -1.0f, 0.0f,
            0.8571435f, 1.0f, 0.0f,     //-- end of rect 3
            1.4285725f, 1.0f, 0.0f,
            1.4285725f, -1.0f, 0.0f,
            2.0f, -1.0f, 0.0f,
            2.0f, 1.0f, 0.0f};
    private short drawOrder[] = {0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8, 10, 11, 12, 13, 14, 12, 14, 15, 16, 17, 18, 16, 18, 19}; // order to draw vertices

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mRect = new MyRectangle(shapeCoords, drawOrder);

    }

    public void onDrawFrame(GL10 unused) {

        float[] scratch = new float[16];

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        xpos = xpos + increment;
        if (xpos >= 2*aa)
            xpos = 0.0f;

        //-- Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.00090f * ((int) time);

        //Log.d(TAG, "time = " + time + ", angle = " + angle);

        //Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, xpos, 0, 0);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -1, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        //mRect.draw(mMVPMatrix);
        mRect.draw(scratch);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width/height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 100);
        Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, -1, 1);
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}

