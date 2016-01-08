package jemboy.buddynavigator.Network;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import jemboy.buddynavigator.Main.MainActivity;
import jemboy.buddynavigator.Main.OnTaskCompleted;
import jemboy.buddynavigator.Utility.ConnectionOperations;
import jemboy.buddynavigator.Utility.Constants;

public class DownloadCoordinatesTask extends AsyncTask<String, Void, String[]> {
    private OnTaskCompleted taskCompleted;
    private String requestURL;

    public DownloadCoordinatesTask(MainActivity mainActivity, String requestURL) {
        this.taskCompleted = mainActivity;
        this.requestURL = requestURL;

    }

    protected String[] doInBackground(String... params) {
        String responseArr[] = {null, null, null};
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection(requestURL);
            String query = "username=" + params[0];
            ConnectionOperations.sendRequest(connection, query);
            responseArr = ConnectionOperations.readMultipleResponse(connection);

        } catch (Exception e) {
            responseArr[0] = Constants.EXCEPTION;
            e.printStackTrace();
        }
        return responseArr;

    }

    protected void onPostExecute(String[] responseArr) {
        String response = responseArr[0], latitude = responseArr[1], longitude = responseArr[2];
        taskCompleted.onDownloadCoordinatesCompleted(response, latitude, longitude);
    }
}
