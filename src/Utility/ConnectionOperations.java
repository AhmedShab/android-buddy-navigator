package jemboy.navitwo.Utility;

import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionOperations {
    public static HttpURLConnection startConnection(String requestURL) {
        HttpURLConnection connection;
        try {
            URL url = new URL(requestURL);
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

    public static void sendRequest(HttpURLConnection connection, String query) {
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

    public static String readSingleResponse(HttpURLConnection connection) {
        String response;
        try {
            InputStream inputStream = connection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = inputStream.read()) != -1) {
                stringBuilder.append((char) character);
            }
            inputStream.close();

            String jsonString = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(jsonString);

            response = jsonObject.getString("result");

        } catch (Exception e) {
            response = Constants.EXCEPTION;
            e.printStackTrace();
        }
        return response;
    }

    public static String[] readMultipleResponse(HttpURLConnection connection) {
        String response[] = {null, null, null};
        try {
            InputStream inputStream = connection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = inputStream.read()) != -1) {
                stringBuilder.append((char) character);
            }
            inputStream.close();

            String jsonString = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(jsonString);

            response[0] = jsonObject.getString("result");

            if (jsonObject.has("latitude")) {
                response[1] = jsonObject.getString("latitude");
                response[2] = jsonObject.getString("longitude");
            }

        } catch (Exception e) {
            response[0] = Constants.EXCEPTION;
            e.printStackTrace();
        }
        return response;
    }
}







