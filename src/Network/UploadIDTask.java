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

public class UploadIDTask extends AsyncTask<String, Void, String> {
    MainActivity mActivity;
    String serverIP;
    Button uploadButton;

    public UploadIDTask(MainActivity mActivity, String serverIP, Button uploadButton) {
        this.mActivity = mActivity;
        this.serverIP = serverIP;
        this.uploadButton = uploadButton;
    }

    protected String doInBackground(String... params) {
        String taskResult = "Fail";
        try {
            URL url = new URL(serverIP);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String requestID = "request=upload&localID=" + params[0] + "&pastID=" + params[1];
            connection.setFixedLengthStreamingMode(requestID.getBytes().length);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(requestID);
            writer.flush();
            writer.close();

            InputStream inputStream = connection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            for (int character; (character = inputStream.read()) != -1;)
                stringBuffer.append((char)character);
            inputStream.close();
            taskResult = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskResult;
    }

    protected void onPostExecute(String taskResult) {
        if (taskResult.equals("Success")) { // Successfully taken ID
            mActivity.setPastID();
            uploadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        } else if (taskResult.equals("Fail")) {
            uploadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }
}
