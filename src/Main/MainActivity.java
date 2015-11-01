package jemboy.navitwo.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;
import jemboy.navitwo.GPSUtility.GPSReceiver;
import jemboy.navitwo.GPSUtility.GPSTracker;
import jemboy.navitwo.Network.NetworkTask;
import jemboy.navitwo.R;
import jemboy.navitwo.Sensors.CompassTracker;
import jemboy.navitwo.Utility.Ultimate;

public class MainActivity extends Activity {
    Intent gpsIntent, compassIntent;
    GPSReceiver gpsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ultimate ultimate = new Ultimate((ImageView)findViewById(R.id.imageView));

        new NetworkTask(ultimate).execute(40.891381f, 29.380003f);

        /*
        gpsIntent = new Intent(this, GPSTracker.class);
        compassIntent = new Intent(this, CompassTracker.class);
        gpsReceiver = new GPSReceiver(ultimate);
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        registerReceiver(gpsReceiver, new IntentFilter("jemboy.navitwo.location"));
        startService(gpsIntent);
        startService(compassIntent);
        */
    }

    @Override
    public void onPause() {
        super.onPause();
        /*
        unregisterReceiver(gpsReceiver);
        stopService(gpsIntent);
        stopService(compassIntent);
        */
    }

    @Override
    public void onStop() {
        super.onStop();
        /*
        unregisterReceiver(gpsReceiver);
        stopService(gpsIntent);
        stopService(compassIntent);
        */
    }
}
