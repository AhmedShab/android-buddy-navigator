package jemboy.navitwo.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

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
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
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
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
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
                    intent.putExtra(Constants.REQUEST, Constants.LCOORDINATES);

                    Random random = new Random();
                    intent.putExtra("latitude", (float) random.nextInt(100));
                    intent.putExtra("longitude", (float) random.nextInt(100));
                    sendBroadcast(intent);
                }
            });
        }
    }
}
