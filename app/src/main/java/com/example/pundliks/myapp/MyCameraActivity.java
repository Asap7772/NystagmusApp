package com.example.pundliks.myapp;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.media.MediaRecorder;

public class MyCameraActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //-- Create a GLSurfaceView instance and set it
        //-- as the ContentView for this Activity.


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
        //setContentView(R.layout.activity_my_camera);

    }
}







