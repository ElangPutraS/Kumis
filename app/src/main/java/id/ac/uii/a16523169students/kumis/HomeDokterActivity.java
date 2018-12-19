package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.ListActivity;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeDokterActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {
    private String apiPath = "https://kumisproject.000webhostapp.com/RestController.php?view=update";
    private JSONArray resultJsonArray;
    private boolean resultBool = false;
    private int success = 0;
    private ProgressDialog processDialog;
    private Toast toast;

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

    //CBR
    private String apiPathCBR = "https://kumisproject.000webhostapp.com/RestController.php?view=tabelCBR";
    private JSONArray resultJsonArrayCBR;
    private boolean resultBoolCBR = false;
    private int successCBR = 0;
    private ArrayList<Cases> m_partsCBR = new ArrayList<Cases>();
    private JSONObject resultJsonObjectCBR;

    private Runnable viewPartsCBR;
    private ListCaseAdapter m_adapterCBR;
    private TextView emptlist;
    private ListView listOfCases;
    private SwipeRefreshLayout pullToRefreshLayoutCBR;

    // declare class variables
    private ArrayList<ChatMessage> m_parts = new ArrayList<ChatMessage>();
    private Runnable viewParts;
    private ListMessageAdapter m_adapter;
    private TextView emptylist;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chats");
    private SwipeRefreshLayout pullToRefreshLayout;

    private String mUser, eNama, eEmail, ePass, eCPass;
    private String dehiBerat, dehiRingan, tdkDehi, simpDehi;
    private EditText editUser, editNama, editEmail, editPass, confPass;
    //textview
    private TextView tNama, tEmail;
    //viewflipper
    private ViewFlipper vf;

    //googlesignin
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_dokter);

        editNama = (EditText) findViewById(R.id.edit_nama);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPass = (EditText) findViewById(R.id.edit_pass);
        confPass = (EditText) findViewById(R.id.conf_pass);
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

        //viewflipper
        vf = (ViewFlipper)findViewById(R.id.vf_dokter);

        vf.setDisplayedChild(0);

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

        editEmail.setText(email);
        editNama.setText(nama);
        mUser = username;

        TextDrawable edrawable = TextDrawable.builder()
                .buildRound("D", color );

        ImageView eimage = (ImageView) findViewById(R.id.avatar);
        eimage.setImageDrawable(edrawable);
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

        //Keperluan CBR

        //listview
        listOfCases = (ListView) findViewById(R.id.list_of_cases);

        //refresh layout
        pullToRefreshLayoutCBR = (SwipeRefreshLayout) findViewById(R.id.activity_list_case);
        pullToRefreshLayoutCBR.setOnRefreshListener(this);

        emptlist = (TextView) findViewById(R.id.emptylistt);
        emptlist.setVisibility(View.INVISIBLE);

        m_adapterCBR = new ListCaseAdapter(this, R.layout.caselist, m_partsCBR);
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

        new ServiceCBRAsyncTask(this, this).execute();
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

        //CBR
        listOfCases.setAdapter(null);
        new ServiceCBRAsyncTask(this, this).execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayoutCBR.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_dokter);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(vf.getDisplayedChild()!=0){
                vf.setDisplayedChild(0);
            } else{
                finish();
                super.onBackPressed();
            }
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
            vf.setDisplayedChild(2);
        } else if (id == R.id.nav_sistem) {
            vf.setDisplayedChild(1);
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
            String api = apiPath+"&nama="+eNama+"&username="+mUser+"&email="+eEmail+"&password="+ePass;
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
                toast = Toast.makeText(mContext,"Edit profile berhasil, mengalihkan ke halaman awal...",Toast.LENGTH_LONG);
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
                toast = Toast.makeText(mContext,"Edit profile gagal",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();
            }
            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }
        }

    }//end of async task

    public void onCheckboxClickedDehidrasi(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioDehidrasi1:
                if (checked)
                    System.out.println("Ya turun sadar");
                break;
            case R.id.radioDehidrasi2:
                if (checked)
                    System.out.println("Tidak turun sadar");
                break;
            case R.id.radioDehidrasi3:
                if (checked)
                    System.out.println("Ya mata cekung");
                break;
            case R.id.radioDehidrasi4:
                if (checked)
                    System.out.println("Mata tidak cekung");
                break;
            case R.id.radioDehidrasi5:
                if (checked)
                    System.out.println("Ya haus");
                break;
            case R.id.radioDehidrasi6:
                if (checked)
                    System.out.println("Merasa normal");
                break;
            case R.id.radioDehidrasi7:
                if (checked)
                    System.out.println("Tidak haus");
                break;
            case R.id.radioDehidrasi8:
                if (checked)
                    System.out.println("Ya cubit lama");
                break;
            case R.id.radioDehidrasi9:
                if (checked)
                    System.out.println("Cubit tidak lama");
                break;
        }
    }

    public void nextPageGejala(View view) {
        Intent intent = new Intent(this, HasilPakarActivity.class);
        startActivity(intent);
    }

    private Handler handlerCBR = new Handler()
    {
        public void handleMessage(Message msg)
        {
            try {
                System.out.println("IKILOHHHH : "+resultJsonArrayCBR.length() + " " + resultJsonArrayCBR.get(0));
                int bGejala = 0, idKasus = 0;
                String ket = "", penyebab = "";

                for (int i = 0 ; i < resultJsonArrayCBR.length(); i++){
                    JSONObject objTabel = resultJsonArrayCBR.getJSONObject(i);
                    bGejala = 0;
                    System.out.println("Data ke-"+i);
                    for(int j = 1; j <= 28; j++ ){
                        String tmp = "G"+j;
                        if(objTabel.getInt(tmp) > 0){
                            bGejala += 1;
                        }
                    }
                    ket = objTabel.getString("Keterangan");
                    penyebab = objTabel.getString("Penyebab");
                    idKasus = objTabel.getInt("id");

                    m_partsCBR.add(new Cases(bGejala, ket, penyebab, idKasus));
                    System.out.println("ININAHH ANJAYYYY "+bGejala+" "+ket+" "+penyebab);
                }
                m_adapterCBR = new ListCaseAdapter(HomeDokterActivity.this, R.layout.caselist, m_partsCBR);
                if (m_adapterCBR == null)
                    emptlist.setVisibility(View.VISIBLE);
                else
                    listOfCases.setAdapter(m_adapterCBR);
                // display the list.
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listOfCases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(HomeDokterActivity.this, DetailCaseActivity.class);
                    TextView idKasus = (TextView) view.findViewById(R.id.idKasus);
                    String idcase = idKasus.getText().toString();
                    System.out.println("IKIIII : "+idcase.substring(12,idcase.length()));
                    i.putExtra("ID_KASUS", Integer.parseInt(idcase.substring(12,idcase.length())));
                    startActivity(i);
                }
            });
        }
    };

    private class ServiceCBRAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String response = "";
        HashMap<String, String> postDataParams;

        public ServiceCBRAsyncTask(Context context, Activity activity) {
            mContext = context;
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");


            HttpConnectionService service = new HttpConnectionService();
            response = service.sendRequest(apiPathCBR, postDataParams);

            try {
                successCBR = 1;
                resultJsonObjectCBR = new JSONObject(response);
                resultJsonArrayCBR = resultJsonObjectCBR.getJSONArray("output");

                m_partsCBR.clear();
                // here we are defining our runnable thread.
                viewPartsCBR = new Runnable(){
                    public void run(){
                        handlerCBR.sendEmptyMessage(0);
                    }
                };

                // here we call the thread we just defined - it is sent to the handler below.
                Thread thread =  new Thread(null, viewPartsCBR, "MagentoBackground");
                thread.start();
            } catch (JSONException e) {
                successCBR = 0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

        }

    }//end of async task
}
