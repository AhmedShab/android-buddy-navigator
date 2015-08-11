package jemboy.navigo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends Activity {
    private Button mRegisterButton;
    private TextView mUsernameView;
    private TextView mPasswordView;
    private TextView mPasswordRepeatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameView = (TextView) findViewById(R.id.register_username);
        mPasswordView = (TextView) findViewById(R.id.register_password);
        mPasswordRepeatView = (TextView) findViewById(R.id.register_password_repeat);

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputCheck() == true) {
                    new RegisterOperation(Constants.LINK,
                            mUsernameView.getText().toString()).execute();
                }
            }
        });
    }

    private class RegisterOperation extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        String myurl, username;
        RegisterOperation(String myurl, String username) {
            this.myurl = myurl;
            this.username = username;
            progressDialog.setMessage("Registering...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            URL url;
            HttpURLConnection conn = null;
            InputStream inputStream;
            DataOutputStream dataOutputStream;
            try {
                url = new URL(this.myurl);
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = "username=" + this.username;
                conn.setFixedLengthStreamingMode(data.getBytes().length);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                writer.write(data);
                writer.flush();
                writer.close();
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
                Intent intent = new Intent(RegisterActivity.this, LaunchActivity.class);
                // Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                mUsernameView.setError(getString(R.string.error_taken_username));
            }
        }
    }

    private boolean inputCheck() {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRepeat = mPasswordRepeatView.getText().toString();
        if (username.length() == 0) {
            mUsernameView.setError(getString(R.string.error_field_required));
            return false;
        }
        if (username.length()  < 6) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            return false;
        }
        if (password.length() < 6) {
            mPasswordView.setError(getString(R.string.error_field_too_short));
            return false;
        }
        if (password.equals(passwordRepeat) == false) {
            mPasswordRepeatView.setError(getString(R.string.error_matching_password));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }
}
