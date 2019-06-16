package vite.kike.orientationcolor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(
                rotationMatrix, sensorEvent.values);

        float[] remappedRotationMatrix = new float[16];
        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationMatrix);

        // Convert to orientations
        float[] orientations = new float[3];
        SensorManager.getOrientation(remappedRotationMatrix, orientations);

        int eix;
        eix = 0;
        orientations[eix] = (float)(Math.toDegrees(orientations[eix]) + 180f);//360
        orientations[eix] /= (float)(360.0/255.0);
        int intR = (int) (orientations[eix] > 255.0 ? 255.0f : orientations[eix] < 0.0 ? 0.0f : orientations[eix]);

        eix = 1;
        orientations[eix] = (float)(Math.toDegrees(orientations[eix]) + 90f);//180
        orientations[eix] /= (float)(255.0/360.0);
        int intG = (int) (orientations[eix] > 255.0 ? 255.0f : orientations[eix] < 0.0 ? 0.0f : orientations[eix]);

        eix = 2;
        orientations[eix] = (float)(Math.toDegrees(orientations[eix]) + 180f);//360
        orientations[eix] /= (float)(360.0/255.0);
        int intB = (int) (orientations[eix] > 255.0 ? 255.0f : orientations[eix] < 0.0 ? 0.0f : orientations[eix]);

        //eix = 0;
        //Log.i("kike", "x:" + orientations[eix] + " y:" + orientations[eix+1] + " z:" + orientations[eix+2]);
        //Log.i("kike", "r:" + intR + " g:" + intG + " b:" + intB);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(intR, intG, intB));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
