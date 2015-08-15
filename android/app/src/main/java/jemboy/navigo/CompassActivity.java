package jemboy.navigo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;

public class CompassActivity extends Activity {
    String current, target;
    private GPSReceiver gpsReceiver;
    private ImageView mArrowView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mArrowView = (ImageView)findViewById(R.id.arrow);

        current = getIntent().getStringExtra("current");
        target = getIntent().getStringExtra("target");

        gpsReceiver = new GPSReceiver() {
            @Override
            protected void rotateImage() {
                mArrowView.startAnimation(getRotateAnimation());
            }
        };

        this.registerReceiver(gpsReceiver, new IntentFilter("jemboy.navigo.location"));
        Intent GPSIntent = new Intent(this, GPSTracker.class);
        startService(GPSIntent);
    }

    @Override
    public void onResume() {
        this.registerReceiver(gpsReceiver, new IntentFilter("jemboy.navigo.location"));
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