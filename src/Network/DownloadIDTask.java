package jemboy.navitwo.Network;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import jemboy.navitwo.Main.MainActivity;
import jemboy.navitwo.Utility.Constants;

public class DownloadIDTask extends AsyncTask<String, Void, Boolean> {
    MainActivity mActivity;
    String serverIP;
    Button downloadButton;

    public DownloadIDTask(MainActivity mActivity, String serverIP, Button downloadButton) {
        this.mActivity = mActivity;
        this.serverIP = serverIP;
        this.downloadButton = downloadButton;
    }

    protected Boolean doInBackground(String... params) {
        boolean result = false;
        try {
            URL url = new URL(serverIP);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String requestID = "request=downloadID&remoteID=" + params[0];
            connection.setFixedLengthStreamingMode(requestID.getBytes().length);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(requestID);
            writer.flush();
            writer.close();

            InputStream inputStream = connection.getInputStream();
            result = (inputStream.read() == '1');
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(Boolean result) {
        if (result == true) { // Successfully taken ID
            downloadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }
        else {
            mActivity.setRemoteID("");
            downloadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        mActivity.getDownloadID().setEnabled(true);
        mActivity.setNetworkBusy(false);
    }
}