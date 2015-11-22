package jemboy.navitwo.GPSUtility;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jemboy.navitwo.Utility.Constants;

public class DummyService extends Service {
    public static final long TIME_INTERVAL = 4 * 1000; // 4s
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    public DummyService() {}
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();
        mTimer.schedule(new DummyTask(), 0, TIME_INTERVAL);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_LONG).show();
        mTimer.cancel();
    }

    class DummyTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setAction(Constants.RECEIVER);
                    intent.putExtra("action", "GPSInfo");

                    Random random = new Random();
                    intent.putExtra("latitude", (float) random.nextInt(100));
                    intent.putExtra("longitude", (float) random.nextInt(100));
                    sendBroadcast(intent);
                }
            });
        }
    }
}
