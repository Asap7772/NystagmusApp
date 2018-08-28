package com.example.pundliks.stripes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.util.Date;

//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.media.MediaRecorder;

public class MyCameraActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE = 5;
    private MyGLSurfaceView mGLView;
    private int vertices, numBars;
    private float widthBars, speed;
    private int screenWidth,screenHeight;
    private CameraView cameraView;
    private Thread t;
    private float ppd;

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
        ppd = i.getFloatExtra(MainActivity.EXTRA_PPD, 0);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)

        mGLView = new MyGLSurfaceView(this,vertices, numBars, widthBars,speed, screenWidth,screenHeight,ppd);
        cameraView = new CameraView(this);

        cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
        cameraView.setPermissions(CameraKit.Constants.PERMISSIONS_STRICT);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
                Log.e("ERROR", cameraKitEvent.getMessage());
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
                Log.e("ERROR", cameraKitError.getMessage());
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Log.e("testing-tag", String.valueOf(cameraKitImage.getJpeg()));
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
                Log.e("testing-tag", String.valueOf(cameraKitVideo.getVideoFile()));
                File from = cameraKitVideo.getVideoFile();
                File to = getOutputMediaFile();
                from.renameTo(to);
            }
        });

        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mGLView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cameraView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        FloatingActionButton back = new FloatingActionButton(this);

        back.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) back.getLayoutParams();
        int margin = (int) getResources().getDimension(R.dimen.fab_margin);
        p.setMargins(margin,margin,margin,margin);
        back.setLayoutParams(p);

        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        layout.addView(cameraView);
        layout.addView(mGLView);
        layout.addView(back);

        setContentView(layout);


        //setContentView(R.layout.activity_my_camera);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE);
        }

        t = new Thread() {
            @Override
            public void run() {
                while(!mGLView.isVideo()){}
                cameraView.captureVideo();
                while(!mGLView.isCaptured()){}
                cameraView.stopVideo();
            }
        };

        t.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
        if(t != null){
            t.interrupt();
            t = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
        if(t != null){
            t.interrupt();
            t = null;
        }
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "OpenGLES");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("SupervisionSearch", "failed to create directory");
                return null;
            }
        }

        Date d = new Date();
        // Create a media file name
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator+d.getTime()+"_video.mp4");

        return mediaFile;
    }

}







