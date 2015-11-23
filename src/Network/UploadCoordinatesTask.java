package jemboy.navitwo.Network;

import android.os.AsyncTask;
import java.net.HttpURLConnection;

import jemboy.navitwo.Main.MainActivity;
import jemboy.navitwo.Main.OnTaskCompleted;
import jemboy.navitwo.Utility.ConnectionOperations;

public class UploadCoordinatesTask extends AsyncTask<Float, Void, String> {
    OnTaskCompleted taskCompleted;
    private String localID;

    public UploadCoordinatesTask(MainActivity mainActivity, String localID) {
        this.taskCompleted = mainActivity;
        this.localID = localID;
    }

    protected String doInBackground(Float... params) {
        String response;
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection();
            String query = "request=uploadCoord&localID=" + localID +
                    "&latitude=" + params[0] + "&longitude=" + params[1];
            ConnectionOperations.writeToServer(connection, query);
            response = ConnectionOperations.readResponse(connection);




        } catch (Exception e) {
            response = "Exception";
            e.printStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        taskCompleted.onUploadCoordinatesCompleted(response);
    }
}