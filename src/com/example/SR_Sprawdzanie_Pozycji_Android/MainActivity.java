package com.example.SR_Sprawdzanie_Pozycji_Android;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdateTime=System.currentTimeMillis();
    //time in miliseconds to pass between measurements
    private final int TIME_SPAN=500;
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

        long actualTime=System.currentTimeMillis();
        if(actualTime-lastUpdateTime<TIME_SPAN) {
            return;
        } else {
            lastUpdateTime=actualTime;
        }

        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            TextView xCoordinateView=(TextView)findViewById(R.id.XCoordinate);
            TextView yCoordinateView=(TextView)findViewById(R.id.YCoordinate);
            TextView zCoordinateView=(TextView)findViewById(R.id.ZCoordinate);
            xCoordinateView.setText(Float.toString(x));
            yCoordinateView.setText(Float.toString(y));
            zCoordinateView.setText(Float.toString(z));
        } else if(mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float xRotation = sensorEvent.values[0];
            float yRotation = sensorEvent.values[1];
            float zRotation = sensorEvent.values[2];
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
