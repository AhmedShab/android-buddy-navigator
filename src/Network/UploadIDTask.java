package jemboy.navitwo.Network;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import jemboy.navitwo.Main.MainActivity;
import jemboy.navitwo.Main.OnTaskCompleted;
import jemboy.navitwo.Utility.ConnectionOperations;

public class UploadIDTask extends AsyncTask<String, Void, String> {
    private OnTaskCompleted taskCompleted;

    public UploadIDTask(MainActivity mActivity) {
        this.taskCompleted = mActivity;
    }

    protected String doInBackground(String... params) {
        String result;
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection();

            String query = "request=uploadID&localID=" + params[0];

            ConnectionOperations.writeToServer(connection, query);

            result = ConnectionOperations.readResponse(connection);
        } catch (Exception e) {
            result = "Exception";
            e.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(String result) {
        taskCompleted.onUploadIDCompleted(result);
    }
}