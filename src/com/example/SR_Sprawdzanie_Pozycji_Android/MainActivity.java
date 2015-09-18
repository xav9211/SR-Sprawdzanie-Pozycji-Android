package com.example.SR_Sprawdzanie_Pozycji_Android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdateTimeGravity=System.currentTimeMillis();
    private long lastUpdateTimeAcceleration=System.currentTimeMillis();

    //time in miliseconds to pass between measurements
    private final int TIME_SPAN=500;

    private double positionX=0.0;
    private double positionY=0.0;
    private double positionZ=0.0;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long actualTime=System.currentTimeMillis();
            if(actualTime-lastUpdateTimeAcceleration<TIME_SPAN) {
                return;
            } else {
                lastUpdateTimeAcceleration=actualTime;
            }
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            TextView xCoordinateView=(TextView)findViewById(R.id.XCoordinate);
            TextView yCoordinateView=(TextView)findViewById(R.id.YCoordinate);
            TextView zCoordinateView=(TextView)findViewById(R.id.ZCoordinate);

            xCoordinateView.setText(Float.toString(x));
            yCoordinateView.setText(Float.toString(y));
            zCoordinateView.setText(Float.toString(z));

        }  else if(mySensor.getType() == Sensor.TYPE_GRAVITY) {
            long actualTime=System.currentTimeMillis();
            if(actualTime-lastUpdateTimeGravity<TIME_SPAN) {
                return;
            } else {
                lastUpdateTimeGravity=actualTime;
                float x=sensorEvent.values[0];
                float y=sensorEvent.values[1];
                float z=sensorEvent.values[2];
                TextView xCoordinateView=(TextView)findViewById(R.id.XCoordinate);
                TextView yCoordinateView=(TextView)findViewById(R.id.YCoordinate);
                TextView zCoordinateView=(TextView)findViewById(R.id.ZCoordinate);
                xCoordinateView.setText(Float.toString(x));
                yCoordinateView.setText(Float.toString(y));
                zCoordinateView.setText(Float.toString(z));
                Button button=(Button)findViewById(R.id.restartButton);
                button.setTextColor(Color.BLUE);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
