package jemboy.navitwo.Network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jemboy.navitwo.GPSUtility.GPSReceiver;
import jemboy.navitwo.Utility.Data;
import jemboy.navitwo.Utility.Ultimate;

public class NetworkTask extends AsyncTask<Float, Void, Data> {
    Ultimate ultimate;

    public NetworkTask(Ultimate ultimate) {
        this.ultimate = ultimate;
    }

    protected Data doInBackground(Float... coordinates) {
        float localX = coordinates[0];
        float localY = coordinates[1];
        float remoteX;
        float remoteY;

        try {
            URL url = new URL("http://10.50.153.136:8080");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            Log.d("Status: ", "Connected");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonData = bufferedReader.readLine();
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(jsonData);
            remoteX = Float.parseFloat(jsonObject.getString("latitude"));
            remoteY = Float.parseFloat(jsonObject.getString("longitude"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new Data(localX, localY, remoteX, remoteY);
    }

    protected void onPostExecute(Data mData) {
        if (mData != null) {
            ultimate.updateAzimuth(mData);
            ultimate.rotateImageView();
        }
    }
}
