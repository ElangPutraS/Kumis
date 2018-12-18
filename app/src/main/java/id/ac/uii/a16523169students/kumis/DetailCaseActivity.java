package id.ac.uii.a16523169students.kumis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailCaseActivity extends AppCompatActivity {
    private String apiPathSolusi = "https://kumisproject.000webhostapp.com/RestController.php?view=findCBR";
    private JSONArray resultJsonArraySolusi, solutionsArray;
    private boolean resultBool = false, resBool = false;
    private int successSolusi = 0;
    private int idKasus = 0;
    private LinearLayout ll;
    private ProgressDialog processDialog;
    private  boolean edit = false;
    private EditText tKet, tPenyebab, tObat, tPantangan, tId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_case);

        edit = false;
        Bundle extras = getIntent().getExtras();
        idKasus= extras.getInt("ID_KASUS");

        tId = (EditText) findViewById(R.id.tId);
        tKet = (EditText) findViewById(R.id.tKet);
        tPenyebab = (EditText) findViewById(R.id.tPenyebab);
        tObat = (EditText) findViewById(R.id.tObat);
        tPantangan = (EditText) findViewById(R.id.tPantangan);

        tId.setEnabled(false);
        tKet.setEnabled(false);
        tPenyebab.setEnabled(false);
        tObat.setEnabled(false);
        tPantangan.setEnabled(false);

        ll = (LinearLayout)findViewById(R.id.bobot);

        new GetSolusiAttr(this, this).execute();
    }

    public void editSolusi(View view) {
        edit = true;
        tId.setEnabled(true);
        tKet.setEnabled(true);
        tPenyebab.setEnabled(true);
        tObat.setEnabled(true);
        tPantangan.setEnabled(true);
    }

    public void updateKasus(View view) {
        if (edit){
            boolean beda = true;
            for(int i = 0; i < solutionsArray.length() ; i++){
                try {
                    if (tId.getText().toString().equals(solutionsArray.getJSONObject(i).getString("id"))){
                        beda = false;
                        Toast toast = Toast.makeText(DetailCaseActivity.this,"Id Solusi sudah dipakai",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (beda){
                new SimpanKasusAsyncTask(this, this).execute();
            }
        }
        else {
            new SimpanKasusAsyncTask(this, this).execute();
        }
    }

    public void resetSolusi(View view) {
        JSONObject obj = null;
        try {
            edit = false;

            obj = resultJsonArraySolusi.getJSONObject(0);
            tId.setText(obj.getString("id_solusi"));
            tKet.setText(obj.getString("Keterangan"));
            tPenyebab.setText(obj.getString("Penyebab"));
            tObat.setText(obj.getString("ObatTambahan"));
            tPantangan.setText(obj.getString("pantangan"));

            tId.setEnabled(false);
            tKet.setEnabled(false);
            tPenyebab.setEnabled(false);
            tObat.setEnabled(false);
            tPantangan.setEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class GetSolusiAttr extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String response = "";
        HashMap<String, String> postDataParams;

        public GetSolusiAttr(Context context, Activity activity) {
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

            String api = apiPathSolusi + "&id=" + idKasus;
            System.out.println("ININAHHH : "+api);

            HttpConnectionService service = new HttpConnectionService();
            response = service.sendRequest(api, postDataParams);

            try {
                successSolusi = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                resultJsonArraySolusi = resultJsonObject.getJSONArray("output");
                resultJsonObject = resultJsonArraySolusi.getJSONObject(0);

                final JSONObject finalResultJsonObject = resultJsonObject;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        try {
                            tId.setText(finalResultJsonObject.getString("id_solusi"));
                            tKet.setText(finalResultJsonObject.getString("Keterangan"));
                            tPenyebab.setText(finalResultJsonObject.getString("Penyebab"));

                            if (finalResultJsonObject.getString("ObatTambahan").equals("null"))
                                tObat.setText("-");
                            else
                                tObat.setText(finalResultJsonObject.getString("ObatTambahan"));

                            if (finalResultJsonObject.getString("pantangan").equals("null"))
                                tPantangan.setText("-");
                            else
                                tPantangan.setText(finalResultJsonObject.getString("pantangan"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                for (int i = 1; i <= 28; i++){
                    if (resultJsonObject.getInt("G"+i) > 0){
                        final String tmp = "G"+i;
                        String namaGejala = "\n";
                        if (tmp.equals("G1"))
                            namaGejala = "Suhu Badan Normal";
                        else if (tmp.equals("G2"))
                            namaGejala = "Peningkatan Suhu Badan Sedang";
                        else if (tmp.equals("G3"))
                            namaGejala = "Peningkatan Suhu Badan Tinggi";
                        else if (tmp.equals("G4"))
                            namaGejala = "Mual Jarang";
                        else if (tmp.equals("G5"))
                            namaGejala = "Mual Sering";
                        else if (tmp.equals("G6"))
                            namaGejala = "Volume Feses Sedikit";
                        else if (tmp.equals("G7"))
                            namaGejala = "Volume Feses Sedang";
                        else if (tmp.equals("G8"))
                            namaGejala = "Volume Feses Banyak";
                        else if (tmp.equals("G9"))
                            namaGejala = "3-5 kali/hari";
                        else if (tmp.equals("G10"))
                            namaGejala = "5-10 kali/hari";
                        else if (tmp.equals("G11"))
                            namaGejala = ">10 kali/hari";
                        else if (tmp.equals("G12"))
                            namaGejala = "Konsistensi Feses Normal";
                        else if (tmp.equals("G13"))
                            namaGejala = "Konsistensi Feses Lembek";
                        else if (tmp.equals("G14"))
                            namaGejala = "Konsistensi Feses Cair";
                        else if (tmp.equals("G15"))
                            namaGejala = "Bau Feses Langu";
                        else if (tmp.equals("G16"))
                            namaGejala = "Bau Feses Busuk";
                        else if (tmp.equals("G17"))
                            namaGejala = "Bau Feses Amis Khas";
                        else if (tmp.equals("G18"))
                            namaGejala = "Warna Feses Kuning-Hijau";
                        else if (tmp.equals("G19"))
                            namaGejala = "Warna Feses Merah-Hijau";
                        else if (tmp.equals("G20"))
                            namaGejala = "Warna Feses Kehijauan";
                        else if (tmp.equals("G21"))
                            namaGejala = "Warna Feses Putih Pekat";
                        else if (tmp.equals("G22"))
                            namaGejala = "Feses Disertai Darah";
                        else if (tmp.equals("G23"))
                            namaGejala = "Feses Tidak Disertai Darah";
                        else if (tmp.equals("G24"))
                            namaGejala = "Anoreksia";
                        else if (tmp.equals("G25"))
                            namaGejala = "Kejang";
                        else if (tmp.equals("G26"))
                            namaGejala = "Sepsis";
                        else if (tmp.equals("G27"))
                            namaGejala = "Meteorismus";
                        else if (tmp.equals("G28"))
                            namaGejala = "Infeksi";
                        // add edittext

                        final int finalI = i;
                        final String finalNamaGejala = namaGejala;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // Stuff that updates the UI
                                TextView title =  new TextView(DetailCaseActivity.this);
                                EditText et = new EditText(DetailCaseActivity.this);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                title.setPadding(0,10,0,0);
                                title.setLayoutParams(p);
                                et.setLayoutParams(p);
                                title.setText("Bobot Gejala "+ finalNamaGejala);
                                et.setHint("Bobot "+tmp);
                                et.setId(finalI);
                                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                                ll.addView(title);
                                ll.addView(et);
                            }
                        });
                    }
                }
                String allSolusi = "https://kumisproject.000webhostapp.com/RestController.php?view=tabelSolusi";
                HttpConnectionService serve = new HttpConnectionService();
                String res = serve.sendRequest(allSolusi, postDataParams);

                JSONObject solutions = new JSONObject(res);
                solutionsArray = solutions.getJSONArray("output");
            } catch (JSONException e) {
                successSolusi = 0;
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

        }

    }//end of async task

    private class SimpanKasusAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String result = "", res = "";
        HashMap<String, String> postDataParams, postDataPar;

        public SimpanKasusAsyncTask(Context context, Activity activity) {
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

            if (edit){
                postDataPar = new HashMap<String, String>();
                postDataPar.put("HTTP_ACCEPT", "application/json");

                String apiStoreSolusi = "https://kumisproject.000webhostapp.com/RestController.php?view=storeSolusi";
                apiStoreSolusi = apiStoreSolusi + "&id=" + tId.getText().toString();
                apiStoreSolusi = apiStoreSolusi + "&Keterangan=" + tKet.getText().toString();
                apiStoreSolusi = apiStoreSolusi + "&Penyebab=" + tPenyebab.getText().toString();
                apiStoreSolusi = apiStoreSolusi + "&Obat=" + tObat.getText().toString();
                apiStoreSolusi = apiStoreSolusi + "&Pantangan=" + tPantangan.getText().toString();

                System.out.println("INIII : "+ apiStoreSolusi);

                HttpConnectionService service = new HttpConnectionService();
                res = service.sendRequest(apiStoreSolusi, postDataPar);
            }

            String apiUpdate = "https://kumisproject.000webhostapp.com/RestController.php?view=updateCase";

            JSONObject resultJsonObject = null;
            try {
                resultJsonObject = resultJsonArraySolusi.getJSONObject(0);
                for (int i = 1; i <= 28; i++) {
                    if (resultJsonObject.getInt("G" + i) > 0) {
                        @SuppressLint("ResourceType") EditText tmp = (EditText) findViewById(i);
                        apiUpdate = apiUpdate + "&G"+i + "=" + tmp.getText().toString();
                    }
                }
                apiUpdate = apiUpdate + "&id_solusi="+tId.getText().toString()+ "&id="+resultJsonObject.getInt("id");
                System.out.println("INIINAHHH : "+apiUpdate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpConnectionService serve = new HttpConnectionService();
            result = serve.sendRequest(apiUpdate, postDataParams);

            try {
                successSolusi = 1;
                JSONObject obj = new JSONObject(result);
                resultBool = obj.getJSONObject("output").getBoolean("acknowledge");
                JSONObject object = new JSONObject(res);
                resBool = object.getJSONObject("output").getBoolean("acknowledge");

            } catch (JSONException e) {
                successSolusi = 0;
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

            if (successSolusi==1){
                Toast toast = Toast.makeText(DetailCaseActivity.this,"Update berhasil",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(DetailCaseActivity.this,"Update gagal, mohon cek koneksi internet anda",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();
            }

        }

    }//end of async task
}
