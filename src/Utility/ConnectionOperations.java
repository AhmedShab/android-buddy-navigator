package jemboy.navitwo.Utility;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionOperations {
    public static HttpURLConnection startConnection() {
        HttpURLConnection connection;
        try {
            URL url = new URL(Constants.SERVER);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        } catch (Exception e) {
            connection = null;
        }
        return connection;
    }

    public static void writeToServer(HttpURLConnection connection, String query) {
        try {
            connection.setFixedLengthStreamingMode(query.getBytes().length);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(query);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readResponse(HttpURLConnection connection) {
        String response;
        try {
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
        } catch (Exception e) {
            response = "Exception";
            e.printStackTrace();
        }
        return response;
    }
}







