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

public class MainActivity extends Activity {
    private Button uploadButton, downloadButton;
    private EditText uploadID, downloadID;
    private Intent gpsIntent, compassIntent;
    private GPSReceiver gpsReceiver;

    private String localID = "", remoteID = "", pastLocalID, pastRemoteID;

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
                localID = uploadID.getText().toString();
                if (localID.equals(pastLocalID) == false && localID.equals("") == false) {
                    new UploadIDTask(MainActivity.this, Constants.serverIP, uploadButton)
                            .execute(localID);
                }
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pastRemoteID = remoteID;
                remoteID = downloadID.getText().toString();
                if (remoteID.equals(pastLocalID) == false && remoteID.equals("") == false
                        && remoteID.equals(pastRemoteID) == false)
                    new DownloadIDTask(MainActivity.this, Constants.serverIP, downloadButton)
                            .execute(remoteID);
            }
        });

        /*
        Ultimate ultimate = new Ultimate((ImageView)findViewById(R.id.imageView));
        gpsIntent = new Intent(this, GPSTracker.class);
        compassIntent = new Intent(this, CompassTracker.class);
        gpsReceiver = new GPSReceiver(ultimate);
        */
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