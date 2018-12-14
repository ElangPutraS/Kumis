package id.ac.uii.a16523169students.kumis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeDokterActivity extends AppCompatActivity {
    public static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ROLE = "role";
    private static final String PREF_SENDER = "sender";
    private ListView listOfMessages;
    private String mUsername;

    // declare class variables
    private ArrayList<ChatMessage> m_parts = new ArrayList<ChatMessage>();
    private Runnable viewParts;
    private ListMessageAdapter m_adapter;
    private TextView emptylist;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chats");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dokter);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String username = pref.getString(PREF_USERNAME, null);
        listOfMessages = (ListView) findViewById(R.id.list_of_chats);
        mUsername = username;

        emptylist = (TextView) findViewById(R.id.emptylist);
        emptylist.setVisibility(View.INVISIBLE);

        m_adapter = new ListMessageAdapter(HomeDokterActivity.this, R.layout.messagelist, m_parts);

        // display the list.
        listOfMessages.setAdapter(m_adapter);
        // here we are defining our runnable thread.
        viewParts = new Runnable(){
            public void run(){
                handler.sendEmptyMessage(0);
            }
        };

        // here we call the thread we just defined - it is sent to the handler below.
        Thread thread =  new Thread(null, viewParts, "MagentoBackground");
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            // create some objects
            // here is where you could also request data from a server
            // and then create objects from that data.
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            ChatMessage cm = text(dataSnapshot, ds.getKey());
                            String mText = cm.getMessageText();
                            Long mTime = cm.getMessageTime();

                            m_parts.add(new ChatMessage(mText, ds.getKey(), mTime));
                            System.out.println("COBAAA "+ mText + " | " + mTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    m_adapter = new ListMessageAdapter(HomeDokterActivity.this, R.layout.messagelist, m_parts);
                    if (m_adapter == null)
                        emptylist.setVisibility(View.VISIBLE);
                    // display the list.
                    listOfMessages.setAdapter(m_adapter);
                }

                public ChatMessage text (DataSnapshot ds, String child){
                    ChatMessage newPost = null;
                    for (DataSnapshot dos: ds.child(child).getChildren()) {
                        newPost = dos.getValue(ChatMessage.class);
                    }
                    return newPost;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            ref.addListenerForSingleValueEvent(eventListener);

            listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView sender = (TextView) view.findViewById(R.id.mUser);

                    getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putString(PREF_SENDER, sender.getText().toString())
                            .commit();

                    Intent intent = new Intent(HomeDokterActivity.this, KonsultasiActivity.class);
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }
}
