package jemboy.navigo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

    Dialog mDialog;
    Button dialogAddFriend;
    Button dialogCancel;
    Button mAddFriendButton;
    ListView mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        mSocket.connect();

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_friends);

        dialogAddFriend = (Button)mDialog.findViewById(R.id.add_friend);
        dialogCancel = (Button)mDialog.findViewById(R.id.cancel);

        mAddFriendButton = (Button)findViewById(R.id.friend_add);
        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });

        mFriendList = (ListView)findViewById(R.id.friend_list);
        setMFriendList();
    }

    private void addFriend() {
        dialogAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add shit
                mDialog.dismiss();
            }
        });
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void setMFriendList() {
        SharedPreferences sharedPreferences = getSharedPreferences("friend_list", MODE_PRIVATE);
        Set<String> sharedPrefInstance = sharedPreferences.getStringSet("friend_array", new HashSet<String>());
        Set<String> friendSet = new HashSet<String>();
        friendSet.addAll(sharedPrefInstance);
        createList(friendSet);
    }

    private void createList(Set<String> friendSet) {
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
                // Do something else
            }
        });
    }
}
