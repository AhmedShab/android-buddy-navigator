package jemboy.navigo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class RegisterActivity extends Activity {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.LINK);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Button mRegisterButton;
    private TextView mUsernameView;
    private TextView mPasswordView;
    private TextView mPasswordRepeatView;
    private View mRegisterFormView;
    private View mProgressView;

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
                    attemptRegister();
                }
            }
        });
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void attemptRegister() {
        showProgress(true);
        mSocket.connect();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", mUsernameView.getText().toString());
            jsonObject.put("password", mPasswordView.getText().toString()); // Need to hash the password here!
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("register", jsonObject);
        mSocket.on("register_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Intent intent = new Intent(RegisterActivity.this, Blank.class);
                startActivity(intent);
                mSocket.disconnect();
            }
        });
        mSocket.on("register_fail", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        mUsernameView.setError(getString(R.string.error_taken_username));
                    }
                });
                mSocket.disconnect();
            }
        });
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
            mPasswordView.setError(getString(R.string.error_invalid_password));
            return false;
        }
        if (password.equals(passwordRepeat) == false) {
            mPasswordRepeatView.setError(getString(R.string.error_matching_password));
            return false;
        }
        return true;
    }

    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
