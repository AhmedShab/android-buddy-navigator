package jemboy.navitwo.Utility;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class ResponseReader {
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
