package jemboy.buddynavigator.Service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.util.Timer;
import java.util.TimerTask;

import jemboy.buddynavigator.Utility.Constants;

public class LocationService extends Service
        implements ConnectionCallbacks, OnConnectionFailedListener {
    public static final long TIME_INTERVAL = 4 * 1000;

    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    public LocationService() {}
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildGoogleApiClient();
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();
        mTimer.schedule(new LocationTask(), 0, TIME_INTERVAL);
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mTimer.cancel();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    class LocationTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mGoogleApiClient.isConnected()) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (mLastLocation != null) {
                            Intent intent = new Intent();
                            intent.setAction(Constants.RECEIVER);
                            intent.putExtra(Constants.REQUEST, Constants.LCOORDINATES);
                            intent.putExtra("latitude", (float)mLastLocation.getLatitude());
                            intent.putExtra("longitude", (float)mLastLocation.getLongitude());
                            sendBroadcast(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Location unavailable!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            });
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        mGoogleApiClient.connect();
    }
}
