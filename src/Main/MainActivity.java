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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;
import jemboy.navitwo.Network.DownloadCoordinatesTask;
import jemboy.navitwo.Service.DummyService;
import jemboy.navitwo.Network.DeleteIDTask;
import jemboy.navitwo.Network.DownloadIDTask;
import jemboy.navitwo.Network.UploadCoordinatesTask;
import jemboy.navitwo.Network.UploadIDTask;
import jemboy.navitwo.R;
import jemboy.navitwo.Service.LocationService;
import jemboy.navitwo.Service.RotationService;
import jemboy.navitwo.Utility.ArrowAnimation;
import jemboy.navitwo.Utility.Constants;

public class MainActivity extends Activity implements OnTaskCompleted {
    private Button uploadButton, downloadButton;
    private EditText uploadID, downloadID;
    private Intent locationIntent, rotationIntent, dummyIntent;
    private BroadcastReceiver broadcastReceiver;
    private String localID = "", remoteID = "", pastLocalID = "", pastRemoteID = "";
    private boolean isNetworkBusy = false, isUploading = false, isDownloading = false;
    private Handler handler = new Handler();
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        final ArrowAnimation arrowAnimation = new ArrowAnimation(imageView);

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
            private float xlat = 0, xlong = 0, ylat = 0, ylong = 0, degree = 0;
            private boolean isLocked = false;
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isLocked) {
                    isLocked = true;
                    Bundle bundle = intent.getExtras();
                    String request = bundle.getString(Constants.REQUEST);
                    if (request.equals(Constants.LCOORDINATES)) {
                        xlat = bundle.getFloat("latitude");
                        xlong = bundle.getFloat("longitude");
                        new UploadCoordinatesTask(MainActivity.this, Constants.UPLOAD_COORDINATES)
                                .execute(localID, Float.toString(xlat), Float.toString(xlong));
                        arrowAnimation.updateAzimuth(xlat, xlong, ylat, ylong);
                    }
                    else if (request.equals(Constants.RCOORDINATES)) {
                        ylat = bundle.getFloat("latitude");
                        ylong = bundle.getFloat("longitude");
                        arrowAnimation.updateAzimuth(xlat, xlong, ylat, ylong);
                        arrowAnimation.rotateImageView();
                    }
                    else if (request.equals(Constants.ROTATION)) {
                        degree = bundle.getFloat("degree");
                        arrowAnimation.updateDegree(degree);
                        arrowAnimation.rotateImageView();
                    }
                    isLocked = false;
                }
            }
        };
        /*
        locationIntent = new Intent(this, LocationService.class);
        rotationIntent = new Intent(this, RotationService.class);
        */
        dummyIntent = new Intent(this, DummyService.class);
    }

    @Override
    public void onUploadIDCompleted(String result) {
        if (result.equals(Constants.SUCCESS)) {
            pastLocalID = localID;
            uploadButton.setSelected(true);
            uploadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            /*
            startService(locationIntent);
            startService(rotationIntent);
            */
            startService(dummyIntent);
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
            timer = new Timer();
            timer.schedule(new DownloadSchedule(), 0, 4 * 1000);
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
            Intent intent = new Intent();
            intent.setAction(Constants.RECEIVER);
            intent.putExtra(Constants.REQUEST, Constants.RCOORDINATES);
            intent.putExtra("latitude", Float.parseFloat(latitude));
            intent.putExtra("longitude", Float.parseFloat(longitude));
            sendBroadcast(intent);
        }
        else if (response.equals(Constants.FAILURE)) {
            cancelAndNullify();
            remoteID = pastRemoteID = "";
            downloadID.setEnabled(true);
            downloadID.setError("Client has disconnected.", null);
            downloadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

        else if (response.equals(Constants.EXCEPTION)) {
            // Do nothing
        }
    }

    public void clearUploadID(View v) {
        if (isMyServiceRunning(DummyService.class)) {
            /*
            stopService(locationIntent);
            stopService(rotationIntent);
            */
            stopService(dummyIntent);
        }
        if (uploadButton.isSelected()) {
            uploadButton.setSelected(false);
            new DeleteIDTask(MainActivity.this, Constants.DELETE_ID).execute(localID);
        }
        uploadID.setEnabled(true);
        uploadID.setText("");
        localID = pastLocalID = "";
        uploadButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
    }

    public void clearDownloadID(View v) {
        cancelAndNullify();
        downloadID.setEnabled(true);
        downloadID.setText("");
        remoteID = pastRemoteID = "";
        downloadButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
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

    private void cancelAndNullify() {
        timer.cancel();
        timer = null;
    }

    class DownloadSchedule extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    new DownloadCoordinatesTask(MainActivity.this, Constants.DOWNLOAD_COORDINATES).execute(remoteID);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.RECEIVER));
        if (isUploading) {
            isUploading = false;
            /*
            startService(locationIntent);
            startService(rotationIntent);
            */
            startService(dummyIntent);
        }

        if (isDownloading) {
            timer = new Timer();
            timer.schedule(new DownloadSchedule(), 0, 4 * 1000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isMyServiceRunning(DummyService.class)) {
            isUploading = true;
            /*
            stopService(locationIntent);
            stopService(rotationIntent);
            */
            stopService(dummyIntent);
        }
        if (timer != null) {
            isDownloading = true;
            cancelAndNullify();
        }
    }
}