package com.example.pundliks.myapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;

import java.util.ArrayList;
import java.util.Arrays;

class MyGLSurfaceView extends GLSurfaceView {
    private final ParametrizedRenderer mRenderer;

    private int vertices, numBars;
    private float widthBars, speed;
    private int screenWidth,screenHeight;
    private CameraView cameraView;
    boolean video = false;
    boolean captured = false;

    public MyGLSurfaceView(final Context context, int vertices, int numBars, float width, float speed, int screenWidth, int screenHeight) {
        super(context);
        this.vertices = vertices;
        this.numBars = numBars;
        this.widthBars = width;
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new ParametrizedRenderer(vertices,numBars,widthBars,speed,screenWidth,screenHeight);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        cameraView = new CameraView(context);
        cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
        cameraView.setVideoQuality(CameraKit.Constants.VIDEO_QUALITY_HIGHEST);


        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(context,"Tapped",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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
}