package jemboy.navitwo.Network;

import android.app.Activity;
import android.content.Context;
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

public class UploadIDTask extends AsyncTask<String, Void, Boolean> {
    MainActivity mActivity;
    String serverIP;
    Button uploadButton;

    public UploadIDTask(MainActivity mActivity, String serverIP, Button uploadButton) {
        this.mActivity = mActivity;
        this.serverIP = serverIP;
        this.uploadButton = uploadButton;
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

            String requestID = "request=uploadID&localID=" + params[0];
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
            new DeleteIDTask(Constants.serverIP)
                    .execute(mActivity.getPastLocalID());
            mActivity.setPastLocalID(mActivity.getLocalID());
            uploadButton.setSelected(true);
            uploadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }
        else {
            mActivity.setPastLocalID("");
            uploadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        mActivity.getUploadID().setEnabled(true);
        mActivity.setNetworkBusy(false);
    }
}