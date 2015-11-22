package jemboy.navitwo.Network;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import jemboy.navitwo.Utility.Constants;

public class UploadCoordinatesTask extends AsyncTask<Float, Void, Void> {
    private String localID;

    public UploadCoordinatesTask(String localID) {
        this.localID = localID;
    }

    protected Void doInBackground(Float... params) {
        try {
            URL url = new URL(Constants.SERVER);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String requestID = "request=uploadCoord&localID=" + localID +
                    "&latitude=" + params[0] + "&longitude=" + params[1];
            connection.setFixedLengthStreamingMode(requestID.getBytes().length);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(requestID);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}