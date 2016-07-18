package jp.hina.lightsound;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private SoundPool soundPool;
    private  int soundID;
    private float count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onResume(){
        super.onResume();
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_LIGHT);
        if(sensors.size() > 0){
            Sensor s = sensors.get(0);
            sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(getApplicationContext(), R.raw.bell, 0);

    }

    @Override
    protected void onPause(){
        super.onPause();
        soundPool.release();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int acuuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){

        switch (event.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                float light = event.values[0];
                Log.d("light",String.valueOf(light));

                if(light < 20){
                    count++;
                    if(count > 4){
                        playFromSoundPool();
                        count = 0;
                    }
                }
                break;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(sensorManager != null){
            sensorManager.unregisterListener(this);
        }
    }

    private void playFromSoundPool(){
        soundPool.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
    }

}
