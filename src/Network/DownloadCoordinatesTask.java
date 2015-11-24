package jemboy.navitwo.Network;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

import jemboy.navitwo.Main.MainActivity;
import jemboy.navitwo.Main.OnTaskCompleted;
import jemboy.navitwo.Utility.ConnectionOperations;

public class DownloadCoordinatesTask extends AsyncTask<String, Void, String> {
    OnTaskCompleted taskCompleted;

    public DownloadCoordinatesTask(MainActivity mainActivity) {
        this.taskCompleted = mainActivity;
    }

    protected String doInBackground(String... params) {
        String response;
        try {
            HttpURLConnection connection = ConnectionOperations.startConnection();
            String query = "request=checkRemote&remoteID=" + params[0];
            ConnectionOperations.writeToServer(connection, query);

            InputStream inputStream = connection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            int character;
            while ((character = inputStream.read()) != -1) {
                stringBuffer.append((char) character);
            }
            inputStream.close();
            response = stringBuffer.toString();
            JSONObject jsonObject = new JSONObject(response);
            response = jsonObject.getString("result");
            if (jsonObject.getString("result").equals("Success")) {
                response = "Success " + jsonObject.getString("latitude") + jsonObject.getString("longitude");
            }
        } catch (Exception e) {
            response = "Exception";
            e.printStackTrace();
        }
        return response;

    }

    protected void onPostExecute(String response) {
        String latitude = "", longitude = "";
        if (response.equals("Success")) {
            String[] temp = response.split(" ");
            latitude = temp[1];
            longitude = temp[2];
        }
        taskCompleted.onDownloadCoordinatesCompleted(response, latitude, longitude);
    }
}
