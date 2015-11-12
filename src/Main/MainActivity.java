package jemboy.navitwo.Main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import jemboy.navitwo.GPSUtility.GPSReceiver;
import jemboy.navitwo.Network.DownloadIDTask;
import jemboy.navitwo.Network.UploadIDTask;
import jemboy.navitwo.R;
import jemboy.navitwo.Utility.Constants;

public class MainActivity extends Activity implements OnTaskCompleted {
    private Button uploadButton, downloadButton;
    private EditText uploadID;
    private EditText downloadID;
    private Intent gpsIntent, compassIntent;
    private GPSReceiver gpsReceiver;
    private String localID = "", remoteID = "", pastLocalID, pastRemoteID;
    private boolean isNetworkBusy = false;

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

        uploadID.addTextChangedListener(new LovelyTextWatcher(uploadButton));
        downloadID.addTextChangedListener(new LovelyTextWatcher(downloadButton));

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkBusy == false) {
                    isNetworkBusy = true;
                    uploadID.setEnabled(false);
                    localID = uploadID.getText().toString();
                    if (localID.equals(pastLocalID) == false && localID.equals("") == false)
                        new UploadIDTask(MainActivity.this, Constants.serverIP, uploadButton)
                                .execute(localID);
                    else {
                        uploadID.setEnabled(true);
                        isNetworkBusy = false;
                    }
                }
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkBusy == false) {
                    isNetworkBusy = true;
                    downloadID.setEnabled(false);
                    pastRemoteID = remoteID;
                    remoteID = downloadID.getText().toString();
                    if (remoteID.equals(pastLocalID) == false && remoteID.equals("") == false
                            && remoteID.equals(pastRemoteID) == false)
                        new DownloadIDTask(MainActivity.this, Constants.serverIP, downloadButton)
                                .execute(remoteID);
                    else {
                        downloadID.setEnabled(true);
                        isNetworkBusy = false;
                    }
                }
            }
        });

        /*
        Ultimate ultimate = new Ultimate((ImageView)findViewById(R.id.imageView));
        gpsIntent = new Intent(this, GPSTracker.class);
        compassIntent = new Intent(this, CompassTracker.class);
        gpsReceiver = new GPSReceiver(ultimate);
        */
    }

    @Override
    public void onUploadIDCompleted(String result) {
        if (result.equals("Success")) {
            pastLocalID = localID;
            uploadButton.setSelected(true);
            uploadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            startService(gpsIntent);
        }

        else if (result.equals("Fail")) {
            pastLocalID = "";
            uploadID.setError("Username already taken.");
            uploadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

        else if (result.equals("Exception")) {
            pastLocalID = "";
            uploadID.setError("Oops, something went wrong. Please try again.");
            uploadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        uploadID.setEnabled(true);
        isNetworkBusy = false;
    }

    @Override
    public void onDownloadIDCompleted(String result) {
        if (result.equals("Success")) {
            downloadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }

        if (result.equals("Fail")) {
            remoteID = "";
            downloadID.setError("There is no such username.");
            downloadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

        if (result.equals("Exception")) {
            remoteID = "";
            downloadID.setError("Oops, something went wrong. Please try again.");
            downloadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        downloadID.setEnabled(true);
        isNetworkBusy = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(gpsReceiver, new IntentFilter("jemboy.navitwo.location"));
        startService(gpsIntent);
        startService(compassIntent);
    }

    public void setPastLocalID(String pastLocalID) {
        this.pastLocalID = pastLocalID;
    }

    public String getPastLocalID() {
        return pastLocalID;
    }

    public void setRemoteID(String remoteID) {
        this.remoteID = remoteID;
    }

    public String getLocalID() {
        return localID;
    }

    public EditText getDownloadID() {
        return downloadID;
    }

    public EditText getUploadID() {
        return uploadID;
    }

    public void setNetworkBusy(boolean isNetworkBusy) {
        this.isNetworkBusy = isNetworkBusy;
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