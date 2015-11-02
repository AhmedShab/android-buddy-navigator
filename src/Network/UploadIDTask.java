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

public class UploadIDTask extends AsyncTask<String, Void, String> {
    String serverIP;
    Button uploadButton;

    public UploadIDTask(String serverIP, Button uploadButton) {
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

            String requestID = "request=upload&localID=" + params[0];
            connection.setFixedLengthStreamingMode(requestID.getBytes().length);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(requestID);
            writer.flush();
            writer.close();

            InputStream inputStream = connection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            for (int character; (character = inputStream.read()) != -1;)
                stringBuffer.append(character);
            inputStream.close();
            taskResult = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskResult;
    }

    protected void onPostExecute(String taskResult) {
        if (taskResult.equals("Fail")) { // Successfully taken ID
            uploadButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        } else {
            uploadButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }
}
