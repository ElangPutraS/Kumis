package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends Activity {
    EditText name,username,email,password,cPassword;
    private String apiPath = "https://kumisproject.000webhostapp.com/RestController.php?view=store";
    private ProgressDialog processDialog;
    private JSONArray resultJsonArray;
    private boolean resultBool;
    private int success = 0;
    String response = "";
    HashMap<String, String> postDataParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.nama);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pass);
        cPassword = (EditText) findViewById(R.id.cPass);
    }

    public void onLogin(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void RegisterUser(View view) throws JSONException {
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
            processDialog.setMessage("Please  Wait ...");
            processDialog.setCancelable(false);
            processDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String mName = name.getText().toString();
            String mUsername = username.getText().toString();
            String mEmail = email.getText().toString();
            String mPass = password.getText().toString();
            String mCPass = cPassword.getText().toString();

            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");

            HttpConnectionService service = new HttpConnectionService();
            String api = apiPath+"&nama="+mName+"&username="+mUsername+"&email="+mEmail+"&password="+mPass;
            response = service.sendRequest(api, postDataParams);
            try {
                success = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                resultBool = resultJsonObject.getJSONObject("output").getBoolean("acknowledge");
                System.out.println("INIIII : "+ resultBool);
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
                Toast.makeText(mContext,"Registrasi Berhasil",Toast.LENGTH_LONG);
            } else {
                Toast.makeText(mContext,"Registrasi Gagal",Toast.LENGTH_LONG);
            }
        }

    }//end of async task
}
