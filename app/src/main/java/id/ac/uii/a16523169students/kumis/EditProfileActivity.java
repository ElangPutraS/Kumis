package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private String apiPath = "https://kumisproject.000webhostapp.com/RestController.php?view=update";
    private JSONArray resultJsonArray;
    private boolean resultBool = false;
    private int success = 0;
    private ProgressDialog processDialog;
    private Toast toast;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_NAME = "name";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ROLE = "role";
    private static final String PREF_PASSWORD = "password";
    private EditText editUser, editNama, editEmail, editPass, confPass;
    private String username, name, password, tNama = "", tEmail = "", tPass = "", tCPass = "", email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editNama = (EditText) findViewById(R.id.edit_nama);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPass = (EditText) findViewById(R.id.edit_pass);
        confPass = (EditText) findViewById(R.id.conf_pass);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        username = pref.getString(PREF_USERNAME, null);
        name = pref.getString(PREF_NAME, null);
        password = pref.getString(PREF_PASSWORD, null);
        email = pref.getString(PREF_EMAIL, null);

        editEmail.setText(email);
        editNama.setText(name);

        int color = Color.rgb(85,85,85);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(username.charAt(0)), color );

        ImageView image = (ImageView) findViewById(R.id.avatar);
        image.setImageDrawable(drawable);
    }

    public void editProfile(View view) {
        tNama = editNama.getText().toString();
        tEmail = editEmail.getText().toString();
        tPass = editPass.getText().toString();
        tCPass = confPass.getText().toString();

        if(!tPass.equals(tCPass)){
            Toast toast = Toast.makeText(this,"Password tidak cocok, mohon cek kembali",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
            toast.show();
        }
        else if (tNama.equals("") || tEmail.equals("") || tPass.equals("") || tCPass.equals("")){
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
            String api = apiPath+"&nama="+tNama+"&username="+username+"&email="+tEmail+"&password="+tPass;
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
                        .putString(PREF_NAME, tNama)
                        .putString(PREF_EMAIL, tEmail)
                        .commit();

                finish();
                Intent intent = new Intent(mContext, Input1Activity.class);
                startActivity(intent);
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
}
