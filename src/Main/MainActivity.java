package jemboy.navitwo.Main;

import android.app.Activity;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import jemboy.navitwo.GPSUtility.GPSReceiver;
import jemboy.navitwo.Network.DownloadIDTask;
import jemboy.navitwo.Network.UploadIDTask;
import jemboy.navitwo.R;
import jemboy.navitwo.Utility.Constants;

public class MainActivity extends Activity {
    private Button uploadButton, downloadButton;
    private EditText localID, remoteID;
    private Intent gpsIntent, compassIntent;
    private GPSReceiver gpsReceiver;
    private String pastID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = (Button)findViewById(R.id.upload_button);
        downloadButton = (Button)findViewById(R.id.download_button);

        localID = (EditText)findViewById(R.id.local_identification);
        remoteID = (EditText)findViewById(R.id.remote_identification);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newID = localID.getText().toString();
                Log.d("Tag: ", newID + " " + pastID);
                if (pastID.equals(newID) == false)
                    new UploadIDTask(MainActivity.this, Constants.serverIP, uploadButton)
                            .execute(localID.getText().toString(), pastID);
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadIDTask(Constants.serverIP, downloadButton)
                        .execute(remoteID.getText().toString());
            }
        });

        /*
        Ultimate ultimate = new Ultimate((ImageView)findViewById(R.id.imageView));
        gpsIntent = new Intent(this, GPSTracker.class);
        compassIntent = new Intent(this, CompassTracker.class);
        gpsReceiver = new GPSReceiver(ultimate);
        */
    }

    public void setPastID() {
        pastID = localID.getText().toString();
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
