package vite.kike.orientationcolor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    TextView angle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        angle = findViewById(R.id.angle);
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

        int eix = 0;
        orientations[eix] = (float)(Math.toDegrees(orientations[eix]) + 180f);
        orientations[eix] *= 4.25f;

        float r = 0, g = 0, b = 0;
        if (0 <= orientations[eix] && orientations[eix] <= 255) {
            r = 255;
            g = orientations[eix];
        } else if (255 < orientations[eix] && orientations[eix] <= 510) {
            r = 510 - orientations[eix];
            g = 255;
        } else if (510 < orientations[eix] && orientations[eix] <= 765) {
            g = 255;
            b = orientations[eix] - 510;
        } else if (765 < orientations[eix] && orientations[eix] <= 1020) {
            g = 1020 - orientations[eix];
            b = 255;
        } else if (1020 < orientations[eix] && orientations[eix] <= 1275) {
            r = orientations[eix] - 1020;
            b = 255;
        } else if (1275 < orientations[eix] && orientations[eix] <= 1530) {
            r = 255;
            b = 1530 - orientations[eix];
        }
        int intR = (int) r;
        int intG = (int) g;
        int intB = (int) b;

        //Log.i("kike", "" + intR + " " + intG + " " + intB);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(intR, intG, intB));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
