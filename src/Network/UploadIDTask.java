package jemboy.navitwo.Network;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import jemboy.navitwo.Main.MainActivity;
import jemboy.navitwo.Main.OnTaskCompleted;
import jemboy.navitwo.Utility.ConnectionOperations;

public class UploadIDTask extends AsyncTask<String, Void, String> {
    private OnTaskCompleted taskCompleted;

    public UploadIDTask(MainActivity mainActivity) {
        this.taskCompleted = mainActivity;
    }

    protected String doInBackground(String... params) {
        String response;
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection();
            String query = "request=uploadID&localID=" + params[0];
            ConnectionOperations.writeToServer(connection, query);
            response = ConnectionOperations.readResponse(connection);




        } catch (Exception e) {
            response = "Exception";
            e.printStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        taskCompleted.onUploadIDCompleted(response);
    }
}