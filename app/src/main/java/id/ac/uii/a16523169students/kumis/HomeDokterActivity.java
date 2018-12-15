package id.ac.uii.a16523169students.kumis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeDokterActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {
    public static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_NAME = "name";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ROLE = "role";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_SENDER = "sender";
    private ListView listOfMessages;
    private String mUsername;

    // declare class variables
    private ArrayList<ChatMessage> m_parts = new ArrayList<ChatMessage>();
    private Runnable viewParts;
    private ListMessageAdapter m_adapter;
    private TextView emptylist;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chats");
    private SwipeRefreshLayout pullToRefreshLayout;

    //textview
    private TextView tNama, tEmail;

    //googlesignin
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_dokter);

        //get google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //untuk navbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dokter);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_dokter);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_dokter);
        navigationView.setNavigationItemSelectedListener(this);

        //ambil view lain
        View nav_header = navigationView.inflateHeaderView(R.layout.nav_header_home);

        //set profpic
        int color = Color.rgb(85,85,85);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("D", color );

        ImageView image = (ImageView) nav_header.findViewById(R.id.image_dokter);
        image.setImageDrawable(drawable);

        //get sharedpref
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String username = pref.getString(PREF_USERNAME, null);
        final String email = pref.getString(PREF_EMAIL, null);
        final String nama = pref.getString(PREF_NAME, null);

        //listview
        listOfMessages = (ListView) findViewById(R.id.list_of_chats);
        mUsername = username;

        //refresh layout
        pullToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_home_dokter);
        pullToRefreshLayout.setOnRefreshListener(this);

        emptylist = (TextView) findViewById(R.id.emptylist);
        emptylist.setVisibility(View.INVISIBLE);

        m_adapter = new ListMessageAdapter(HomeDokterActivity.this, R.layout.messagelist, m_parts);

        //set nama email
        tNama = (TextView) nav_header.findViewById(R.id.nama);
        tEmail = (TextView) nav_header.findViewById(R.id.email);

        tNama.setText(nama);
        tEmail.setText(email);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // display the list.
        listOfMessages.setAdapter(null);
        m_parts.clear();
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

    @Override
    public void onRefresh() {
        // display the list.
        listOfMessages.setAdapter(null);
        m_parts.clear();
        // here we are defining our runnable thread.
        viewParts = new Runnable(){
            public void run(){
                handler.sendEmptyMessage(0);
            }
        };

        // here we call the thread we just defined - it is sent to the handler below.
        Thread thread =  new Thread(null, viewParts, "MagentoBackground");
        thread.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_dokter);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_sistem) {

        }  else if (id == R.id.nav_logout) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                            Intent intent = new Intent(HomeDokterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
            this.getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().clear().commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_dokter);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
