package com.example.SR_Sprawdzanie_Pozycji_Android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.opengl.Matrix;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senMagnetic;
    private Sensor senGravity;

    private long lastUpdateTimeGravity=System.currentTimeMillis();
    private long lastUpdateTimeAcceleration=System.currentTimeMillis();
    private long lastUpdateTimeMagnetic=System.currentTimeMillis();

    //time in miliseconds to pass between measurements
    private final int TIME_SPAN=500;

    private double positionX=0.0;
    private double positionY=0.0;
    private double positionZ=0.0;

    private float[] magnetics=new float[3];
    private float[] gravities=new float[3];

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senMagnetic = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senGravity = senSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senMagnetic,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senGravity,SensorManager.SENSOR_DELAY_NORMAL);

        Button button=(Button)findViewById(R.id.restartButton);
        OnClickListener listener=new OnClickListener() {
            private MainActivity activity;
            public OnClickListener setActivity(MainActivity ma) {
                activity=ma;
                return this;
            }
            public void onClick(View v) {
                activity.resetPosition();
            }
        }.setActivity(this);

        button.setOnClickListener(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double timeDiff;
            long actualTime=System.currentTimeMillis();
            if(actualTime-lastUpdateTimeAcceleration<TIME_SPAN) {
                return;
            } else {
                timeDiff=(actualTime-lastUpdateTimeAcceleration)/1000.0;
                lastUpdateTimeAcceleration=actualTime;
            }
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //przeksztalcanie macierzy na wspolrzedne swiata
            float[] r=new float[16],i=new float[16];

            SensorManager.getRotationMatrix(r, i, gravities, magnetics);
            float[] invertResult=new float[16];
            Matrix.invertM(invertResult,0,r,0);

            float[] multiplicationResult=new float[4];
            Matrix.multiplyMV(multiplicationResult,0,invertResult,0,sensorEvent.values,0);

            TextView xCoordinateView=(TextView)findViewById(R.id.XCoordinate);
            TextView yCoordinateView=(TextView)findViewById(R.id.YCoordinate);
            TextView zCoordinateView=(TextView)findViewById(R.id.ZCoordinate);

            positionX+=multiplicationResult[0]*timeDiff*timeDiff;
            positionY+=multiplicationResult[1]*timeDiff*timeDiff;
            positionZ+=multiplicationResult[2]*timeDiff*timeDiff;

            xCoordinateView.setText(Double.toString(positionX));
            yCoordinateView.setText(Double.toString(positionY));
            zCoordinateView.setText(Double.toString(positionZ));

        }  else if(mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            long actualTime=System.currentTimeMillis();
            if(actualTime-lastUpdateTimeMagnetic<TIME_SPAN) {
                return;
            } else {
                lastUpdateTimeMagnetic=actualTime;
                float x=sensorEvent.values[0];
                float y=sensorEvent.values[1];
                float z=sensorEvent.values[2];
                TextView xCoordinateView=(TextView)findViewById(R.id.XCoordinate);
                TextView yCoordinateView=(TextView)findViewById(R.id.YCoordinate);
                TextView zCoordinateView=(TextView)findViewById(R.id.ZCoordinate);
/*
                xCoordinateView.setText(Float.toString(x));
                yCoordinateView.setText(Float.toString(y));
                zCoordinateView.setText(Float.toString(z));
*/
                //Button button=(Button)findViewById(R.id.restartButton);
                //button.setTextColor(Color.BLUE);
            }
        } else if(mySensor.getType() == Sensor.TYPE_GRAVITY) {
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
                /*
                xCoordinateView.setText(Float.toString(x));
                yCoordinateView.setText(Float.toString(y));
                zCoordinateView.setText(Float.toString(z));
                Button button=(Button)findViewById(R.id.restartButton);
                button.setTextColor(Color.BLUE);
                */
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
        senSensorManager.registerListener(this, senGravity, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void resetPosition() {
        positionX=0.0;
        positionY=0.0;
        positionZ=0.0;

        TextView xCoordinateView=(TextView)findViewById(R.id.XCoordinate);
        TextView yCoordinateView=(TextView)findViewById(R.id.YCoordinate);
        TextView zCoordinateView=(TextView)findViewById(R.id.ZCoordinate);

        xCoordinateView.setText(Double.toString(positionX));
        yCoordinateView.setText(Double.toString(positionY));
        zCoordinateView.setText(Double.toString(positionZ));
    }
}

