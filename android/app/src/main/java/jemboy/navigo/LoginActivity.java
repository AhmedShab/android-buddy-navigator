package jemboy.navigo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class LoginActivity extends Activity {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.LINK);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Button mSignInButton;
    private TextView mUsernameView;
    private TextView mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (TextView) findViewById(R.id.login_username);
        mPasswordView = (TextView) findViewById(R.id.login_password);

        mSignInButton = (Button) findViewById(R.id.login_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginOperation(Constants.LINK,
                        mUsernameView.getText().toString(), mPasswordView.getText().toString()).execute();
            }
        });
    }

    private class LoginOperation extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        String link, username, password;
        LoginOperation(String link, String username, String password) {
            this.link = link;
            this.username = username;
            this.password = password;
            progressDialog.setMessage("Logging In...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            URL url;
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            try {
                url = new URL(this.link);
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = "request=login&username=" + this.username + "&password=" + this.password;
                conn.setFixedLengthStreamingMode(data.getBytes().length);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                writer.write(data);
                writer.flush();
                writer.close();

                inputStream = conn.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                int character;
                while ((character = inputStream.read()) != -1) {
                    stringBuffer.append((char) character);
                }
                inputStream.close();
                if (stringBuffer.toString().equals("true"))
                    return true;
                else
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Tag: ", "Caught an exception: " + e.toString());
            } finally {
                conn.disconnect();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result == true) {
                Intent intent = new Intent(LoginActivity.this, FriendsActivity.class);
                intent.putExtra("current", this.username);
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                mUsernameView.setError(getString(R.string.error_invalid_username));
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }
}
