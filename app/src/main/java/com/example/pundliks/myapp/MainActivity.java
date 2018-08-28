package com.example.pundliks.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String EXTRA_PPD = "EXTRA_PPD";
    public static final String EXTRA_VERTICES = "EXTRA_VERTICES";
    public static final String EXTRA_SPEED = "EXTRA_SPEED";
    public static final String EXTRA_BAR_NUMBER = "EXTRA_BAR_NUMBER";
    public static final String EXTRA_BAR_WIDTH = "EXTRA_BAR_WIDTH";
    public static final String EXTRA_SCREEN_WIDTH = "EXTRA_SCREEN_WIDTH";
    public static final String EXTRA_SCREEN_HEIGHT = "EXTRA_SCREEN_HEIGHT";

    int nbars = 0;
    int nvertices = 0;
    float barWidth = 0;
    float spd = 0.0f;
    private float ppd;
    private int screenWidth, screenHeight;

    public static final float tan1deg = 0.01745506492821758576512889521973f;
    public static final float ppi = 570;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText edtxtSF = findViewById(R.id.editText_param1);
        final EditText edtxtSpd = findViewById(R.id.editText_param2);
        final EditText edtxtDist = findViewById(R.id.editText_param3);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float sf = Float.valueOf(edtxtSF.getText().toString());
                    spd = Float.valueOf(edtxtSpd.getText().toString());
                    float dist = Float.valueOf(edtxtDist.getText().toString());

                    float[] val = ComputePatternDetails(sf, dist);
                    barWidth = val[0];
                    nbars = (int) val[1];
                    nvertices = (int) val[2];
                    screenWidth = (int) val[3];
                    screenHeight = (int) val[4];
                    ppd = val[5];

                    Intent intent = new Intent(view.getContext(), MyCameraActivity.class);
                    intent.putExtra(EXTRA_VERTICES, nvertices);
                    intent.putExtra(EXTRA_SPEED, spd);
                    intent.putExtra(EXTRA_PPD, ppd);
                    intent.putExtra(EXTRA_BAR_NUMBER, nbars);
                    intent.putExtra(EXTRA_BAR_WIDTH, barWidth);
                    intent.putExtra(EXTRA_SCREEN_WIDTH, screenWidth);
                    intent.putExtra(EXTRA_SCREEN_HEIGHT, screenHeight);
                    view.getContext().startActivity(intent);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Please Enter Query",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public float[] ComputePatternDetails(float sf, float dist){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        float ppd = dist*tan1deg*ppi/2.54f;
        float bw = (float)Math.floor((double)ppd/sf);
        if (bw < 2.0f)
            bw = 2.0f;
        double ldim = dm.heightPixels;
        if (dm.heightPixels < dm.widthPixels)
            ldim = dm.widthPixels;
        int nbars = (int)Math.floor(ldim/bw) + 1; // H & W are reversed due to portrait mode
        int nv = nbars*4;

        Log.d(TAG, "===============================");
        Log.d(TAG, "Width = " + dm.widthPixels + ", Height = " + dm.heightPixels + ", PPI = " + ppi);
        Log.d(TAG, "ppd = " + ppd + ", bw = " + bw + ", nbars = " + nbars + ", nv = " + nv);
        Log.d(TAG, "===============================");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new float[]{bw,nbars,nv, dm.widthPixels, dm.heightPixels, ppd};
    }
}
