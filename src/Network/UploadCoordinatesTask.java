package jemboy.navitwo.Network;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import jemboy.navitwo.Utility.ConnectionOperations;

public class UploadCoordinatesTask extends AsyncTask<Float, Void, Void> {
    private String localID;

    public UploadCoordinatesTask(String localID) {
        this.localID = localID;
    }

    protected Void doInBackground(Float... params) {
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection();

            String query = "request=uploadCoord&localID=" + localID +
                    "&latitude=" + params[0] + "&longitude=" + params[1];

            ConnectionOperations.writeToServer(connection, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}