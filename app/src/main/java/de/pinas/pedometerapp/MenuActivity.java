package de.pinas.pedometerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    private boolean running = false;
    private SensorManager sensorManager;
    private TextView stepsValue;
    private TextView date;
    private TextView sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        stepsValue = findViewById(R.id.stepsValue);
        date = findViewById(R.id.date);
        sensors = findViewById(R.id.subTitle);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepsValue.setText("" + Globals.getInstance().getSteps() + " " + getResources().getString(R.string.steps));
        date.setText("" + Globals.getInstance().getDate());
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepsSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI);
        }

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this, "No Light Sensor!", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            if (event.sensor.getStringType() == "android.sensor.light") {
                ImageView image = findViewById(R.id.lightImage);
                float previousLight = Globals.getInstance().getLight();
                if (event.values[0] <= 1.0f && event.values[0] >= 0.0f && (previousLight > 1.0f || previousLight < 0.0f)) {
                    image.setImageDrawable(getResources().getDrawable(R.drawable.img_evening, getApplicationContext().getTheme()));
                } else if (event.values[0] <= 70.0f && event.values[0] > 1.0f && (previousLight > 70.0f || previousLight <= 1.0f)) {
                    image.setImageDrawable(getResources().getDrawable(R.drawable.img_cloudy, getApplicationContext().getTheme()));
                } else if (event.values[0] > 70.0f && previousLight <= 70.0f) {
                    image.setImageDrawable(getResources().getDrawable(R.drawable.img_sunshine, getApplicationContext().getTheme()));
                }
                Globals.getInstance().setLight(event.values[0]);
            } else if (event.sensor.getStringType() == "android.sensor.step_counter") {
                Globals.getInstance().setSteps(event.values[0]);
                stepsValue.setText("" + Globals.getInstance().getSteps() + " " + getResources().getString(R.string.steps));
            }

            Calendar kalender = Calendar.getInstance();
            SimpleDateFormat datumsformat = new SimpleDateFormat("dd.MM.yyyy");
            Globals.getInstance().setDate(datumsformat.format(kalender.getTime()));
            date.setText("" + Globals.getInstance().getDate());

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}