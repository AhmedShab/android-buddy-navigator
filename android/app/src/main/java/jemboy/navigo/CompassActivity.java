package jemboy.navigo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;

public class CompassActivity extends Activity {
    private GPSReceiver gpsReceiver;
    private ImageView mArrowView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mArrowView = (ImageView)findViewById(R.id.arrow);

        gpsReceiver = new GPSReceiver() {
            @Override
            protected void rotateImage() {
                mArrowView.startAnimation(getRotateAnimation());
            }
        };

        this.registerReceiver(gpsReceiver, new IntentFilter("jemboy.navigo.location"));
        Intent GPSIntent = new Intent(this, GPSTracker.class);
        Intent CompassIntent = new Intent(this, CompassTracker.class);
        startService(GPSIntent);
        startService(CompassIntent);
    }

    @Override
    public void onResume() {
        this.registerReceiver(gpsReceiver, new IntentFilter("jemboy.compass.location"));
        super.onResume();
    }

    @Override
    public void onPause() {
        this.unregisterReceiver(gpsReceiver);
    }

    @Override
    protected void onStop() {
        this.unregisterReceiver(gpsReceiver);
        super.onStop();
    }
}