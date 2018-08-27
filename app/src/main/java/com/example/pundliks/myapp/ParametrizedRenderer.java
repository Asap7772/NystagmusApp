package com.example.pundliks.myapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ParametrizedRenderer implements GLSurfaceView.Renderer {

    private int screenWidth, screenHeight;
    private int vertices, numBars;
    private float widthBars, speed;
    private float[] shapeCoords;
    private short[] drawOrder;
    private MyRectangle mRect;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    private static final String TAG = "MyRenderer";

    private float xpos = 0.0f;
    private float increment = 0.01f;
    private float aa = 0.571429f;
    private long prevTime = 0;

    public ParametrizedRenderer(int vertices, int numBars, float widthBars, float speed,int screenWidth, int screenHeight) {
        this.vertices = vertices;
        this.numBars = numBars;
        this.widthBars = widthBars;
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        generateRectangles();
        mRect = new MyRectangle(shapeCoords, drawOrder);
    }

    private void generateDefaultRectangles() {
        shapeCoords = MyGLRenderer.shapeCoords;
        drawOrder = MyGLRenderer.drawOrder;
    }

    /*
        shapeCoords[] = {
                -aa, 1.0f, 0.0f,   // top left
                -aa, -1.0f, 0.0f,   // bottom left
                1.8f, -1.0f, 0.0f,   // bottom right
                1.8f, 1.0f, 0.0f}; // top right
        drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices
    */

    private void generateRectangles(){
        aa = widthBars/Math.max(this.screenHeight, this.screenWidth);
        increment = aa/10.0f;
        shapeCoords = new float[3*(vertices+4)];

        int vNum = 0;
        for(int i = 0; i<=numBars;i++){
            int barNum = i*2-2;
            float[] topLeft = new float[]{-1 + barNum * aa, 1.0f, 0.0f};
            float[] bottomLeft = new float[]{-1 + barNum * aa, -1.0f, 0.0f};
            float[] bottomRight = new float[]{-1 + (barNum +1) * aa, -1.0f, 0.0f};
            float[] topRight = new float[]{-1 + (barNum + 1) * aa, 1.0f, 0.0f};

            topLeft[0] = topLeft[0]*2;
            bottomLeft[0] = bottomLeft[0]*2;
            bottomRight[0] = bottomRight[0]*2;
            topRight[0] = topRight[0]*2;


            for(float f: topLeft){
                shapeCoords[vNum] = f;
                vNum += 1;
            }

            for(float f: bottomLeft){
                shapeCoords[vNum] = f;
                vNum += 1;
            }

            for(float f: bottomRight){
                shapeCoords[vNum] = f;
                vNum += 1;
            }

            for(float f: topRight){
                shapeCoords[vNum] = f;
                vNum += 1;
            }
        }

        generateDrawOrder();
    }

    private void generateDrawOrder() {
        drawOrder = new short[(numBars+1) * 6];
        int oNum = 0;
        for(int i = 0; i <= numBars;i++){
            drawOrder[oNum] = (short)(i*4);
            oNum += 1;

            drawOrder[oNum] = (short)(i*4 + 1);
            oNum += 1;

            drawOrder[oNum] = (short)(i*4 + 2);
            oNum += 1;

            drawOrder[oNum] = (short)(i*4);
            oNum += 1;

            drawOrder[oNum] = (short)(i*4 + 2);
            oNum += 1;

            drawOrder[oNum] = (short)(i*4 + 3);
            oNum += 1;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
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

    public void onDrawFrame(GL10 unused) {

        float[] scratch = new float[16];

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        xpos = xpos + increment;
        if (xpos >= 4*aa)
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

        if(System.currentTimeMillis()-prevTime > 250) {
            Log.d(TAG, this.toString());
            prevTime = System.currentTimeMillis();
        }
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public int getNumBars() {
        return numBars;
    }

    public void setNumBars(int numBars) {
        this.numBars = numBars;
    }

    public float getWidthBars() {
        return widthBars;
    }

    public void setWidthBars(float widthBars) {
        this.widthBars = widthBars;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    @Override
    public String toString() {
        return "\nValues\n" + Arrays.toString(this.drawOrder)
                + "\n" + Arrays.toString(this.shapeCoords)
                + "\n" + "aa = " + aa
                + "\n" + Arrays.toString(new float[]{getNumBars(),getVertices(), getWidthBars(),getSpeed(),getScreenWidth(), getScreenHeight()});
    }
}

