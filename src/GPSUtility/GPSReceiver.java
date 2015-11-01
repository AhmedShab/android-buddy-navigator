package jemboy.navitwo.GPSUtility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import jemboy.navitwo.Network.NetworkTask;
import jemboy.navitwo.Utility.Ultimate;

public class GPSReceiver extends BroadcastReceiver {
    Ultimate ultimate;
    private float latitude, longitude, degree;
    private boolean isLocked = false;

    public GPSReceiver(Ultimate ultimate) {
        this.ultimate = ultimate;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isLocked) {
            isLocked = true;
            Bundle bundle = intent.getExtras();

            if (bundle.getString("action").equals("GPSInfo")) {
                latitude = bundle.getFloat("latitude");
                longitude = bundle.getFloat("longitude");
                Log.d("Tag: ", "" + latitude + " " + longitude);
                new NetworkTask(ultimate).execute(latitude, longitude);
            }
            else if (bundle.getString("action").equals("CompassInfo")) {
                degree = bundle.getFloat("degree");
                Log.d("Tag: ", "" + degree);
                ultimate.updateDegree(degree);
            }
            ultimate.rotateImageView();
            isLocked = false;
        }
    }
}
