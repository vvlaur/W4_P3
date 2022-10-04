package com.example.w4_p3;


import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.SeekableByteChannel;


//references: youtube.com/watch?v=zUzZ67grYt8
public class MainActivity extends AppCompatActivity {
    //variables
    TextView txt_accel;

    private double accelCurrentValue;
    private double accelPreviousValue;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private double xprev;
    private double yprev;
    private double zprev;

    private WebView webx;
    private String largest_changed;

    SeekBar sigchange;
    private int progchange;
    public String largest;


    private String getMaxAxis(double x, double y, double z){
        //function to find axis with largest change, returns name of axis
        if (x >= y && x >=z){
            return "x";
        } else if (y >=x && y >=z){
            return "y";
        }
        return "z";
    }

    public void showWebViewURL(String res){
    //function to display webpage based on largest change
        if (res == "x"){
            webx = (WebView) findViewById(R.id.webx);
            WebSettings websettings = webx.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setLoadsImagesAutomatically(true);
            webx.setWebViewClient(new WebViewClient());
            webx.loadUrl("https;//ecosia.org/");
        }
        if(res == "y"){
            webx = (WebView) findViewById(R.id.webx);
            WebSettings websettings = webx.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setLoadsImagesAutomatically(true);
            webx.setWebViewClient(new WebViewClient());
            webx.loadUrl("https://dogpile.com/");
        }
        if(res == "z"){
            webx = (WebView) findViewById(R.id.webx);
            WebSettings websettings = webx.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setLoadsImagesAutomatically(true);
            webx.setWebViewClient(new WebViewClient());
            webx.loadUrl("https://webb.nasa.gov/");
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        //calibrate motion sensor
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            accelCurrentValue = Math.sqrt( ( x*x + y*y + z*z));
            double changed = Math.abs(accelCurrentValue - accelPreviousValue);
            accelPreviousValue= accelCurrentValue;

            double x_change = Math.abs(x - xprev);
            xprev = x;
            double y_change = Math.abs(y - yprev);
            yprev = y;
            double z_change = Math.abs(y - zprev);
            zprev = z;

            largest_changed = getMaxAxis(x_change, y_change, z_change);
            largest = largest_changed;
            if (largest_changed == "x" && x_change > progchange) {
                Toast.makeText(getApplicationContext(), "Movement on the X axis", Toast.LENGTH_LONG).show();
                Log.i("x","Change in X axis");
                showWebViewURL(largest_changed);
            } else if(largest_changed == "y" && y_change > progchange) {
                Toast.makeText(getApplicationContext(), "Movement on the Y axis", Toast.LENGTH_LONG).show();
                Log.i("y","Change in Y axis");
                showWebViewURL(largest_changed);
            } else if(largest_changed == "z" && z_change > progchange) {
                Toast.makeText(getApplicationContext(), "Movement on the Z axis", Toast.LENGTH_LONG).show();
                Log.i("z","Change in Z axis");
                showWebViewURL(largest_changed);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sets orientation to only be portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //sets textview
        txt_accel = findViewById(R.id.txt_accel);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sigchange = (SeekBar) findViewById(R.id.sigchange);

        sigchange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txt_accel.setText("Significant Change: " + String.valueOf(progress));
                progchange = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }
}