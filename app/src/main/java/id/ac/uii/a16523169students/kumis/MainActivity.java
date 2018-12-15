package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_NAME = "name";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ROLE = "role";
    private static final String PREF_PASSWORD = "password";

    TextView alert;

    private String login = "app";
    private String apiPath = "https://kumisproject.000webhostapp.com/RestController.php?view=find";
    private JSONArray resultJsonArray;
    private EditText username, pass;
    private boolean resultBool = false;
    private int success = 0;
    String mName, mEmail, mUsername = "", mPass = "", mRole, api = "";
    private String email = "";
    int RC_SIGN_IN;

    HashMap<String, String> postDataParams;

    GoogleSignInAccount account = null;
    GoogleSignInClient mGoogleSignInClient;

    private ProgressDialog processDialog;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);
        alert = (TextView) findViewById(R.id.textView2);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String role = pref.getString(PREF_ROLE, null);

        if (username != null) {
            if (role.equals("1")) {
                finish();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
            else if (role.equals("2")) {
                finish();
                Intent intent = new Intent(this, HomeDokterActivity.class);
                startActivity(intent);
            }
        } else {
            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(account);
        }

    }

    private void updateUI(GoogleSignInAccount account) {
        if(account != null){
            finish();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void checkEmail(GoogleSignInAccount account) {
        if(account != null){
            login = "sso";
            email = account.getEmail().toString();
            new ServiceStubAsyncTask(this, this).execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            checkEmail(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void onRegister(View view) {
        finish();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginHandler(View view) {
        login = "app";
        new ServiceStubAsyncTask(this, this).execute();
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

            if(login.equals("app")){
                mUsername = username.getText().toString();
                mPass = pass.getText().toString();
                api = apiPath+"&username="+mUsername+"&password="+mPass;
            } else if(login.equals("sso")){
                api = apiPath+"ByEmail&email="+email;
            }

            response = service.sendRequest(api, postDataParams);
            try {
                success = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                resultBool = resultJsonObject.getJSONObject("output").getBoolean("acknowledge");

                if(resultBool){
                    mUsername = resultJsonObject.getJSONObject("output").getString("username");
                    mName = resultJsonObject.getJSONObject("output").getString("nama");
                    mEmail = resultJsonObject.getJSONObject("output").getString("email");
                    mRole = resultJsonObject.getJSONObject("output").getString("role");
                    mPass = resultJsonObject.getJSONObject("output").getString("password");

                    getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putString(PREF_NAME, mName)
                            .putString(PREF_EMAIL, mEmail)
                            .putString(PREF_USERNAME, mUsername)
                            .putString(PREF_ROLE, mRole)
                            .putString(PREF_PASSWORD, mPass)
                            .commit();
                }
            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }

            if (resultBool) {
                if (mRole.equals("1")) {
                    finish();
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    startActivity(intent);
                }
                else if (mRole.equals("2")) {
                    finish();
                    Intent intent = new Intent(mContext, HomeDokterActivity.class);
                    startActivity(intent);
                }
            } else {
                if(login.equals("sso")){
                    Intent intent = new Intent(mContext, RegisterSSOActivity.class);
                    startActivity(intent);
                } else {
                    alert.setText("Login gagal, coba cek kembali username dan password anda");
                }
            }

        }

    }//end of async task
}
