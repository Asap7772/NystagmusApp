package com.example.pundliks.stripes;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

class MyGLSurfaceView extends GLSurfaceView {
    private final ParametrizedRenderer mRenderer;

    private int vertices, numBars;
    private float widthBars, speed;
    private int screenWidth;
    private int screenHeight;
    private float ppd;
    private boolean video, captured;

    public MyGLSurfaceView(final Context context, int vertices, int numBars, float width, float speed, int screenWidth, int screenHeight, float ppd) {
        super(context);
        this.vertices = vertices;
        this.numBars = numBars;
        this.widthBars = width;
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.ppd = ppd;


        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new ParametrizedRenderer(vertices,numBars,widthBars,speed,screenWidth,screenHeight,ppd);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);


        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(video){
                    if(captured){
                        Toast.makeText(context, "Video already Captured", Toast.LENGTH_SHORT).show();
                    }else{
                        captured = true;
                        Toast.makeText(context, "Video Stopped", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    video = true;
                    Toast.makeText(context, "Started Video", Toast.LENGTH_SHORT).show();
                }
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


    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public float getPpd() {
        return ppd;
    }

    public void setPpd(float ppd) {
        this.ppd = ppd;
    }
}