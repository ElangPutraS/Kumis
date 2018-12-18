package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean resultBool = false;
    private int success = 0;

    private String apiPath = "https://kumisproject.000webhostapp.com/RestController.php?view=update";
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_NAME = "name";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ROLE = "role";
    private static final String PREF_PASSWORD = "password";

    public static final String PREFSS_NAME = "CekSehat";
    private static final String PREF_KET = "keterangan";
    private static final String PREF_PENYEBAB = "penyebab";
    private static final String PREF_OBAT = "obat";
    private static final String PREF_PANTANGAN = "pantangan";
    public static final String PREF_VITAMIN = "vitamin";
    public static final String PREF_MAKANAN = "makanan";
    public static final String PREF_IDEAL = "ideal";
    private SharedPreferences prefs;
    private String ket, penyebab, obat, pantangan, vitamin, makanan, ideal;

    private FirebaseListAdapter<ChatMessage> adapter;
    private static final String PREF_SENDER = "sender";
    private String mUsername, mSender, mEmail;
    private SharedPreferences pref;

    private TextView tNama, tEmail, tPass, tCPass;
    private EditText editNama, editEmail, editPass, confPass;

    //viewflipper
    private ViewFlipper vf;
    private String eNama, eEmail, ePass, eCPass;
    private ProgressDialog processDialog;
    private Toast toast;
    //googlesignin
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //ambil view lain
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View nav_header = navView.inflateHeaderView(R.layout.nav_header_home);

        //viewflipper
        vf = (ViewFlipper)findViewById(R.id.vf);

        prefs = getSharedPreferences(PREFSS_NAME,MODE_PRIVATE);
        ket = prefs.getString(PREF_KET, null);
        penyebab = prefs.getString(PREF_PENYEBAB, null);
        obat = prefs.getString(PREF_OBAT, null);
        pantangan = prefs.getString(PREF_PANTANGAN, null);
        vitamin = prefs.getString(PREF_VITAMIN, null);
        makanan = prefs.getString(PREF_MAKANAN, null);
        ideal = prefs.getString(PREF_IDEAL, null);

        vf.setDisplayedChild(0);

        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String namaUser = pref.getString(PREF_NAME, null);
        final String username = pref.getString(PREF_USERNAME, null);
        final String sender = pref.getString(PREF_SENDER, null);
        final String role = pref.getString(PREF_ROLE, null);
        final String email = pref.getString(PREF_EMAIL, null);
        mUsername = username;
        mSender = sender;
        mEmail = email;

        //set profpic
        int color = Color.rgb(85,85,85);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(mUsername.charAt(0)), color );

        ImageView image = (ImageView) nav_header.findViewById(R.id.image_dokter);
        image.setImageDrawable(drawable);

        //set nama email
        tNama = (TextView) nav_header.findViewById(R.id.nama);
        tEmail = (TextView) nav_header.findViewById(R.id.email);

        tNama.setText(namaUser);
        tEmail.setText(email);

        //get google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fabs =
                (FloatingActionButton)findViewById(R.id.fab_konsul);

        fabs.setOnClickListener(new View.OnClickListener() {
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(1);
            }
        });

        displayChatMessages();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Use PagerAdapterNav to manage page views in fragments.
        // Each page is represented by its own fragment.
        // This is another example of the adapter pattern.
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pagerNavbar);
        final PagerAdapterNav adapter = new PagerAdapterNav
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        // Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //Edit Profile
        editNama = (EditText) findViewById(R.id.edit_nama);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPass = (EditText) findViewById(R.id.edit_pass);
        confPass = (EditText) findViewById(R.id.conf_pass);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String name = pref.getString(PREF_NAME, null);
        String password = pref.getString(PREF_PASSWORD, null);

        editEmail.setText(email);
        editNama.setText(name);

        int ecolor = Color.rgb(85,85,85);
        TextDrawable edrawable = TextDrawable.builder()
                .buildRound(String.valueOf(mUsername.charAt(0)), ecolor );

        ImageView eimage = (ImageView) findViewById(R.id.avatar);
        eimage.setImageDrawable(edrawable);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (vf.getDisplayedChild()!=0) {
            vf.setDisplayedChild(0);
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
            vf.setDisplayedChild(2);
        } else if (id == R.id.nav_konsultasi) {
            vf.setDisplayedChild(1);
        } else if (id == R.id.nav_gizi) {
            vf.setDisplayedChild(3);
        }  else if (id == R.id.nav_logout) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
            this.getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().clear().commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
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

    public void editProfile(View view) {
        eNama = editNama.getText().toString();
        eEmail = editEmail.getText().toString();
        ePass = editPass.getText().toString();
        eCPass = confPass.getText().toString();

        if(!ePass.equals(eCPass)){
            Toast toast = Toast.makeText(this,"Password tidak cocok, mohon cek kembali",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
            toast.show();
        }
        else if (eNama.equals("") || eEmail.equals("") || ePass.equals("") || eCPass.equals("")){
            Toast toast = Toast.makeText(this,"Mohon melengkapi semua kolom yang ada",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
            toast.show();
        }
        else{
            new ServiceStubAsyncTask(this, this).execute();
        }

    }

    private class ServiceStubAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String response = "";
        HashMap<String, String> postDataParams;

        public ServiceStubAsyncTask(Context context, Activity activity) {
            mContext = context;
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            processDialog = new ProgressDialog(mContext);
            processDialog.setMessage("Sedang Memproses ...");
            processDialog.setCancelable(false);
            processDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");

            HttpConnectionService service = new HttpConnectionService();
            String api = apiPath+"&nama="+eNama+"&username="+mUsername+"&email="+eEmail+"&password="+ePass;
            response = service.sendRequest(api, postDataParams);
            try {
                success = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                resultBool = resultJsonObject.getJSONObject("output").getBoolean("acknowledge");
            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            if (resultBool) {
                toast = Toast.makeText(mContext,"Edit profile berhasil",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();

                getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                        .edit()
                        .putString(PREF_NAME, eNama)
                        .putString(PREF_EMAIL, eEmail)
                        .commit();
                editNama.setText(eNama);
                editEmail.setText(eEmail);
                editPass.setText("");
                confPass.setText("");

                tNama.setText(eNama);
                tEmail.setText(eEmail);

                vf.setDisplayedChild(0);
            } else {
                toast = Toast.makeText(mContext,"Edit profile gagal, pastikan anda terhubung internet",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();
            }
            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }
        }

    }//end of async task

    public void cekDiabetes(View view) {
        Intent intent = new Intent(this, FormGejalaActivity.class);
        startActivity(intent);
    }

    public String sendKet() {
        return ket;
    }
    public String sendPenyebab() {
        return penyebab;
    }
    public String sendPantangan() {
        return pantangan;
    }
    public String sendObat() {
        return obat;
    }
    public String sendVit() {
        return vitamin;
    }
    public String sendRek() {
        return makanan;
    }
    public String sendIde() {
        return ideal;
    }
}
