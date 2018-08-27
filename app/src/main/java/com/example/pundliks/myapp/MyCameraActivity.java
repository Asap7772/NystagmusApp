package com.example.pundliks.myapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;

//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.media.MediaRecorder;

public class MyCameraActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;
    private int vertices, numBars;
    private float widthBars, speed;
    private int screenWidth,screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //-- Create a GLSurfaceView instance and set it
        //-- as the ContentView for this Activity.

        Intent i = getIntent();
        vertices = i.getIntExtra(MainActivity.EXTRA_VERTICES, 0);
        numBars = i.getIntExtra(MainActivity.EXTRA_BAR_NUMBER, 0);
        widthBars = i.getFloatExtra(MainActivity.EXTRA_BAR_WIDTH, 0);
        speed = i.getFloatExtra(MainActivity.EXTRA_SPEED, 0);
        screenWidth = i.getIntExtra(MainActivity.EXTRA_SCREEN_WIDTH,0);
        screenHeight = i.getIntExtra(MainActivity.EXTRA_SCREEN_HEIGHT,0);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)

        mGLView = new MyGLSurfaceView(this,vertices, numBars, widthBars,speed, screenWidth,screenHeight);
        setContentView(mGLView);
        //setContentView(R.layout.activity_my_camera);

    }
}







