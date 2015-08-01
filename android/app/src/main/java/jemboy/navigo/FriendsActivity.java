package jemboy.navigo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FriendsActivity extends Activity {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.LINK);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    Button mAddFriendButton;
    ListView mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mSocket.connect();

        mAddFriendButton = (Button)findViewById(R.id.friend_add);
        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });

        mFriendList = (ListView)findViewById(R.id.friend_list);

        SharedPreferences sharedPreferences = getSharedPreferences("friend_list", MODE_PRIVATE);
        Set<String> sharedPrefInstance = sharedPreferences.getStringSet("friend_array", new HashSet<String>());
        Set<String> friendSet = new HashSet<String>();
        friendSet.addAll(sharedPrefInstance);
        createList(friendSet);
    }

    private void addFriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText mInput = new EditText(this);
        mInput.setHint("Username");

        mInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(mInput);

        builder.setPositiveButton("Add Friend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSocket.emit("add_friend", mInput.getText().toString());
                mSocket.on("add_friend_success", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        SharedPreferences sharedPreferences = getSharedPreferences("friend_list", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Set<String> sharedPrefInstance = sharedPreferences.getStringSet("friend_array", new HashSet<String>());
                        Set<String> friendSet = new HashSet<String>();
                        friendSet.addAll(sharedPrefInstance);
                        friendSet.add(mInput.getText().toString());
                        editor.putStringSet("friend_array", friendSet);
                        editor.commit();
                    }
                });
                mSocket.on("add_friend_failure", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "User not found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        builder.show();
    }

    private void createList(Set<String> friendSet) {
        ArrayList<String> mFriends = new ArrayList<String>();
        mFriends.addAll(friendSet);
        ArrayAdapter mFriendsAdapter = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mFriends
        );

        mFriendList.setAdapter(mFriendsAdapter);
        mFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something else
            }
        });
    }

}
