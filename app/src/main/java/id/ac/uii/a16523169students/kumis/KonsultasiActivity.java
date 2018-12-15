package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

public class KonsultasiActivity extends AppCompatActivity {
    public static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ROLE = "role";
    private static final String PREF_SENDER = "sender";
    private String mUsername, mSender;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultasi);

        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String username = pref.getString(PREF_USERNAME, null);
        final String sender = pref.getString(PREF_SENDER, null);
        final String role = pref.getString(PREF_ROLE, null);
        mUsername = username;
        mSender = sender;

        System.out.println("USERNAME : " + mUsername);

        displayChatMessages();
//        displayChatMessages();
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                if(!input.getText().toString().equals("")){
                    if (role.equals("1")){
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("chats")
                                .child(username)
                                .push()
                                .setValue(new ChatMessage(input.getText().toString(),
                                        username, "Dokter", username+"_Dokter")
                                );
                    }
                    else if (role.equals("2")){
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("chats")
                                .child(sender)
                                .push()
                                .setValue(new ChatMessage(input.getText().toString(),
                                        "Dokter", sender, sender+"_Dokter")
                                );
                    }
                }

                input.setText("");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        displayChatMessages();


    }
    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        final String role = pref.getString(PREF_ROLE, null);

        Query query = null;

        if (role.equals("1")) {
            query = FirebaseDatabase.getInstance().getReference().child("chats").child(mUsername)
                    .orderByChild("messageHistory")
                    .equalTo(mUsername+"_Dokter");
        } else if (role.equals("2")) {
            query = FirebaseDatabase.getInstance().getReference().child("chats").child(mSender)
                    .orderByChild("messageHistory")
                    .equalTo(mSender+"_Dokter");
        }

//The error said the constructor expected FirebaseListOptions - here you create them:
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.message)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                System.out.println("INI :"+model.getMessageUser());
                int color = String.valueOf(model.getMessageUser().toString().charAt(0)).equals("D") ?
                        Color.rgb(85,85,85) : Color.argb(255,50,199,135);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .width(80)  // width in px
                        .height(80) // height in px
                        .endConfig()
                        .buildRound(String.valueOf(model.getMessageUser().toString().charAt(0)), color );

                ImageView image = (ImageView) v.findViewById(R.id.pUser);
                image.setImageDrawable(drawable);

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        adapter.startListening();
        listOfMessages.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

