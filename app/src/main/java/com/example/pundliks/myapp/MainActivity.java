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



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    int nbars = 0;
    int nvertices = 0;
    float barWidth = 0;
    float spd = 0.0f;
    float tan1deg = 0.01745506492821758576512889521973f;

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
                float sf = Float.valueOf(edtxtSF.getText().toString());
                spd = Float.valueOf(edtxtSpd.getText().toString());
                float dist = Float.valueOf(edtxtDist.getText().toString());

                float[] val = ComputePatternDetails(sf, dist);
                barWidth = val[0];
                nbars = (int)val[1];
                nvertices = (int)val[2];

                Intent intent = new Intent(view.getContext(), MyCameraActivity.class);
                intent.putExtra("EXTRA_VERTICES", nvertices);
                intent.putExtra("EXTRA_SPEED", spd);
                intent.putExtra("EXTRA_Speed", spd);
                intent.putExtra("EXTRA_Speed", spd);
                view.getContext().startActivity(intent);
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

        float ppd = dist*tan1deg*dm.densityDpi/2.54f;
        float bw = (float)Math.floor((double)ppd/sf);
        if (bw < 2.0f)
            bw = 2.0f;
        double ldim = dm.heightPixels;
        if (dm.heightPixels < dm.widthPixels)
            ldim = dm.widthPixels;
        int nbars = (int)Math.floor(ldim/bw) + 1; // H & W are reversed due to portrait mode
        int nv = nbars*4;

        Log.d(TAG, "===============================");
        Log.d(TAG, "Width = " + dm.widthPixels + ", Height = " + dm.heightPixels + ", PPI = " + dm.densityDpi);
        Log.d(TAG, "ppd = " + ppd + ", bw = " + bw + ", nbars = " + nbars + ", nv = " + nv);
        Log.d(TAG, "===============================");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new float[]{bw,nbars,nv};
    }
}
