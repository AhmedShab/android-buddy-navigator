package jemboy.navitwo.Network;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import jemboy.navitwo.Main.MainActivity;
import jemboy.navitwo.Main.OnTaskCompleted;
import jemboy.navitwo.Utility.ConnectionOperations;
import jemboy.navitwo.Utility.Constants;

public class UploadIDTask extends AsyncTask<String, Void, String> {
    private OnTaskCompleted taskCompleted;
    String requestURL;

    public UploadIDTask(MainActivity mainActivity, String requestURL) {
        this.taskCompleted = mainActivity;
        this.requestURL = requestURL;
    }

    protected String doInBackground(String... params) {
        String response;
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection(requestURL);
            String query = "username=" + params[0];
            ConnectionOperations.sendRequest(connection, query);
            response = ConnectionOperations.readSingleResponse(connection);


        } catch (Exception e) {
            response = Constants.EXCEPTION;
            e.printStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        taskCompleted.onUploadIDCompleted(response);
    }
}