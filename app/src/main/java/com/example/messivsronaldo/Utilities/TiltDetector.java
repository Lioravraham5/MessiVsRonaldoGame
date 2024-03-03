package com.example.messivsronaldo.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.messivsronaldo.Interfaces.CallBackMoves;

public class TiltDetector {

    private final int CHECK_TIME = 300;
    private final Float CHECK_MOBILITY = 3.0f;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long timeStamp = 0l;

    private CallBackMoves callBackMoves;

    public TiltDetector (Context context, CallBackMoves callBackMoves) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE); //return the service that treat all the sensors
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // return TYPE_ACCELEROMETER sensors
        this.callBackMoves = callBackMoves;
        initEvenListener(); //start listening to changes in sensors
    }

    public void initEvenListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                calculateMovement(x);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //pass
            }
        };
    }

    public void calculateMovement(float x){
        if(System.currentTimeMillis() - timeStamp > CHECK_TIME){
            timeStamp = System.currentTimeMillis();
            calculateMovementZ(x);

        }

    }


    private void calculateMovementZ(float x) {
        if (x > CHECK_MOBILITY){
            if (callBackMoves != null){
                callBackMoves.moveLeft();
            }
        }
        else if (x < (-1 * CHECK_MOBILITY)) {
            if (callBackMoves != null){
                callBackMoves.moveRight();
            }
        }
    }

    public void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListener, sensor);
    }
}
