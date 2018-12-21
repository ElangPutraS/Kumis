package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterSSOActivity extends Activity {
    private String apiPath = "https://kumisproject.000webhostapp.com/RestController.php?view=store";
    private JSONArray resultJsonArray;
    private boolean resultBool = false;
    private int success = 0;
    private ProgressDialog processDialog;
    private Toast toast;

    GoogleSignInAccount account;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_NAME = "name";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ROLE = "role";

    private EditText username, pass, cPass;
    private String mUsername, mPass, mCPass, mName, mEmail;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sso);

        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        cPass = (EditText) findViewById(R.id.cPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        account = GoogleSignIn.getLastSignedInAccount(this);
    }

    public void registerHandler(View view) {
        mUsername = username.getText().toString();
        mPass = pass.getText().toString();
        mCPass = cPass.getText().toString();
        mName = account.getDisplayName();
        mEmail = account.getEmail();

        if(!mPass.equals(mCPass)){
            Toast toast = Toast.makeText(this,"Password tidak cocok, mohon cek kembali",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
            toast.show();
        } else if (mUsername.equals("") || mPass.equals("") || mCPass.equals("")){
            Toast toast = Toast.makeText(this,"Mohon mengisi semua kolom yang ada",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
            toast.show();
        } else {
            new ServiceStubAsyncTask(this, this).execute();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            mGoogleSignInClient.signOut();
            this.getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().clear().commit();
        }
        return super.onKeyDown(keyCode, event);
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
            String api = apiPath+"&nama="+mName+"&username="+mUsername+"&email="+mEmail+"&password="+mPass;
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
                toast = Toast.makeText(mContext,"Registrasi Berhasil, mohon menunggu...",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();

                getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                        .edit()
                        .putString(PREF_NAME, mName)
                        .putString(PREF_EMAIL, mEmail)
                        .putString(PREF_USERNAME, mUsername)
                        .putString(PREF_ROLE, "1")
                        .commit();

                finish();
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            } else {
                toast = Toast.makeText(mContext,"Registrasi Gagal",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();
            }
            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }
        }

    }//end of async task
}
