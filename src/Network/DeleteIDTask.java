package jemboy.navitwo.Network;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteIDTask extends AsyncTask<String, Void, Void> {
    String serverIP;

    public DeleteIDTask(String serverIP) {
        this.serverIP = serverIP;
    }

    protected Void doInBackground(String... params) {
        try {
            URL url = new URL(serverIP);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String requestID = "request=deleteID&targetID=" + params[0];
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