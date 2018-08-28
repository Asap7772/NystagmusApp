package com.example.pundliks.stripes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MyRectangle {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private final int mProgram;
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix *vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    //-- number of coordinates per vertex in this array

    static final int COORDS_PER_VERTEX = 3;

    //static float shapeCoords[] = {
    //        -0.5f,  0.5f, 0.0f,   // top left
    //        -0.5f, -0.5f, 0.0f,   // bottom left
    //        0.5f, -0.5f, 0.0f,   // bottom right
    //        0.5f,  0.5f, 0.0f }; // top right

    //

    private int mPositionHandle;
    private int mColorHandle;

    private int vertexCount;
    private int vertexStride;

    private int mMVPMatrixHandle;

    private short[] drawOrder = {};

    // Set color with red, green, blue and alpha (opacity) values
    float rectcolor[] = { 0.9999f, 0.9999f, 0.9999f, 1.0f };


    public MyRectangle(float[] shapeCoords, short[] drawOrder) {
        //-- initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                //-- (# of coordinate values * 4 bytes per float)
                shapeCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(shapeCoords);
        vertexBuffer.position(0);

        //-- initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                //-- (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        vertexCount = shapeCoords.length / COORDS_PER_VERTEX;
        vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        this.drawOrder = drawOrder;

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the rectangle's vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the rectangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, rectcolor, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

         //// Draw the triangle
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
