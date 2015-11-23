package jemboy.navitwo.Network;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import jemboy.navitwo.Main.MainActivity;
import jemboy.navitwo.Main.OnTaskCompleted;
import jemboy.navitwo.Utility.ConnectionOperations;
import jemboy.navitwo.Utility.Constants;

public class DownloadIDTask extends AsyncTask<String, Void, String> {
    OnTaskCompleted taskCompleted;

    public DownloadIDTask(MainActivity mainActivity) {
        this.taskCompleted = mainActivity;
    }

    protected String doInBackground(String... params) {
        String response;
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection();
            String query = "request=downloadID&remoteID=" + params[0];
            ConnectionOperations.writeToServer(connection, query);
            response = ConnectionOperations.readResponse(connection);



        } catch (Exception e) {
            response = "Exception";
            e.printStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        taskCompleted.onDownloadIDCompleted(response);
    }
}