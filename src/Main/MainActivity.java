package jemboy.navitwo.Main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import jemboy.navitwo.Network.DownloadCoordinatesTask;
import jemboy.navitwo.Service.CompassTracker;
import jemboy.navitwo.Service.DummyService;
import jemboy.navitwo.Service.GPSTracker;
import jemboy.navitwo.Network.DeleteIDTask;
import jemboy.navitwo.Network.DownloadIDTask;
import jemboy.navitwo.Network.UploadCoordinatesTask;
import jemboy.navitwo.Network.UploadIDTask;
import jemboy.navitwo.R;
import jemboy.navitwo.Utility.Constants;

public class MainActivity extends Activity implements OnTaskCompleted {
    private Button uploadButton, downloadButton;
    private EditText uploadID;
    private EditText downloadID;
    private Intent gpsIntent, compassIntent, dummyIntent;
    private BroadcastReceiver broadcastReceiver;
    private String localID = "", remoteID = "", pastLocalID = "", pastRemoteID = "";
    private boolean isNetworkBusy = false, wereServicesRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = (Button)findViewById(R.id.upload_button);
        uploadButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        uploadButton.setSelected(false);

        downloadButton = (Button)findViewById(R.id.download_button);
        downloadButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);

        uploadID = (EditText)findViewById(R.id.local_identification);
        downloadID = (EditText)findViewById(R.id.remote_identification);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkBusy) {
                    localID = uploadID.getText().toString();
                    if (!localID.equals(pastLocalID) && !hasWhiteSpace(uploadID)
                            && !identicalID(uploadID, downloadID)) {
                        isNetworkBusy = true;
                        uploadID.setEnabled(false);
                        new UploadIDTask(MainActivity.this, Constants.UPLOAD_ID)
                                .execute(localID);
                    }
                }
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkBusy) {
                    remoteID = downloadID.getText().toString();
                    if (!remoteID.equals(pastRemoteID) && !hasWhiteSpace(downloadID)
                            && !identicalID(downloadID, uploadID)) {
                        isNetworkBusy = true;
                        downloadID.setEnabled(false);
                        new DownloadIDTask(MainActivity.this, Constants.DOWNLOAD_ID)
                                .execute(remoteID);
                    }
                }
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            private float latitude = 0, longitude = 0, degree = 0;
            private boolean isLocked = false;
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isLocked) {
                    isLocked = true;
                    Bundle bundle = intent.getExtras();
                    if (bundle.getString("action").equals("GPSInfo")) {
                        latitude = bundle.getFloat("latitude");
                        longitude = bundle.getFloat("longitude");
                        new UploadCoordinatesTask(MainActivity.this, Constants.UPLOAD_COORDINATES)
                                .execute(localID, Float.toString(latitude), Float.toString(longitude));
                    }
                    else if (bundle.getString("action").equals("CompassInfo")) {
                        degree = bundle.getFloat("degree");
                    }
                    // Rotate shite
                    isLocked = false;
                }
            }
        };

        gpsIntent = new Intent(this, GPSTracker.class);
        compassIntent = new Intent(this, CompassTracker.class);
        dummyIntent = new Intent(this, DummyService.class);
    }

    @Override
    public void onUploadIDCompleted(String result) {
        if (result.equals(Constants.SUCCESS)) {
            pastLocalID = localID;
            uploadButton.setSelected(true);
            uploadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            startServices();
        }

        else if (result.equals(Constants.FAILURE)) {
            localID = pastLocalID = "";
            uploadID.setEnabled(true);
            uploadID.setError("Username already taken.", null);
            uploadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

        else if (result.equals(Constants.EXCEPTION)) {
            localID = pastLocalID = "";
            uploadID.setEnabled(true);
            uploadID.setError("Oops, something went wrong. Please try again.", null);
            uploadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        isNetworkBusy = false;
    }

    @Override
    public void onDownloadIDCompleted(String result) {
        if (result.equals(Constants.SUCCESS)) {
            pastRemoteID = remoteID;
            downloadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            new DownloadCoordinatesTask(MainActivity.this, Constants.DOWNLOAD_COORDINATES).execute(remoteID);
        }

        else if (result.equals(Constants.FAILURE)) {
            remoteID = pastRemoteID = "";
            downloadID.setEnabled(true);
            downloadID.setError("Username cannot be found.", null);
            downloadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

        else if (result.equals(Constants.EXCEPTION)) {
            remoteID = pastRemoteID = "";
            downloadID.setEnabled(true);
            downloadID.setError("Oops, something went wrong. Please try again.", null);
            downloadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        isNetworkBusy = false;
    }

    @Override
    public void onDeleteIDCompleted(String response) {
        if (response.equals(Constants.SUCCESS)) {
            // Do nothing
        }

        else if (response.equals(Constants.FAILURE)) {
            // Do nothing
        }

        else if (response.equals(Constants.EXCEPTION)) {
            // Do nothing
        }
    }

    @Override
    public void onUploadCoordinatesCompleted(String response) {
        if (response.equals(Constants.SUCCESS)) {
            // Do nothing
        }
        if (response.equals(Constants.FAILURE)) {
            // Do nothing
        }
        if (response.equals(Constants.EXCEPTION)) {
            // Do nothing
        }
    }

    @Override
    public void onDownloadCoordinatesCompleted(String response, String latitude, String longitude) {
        if (response.equals(Constants.SUCCESS)) {
            Log.d("Tag: ", "Remote Coordinates: " + latitude + " " + longitude);
        }
        else if (response.equals(Constants.FAILURE)) {
            // Target Client has disconnected!!!
        }

        else if (response.equals(Constants.EXCEPTION)) {
            // Do nothing
        }
    }

    public void clearUploadID(View v) {
        if (uploadButton.isSelected()) {
            uploadButton.setSelected(false);
            new DeleteIDTask(MainActivity.this, Constants.DELETE_ID).execute(localID);
        }
        uploadID.setEnabled(true);
        uploadID.setText("");
        localID = pastLocalID = "";
        uploadButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        wereServicesRunning = stopServices();
    }

    public void clearDownloadID(View v) {
        downloadID.setEnabled(true);
        downloadID.setText("");
        remoteID = pastRemoteID = "";
        downloadButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
    }

    public void startServices() {
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.RECEIVER));
        /*
        startService(gpsIntent);
        startService(compassIntent);
        */
        startService(dummyIntent);
    }

    public boolean stopServices() {
        if (isMyServiceRunning(DummyService.class)) {
            unregisterReceiver(broadcastReceiver);
            /*
            stopService(gpsIntent);
            stopService(compassIntent);
            */
            stopService(dummyIntent);
            return true;
        }
        return false;
    }

    private boolean hasWhiteSpace(EditText editText) {
        String username = editText.getText().toString();
        if (username.contains(" ")) {
            editText.setError("Space is not allowed.", null);
            return true;
        }
        return false;
    }

    private boolean identicalID(EditText editText1, EditText editText2) {
        String username1 = editText1.getText().toString(), username2 = editText2.getText().toString();
        if (username1.equals(username2)) {
            editText1.setError("You have entered identical IDs.", null);
            return true;
        }
        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wereServicesRunning)
            startServices();
    }

    @Override
    public void onStop() {
        super.onStop();
        wereServicesRunning = stopServices();
    }
}