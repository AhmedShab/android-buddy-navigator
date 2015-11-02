package jemboy.navitwo.Network;

import android.os.AsyncTask;
import android.widget.Button;

public class DownloadIDTask extends AsyncTask<String, Void, String> {
    String serverIP;
    Button downloadButton;

    public DownloadIDTask(String serverIP, Button downloadButton) {
        this.serverIP = serverIP;
        this.downloadButton = downloadButton;
    }

    protected String doInBackground(String... params) {

        return "";
    }

    protected void onPostExecute(String taskResult) {

    }


}