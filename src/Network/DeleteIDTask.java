package jemboy.navitwo.Network;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import jemboy.navitwo.Utility.ConnectionOperations;

public class DeleteIDTask extends AsyncTask<String, Void, Void> {
    String serverIP;

    public DeleteIDTask(String serverIP) {
        this.serverIP = serverIP;
    }

    protected Void doInBackground(String... params) {
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection();

            String query = "request=deleteID&targetID=" + params[0];

            ConnectionOperations.writeToServer(connection, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}