package jemboy.navigo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FriendsActivity extends Activity {
    String username;
    Dialog mDialog;
    EditText dialogInput;
    Button dialogAddFriend;
    Button dialogCancel;
    Button mAddFriendButton;
    ListView mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        username = getIntent().getStringExtra("current");

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_friends);

        dialogInput = (EditText)mDialog.findViewById(R.id.input);
        dialogAddFriend = (Button)mDialog.findViewById(R.id.add_friend);
        dialogCancel = (Button)mDialog.findViewById(R.id.cancel);

        mAddFriendButton = (Button)findViewById(R.id.friend_add);
        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FriendOperation(Constants.LINK,
                        dialogInput.getText().toString()).execute();
            }
        });
        mFriendList = (ListView)findViewById(R.id.friend_list);

        SharedPreferences sharedPreferences = getSharedPreferences("friend_list", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> sharedPrefInstance = sharedPreferences.getStringSet("friend_array", new HashSet<String>());
        Set<String> friendSet = new HashSet<String>();
        friendSet.addAll(sharedPrefInstance);
        createFriendList(friendSet);
    }

    private class FriendOperation extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(FriendsActivity.this);
        String link, username;
        FriendOperation(String link, String username) {
            this.link = link;
            this.username = username;
            progressDialog.setMessage("Adding...");
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

                String data = "request=friend&username=" + this.username;
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
                SharedPreferences sharedPreferences = getSharedPreferences("friend_list", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Set<String> sharedPrefInstance = sharedPreferences.getStringSet("friend_array", new HashSet<String>());
                Set<String> friendSet = new HashSet<String>();
                friendSet.addAll(sharedPrefInstance);
                friendSet.add(this.username);
                editor.putStringSet("friend_array", friendSet);
                editor.commit();
                createFriendList(friendSet);
            } else {
                Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createFriendList(Set<String> friendSet) {
        ArrayList<String> mFriends = new ArrayList<String>();
        mFriends.addAll(friendSet);
        final ArrayAdapter mFriendsAdapter = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mFriends
        );
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFriendList.setAdapter(mFriendsAdapter);
            }
        });
        mFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendsActivity.this, CompassActivity.class);
                intent.putExtra("current", username);
                intent.putExtra("target", mFriendList.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
    }
}
