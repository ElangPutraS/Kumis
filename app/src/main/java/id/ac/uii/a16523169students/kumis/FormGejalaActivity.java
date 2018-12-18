package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormGejalaActivity extends AppCompatActivity {
    private String apiPath = "https://kumisproject.000webhostapp.com/RestController.php?view=tabelCBR";
    private String apiCase = "https://kumisproject.000webhostapp.com/RestController.php?view=storeCase";
    private JSONArray resultJsonArray;
    private EditText username, pass;
    private boolean resultBool = false;
    private int success = 0;
    private ProgressDialog processDialog;
    private String ket, penyebab, obat, pantangan, rekomendasi, id_solusi, simpanDehi;
    private Toast toast;
    private RadioGroup radioPanas, radioMual, radioVolume, radioFrek, radioKons, radioBau, radioWarna, radioDarah, radioLain, radioDehi1, radioDehi2, radioDehi3, radioDehi4;
    private RadioButton radioButton;
    private Button btnDisplay;
    private float batas;
    private List<String> list;

    public static final String PREFS_NAME = "CekSehat";
    private static final String PREF_KET = "keterangan";
    private static final String PREF_PENYEBAB = "penyebab";
    private static final String PREF_OBAT = "obat";
    private static final String PREF_PANTANGAN = "pantangan";
    private static final String PREF_REKOMENDASI = "rekomendasi";
    private static final String PREF_PERSEN = "persen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gejala);
        radioPanas = (RadioGroup) findViewById(R.id.radioPanas);
        radioMual = (RadioGroup) findViewById(R.id.radioMual);
        radioVolume = (RadioGroup) findViewById(R.id.radioVolume);
        radioFrek = (RadioGroup) findViewById(R.id.radioFrek);
        radioKons = (RadioGroup) findViewById(R.id.radioKons);
        radioBau = (RadioGroup) findViewById(R.id.radioBau);
        radioWarna = (RadioGroup) findViewById(R.id.radioWarna);
        radioDarah = (RadioGroup) findViewById(R.id.radioDarah);
        radioLain = (RadioGroup) findViewById(R.id.radioLain);
        radioDehi1 = (RadioGroup) findViewById(R.id.radioDehidrasi);
        radioDehi2 = (RadioGroup) findViewById(R.id.radioDehidrasiLagi);
        radioDehi3 = (RadioGroup) findViewById(R.id.radioDehidrasiManeh);
        radioDehi4 = (RadioGroup) findViewById(R.id.radioDehidrasiAgain);

        list = new ArrayList<>();

        simpanDehi = "";
    }

    public void nextPageGejala(View view) {
        new ServiceStubAsyncTask(this, this).execute();
//        Intent intent = new Intent(this, KeluhanActivity.class);
//        startActivity(intent);
    }

    public void keRevisi(View view) {
        Intent intent = new Intent(this, RevisiActivity.class);
        startActivity(intent);
    }

    private class ServiceStubAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String response = "";
        HashMap<String, String> postDataParams, postDataCase;

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

            response = service.sendRequest(apiPath, postDataParams);
            try {
                success = 1;

                //Panas
                int selectedId = radioPanas.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Rendah")) {
                        list.add("G1");
                    } else if (radioButton.getText().equals("Sedang")) {
                        list.add("G2");
                    } else if (radioButton.getText().equals("Tinggi")) {
                        list.add("G3");
                    }
                }

                //Mual
                selectedId = radioMual.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Jarang")) {
                        list.add("G4");
                    } else if (radioButton.getText().equals("Sering")) {
                        list.add("G5");
                    }
                }

                //Volume
                selectedId = radioVolume.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Sedikit")) {
                        list.add("G6");
                    } else if (radioButton.getText().equals("Sedang")) {
                        list.add("G7");
                    } else if (radioButton.getText().equals("Banyak")) {
                        list.add("G8");
                    }
                }

                //frekuensi
                selectedId = radioFrek.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("3-5 kali/hari")) {
                        list.add("G9");
                    } else if (radioButton.getText().equals("5-10 kali/hari")) {
                        list.add("G10");
                    } else if (radioButton.getText().equals(">10 kali/hari")) {
                        list.add("G11");
                    }
                }

                //konsistensi
                selectedId = radioKons.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Normal")) {
                        list.add("G12");
                    } else if (radioButton.getText().equals("Lembek")) {
                        list.add("G13");
                    } else if (radioButton.getText().equals("Cair")) {
                        list.add("G14");
                    }
                }

                //Bau
                selectedId = radioBau.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Langu")) {
                        list.add("G15");
                    } else if (radioButton.getText().equals("Busuk")) {
                        list.add("G16");
                    } else if (radioButton.getText().equals("Amis Khas")) {
                        list.add("G17");
                    }
                }

                //Warna
                selectedId = radioWarna.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Kuning-Hijau")) {
                        list.add("G18");
                    } else if (radioButton.getText().equals("Merah-Hijau")) {
                        list.add("G19");
                    } else if (radioButton.getText().equals("Kehijauan")) {
                        list.add("G20");
                    } else if (radioButton.getText().equals("Putih Pekat")) {
                        list.add("G21");
                    }
                }

                //Darah
                selectedId = radioDarah.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Ya")) {
                        list.add("G22");
                    } else if (radioButton.getText().equals("Tidak")) {
                        list.add("G23");
                    }
                }

                //lain-lain
                selectedId = radioLain.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Anoreksia")) {
                        list.add("G24");
                    } else if (radioButton.getText().equals("Kejang")) {
                        list.add("G25");
                    } else if (radioButton.getText().equals("Sepsis")) {
                        list.add("G26");
                    } else if (radioButton.getText().equals("Meteorismus")) {
                        list.add("G27");
                    } else if (radioButton.getText().equals("Infeksi")) {
                        list.add("G28");
                    }
                }

                selectedId = radioDehi1.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Ya")) {
                        simpanDehi += "Ya";
                    } else if (radioButton.getText().equals("Tidak")) {
                        simpanDehi += "Tidak";
                    }
                }

                selectedId = radioDehi2.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Ya")) {
                        simpanDehi += "Ya";
                    } else if (radioButton.getText().equals("Tidak")) {
                        simpanDehi += "Tidak";
                    }
                }

                selectedId = radioDehi3.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Ya")) {
                        simpanDehi += "Ya";
                    } else if (radioButton.getText().equals("Normal")) {
                        simpanDehi += "Normal";
                    } else if (radioButton.getText().equals("Tidak")) {
                        simpanDehi += "Tidak";
                    }
                }

                selectedId = radioDehi4.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (!(selectedId == -1)) {
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton.getText().equals("Ya")) {
                        simpanDehi += "Ya";
                    } else if (radioButton.getText().equals("Tidak")) {
                        simpanDehi += "Tidak";
                    }
                }

                System.out.println("INI DATA : ");
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i));
                }
                System.out.println();

                if (!list.isEmpty()) {
                    JSONObject resultJsonObject = new JSONObject(response);
                    resultJsonArray = resultJsonObject.getJSONArray("output");
                    System.out.println("IKILOHHHH : " + resultJsonArray.length() + " " + resultJsonArray.get(0));

                    batas = 0;
                    for (int i = 0; i < resultJsonArray.length(); i++) {
                        JSONObject objTabel = resultJsonArray.getJSONObject(i);
                        float jum = 0, jumSama = 0;
                        System.out.println("Data ke-" + i);
                        for (int j = 1; j <= 28; j++) {
                            String tmp = "G" + j;
                            System.out.print(tmp + " : " + objTabel.getInt(tmp) + " ");
                            for (int l = 0; l < list.size(); l++) {
                                if (list.get(l).equals(tmp)) {
                                    jumSama += objTabel.getInt(tmp);
                                    if (i == 0) {
                                        apiCase += "&" + tmp + "=1";
                                    }
                                }
                            }
                            jum += objTabel.getInt(tmp);
                        }
                        float persen = (jumSama / jum) * 100;

                        System.out.println("| Jumlah : " + jum + " Jumlah Sama : " + jumSama + " Persen : " + persen + "\n");
                        if (persen > batas) {
                            batas = persen;
                            ket = objTabel.getString("Keterangan");
                            penyebab = objTabel.getString("Penyebab");
                            obat = objTabel.getString("ObatTambahan");
                            pantangan = objTabel.getString("pantangan");
                            rekomendasi = objTabel.getString("rekomendasi");
                            id_solusi = objTabel.getString("id_solusi");
                        }

                    }
                    if (batas > 90 && batas < 100) {
                        apiCase += "&id_solusi=" + id_solusi;
                        System.out.println("APINYA : " + apiCase);

                        postDataCase = new HashMap<String, String>();
                        postDataCase.put("HTTP_ACCEPT", "application/json");
                        HttpConnectionService serve = new HttpConnectionService();
                        String resp = serve.sendRequest(apiCase, postDataCase);
                    }
                }
            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
                Toast toast = Toast.makeText(FormGejalaActivity.this, "Tolong cek kembali koneksi internet anda", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 425);
                toast.show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            list.clear();
            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }
            if (success == 1) {
                if (batas > 90 && !list.isEmpty()) {
                    System.out.println("Keterangan : \n" + ket);
                    System.out.println("Penyebab : \n" + penyebab);

                    if (!obat.equals("null")) {
                        System.out.println("Obat Tambahan : \n" + obat);
                    }
                    if (!pantangan.equals("null")) {
                        System.out.println("Hindari Makanan : \n" + pantangan);
                    }
                    if (!rekomendasi.equals("null")) {
                        System.out.println("Makanan Rekomendasi : \n" + rekomendasi);
                    }

                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(PREF_KET, ket)
                            .putString(PREF_OBAT, obat)
                            .putString(PREF_PANTANGAN, pantangan)
                            .putString(PREF_PENYEBAB, penyebab)
                            .putString(PREF_REKOMENDASI, rekomendasi)
                            .putInt(PREF_PERSEN, (int) batas)
                            .commit();
                } else if (!list.isEmpty())
                    System.out.println("Tidak memenuhi kecocokan minimum pada basis data, mohon konsultasikan ke dokter lewat fitur konsultasi");

                Intent intent = new Intent(FormGejalaActivity.this, KeluhanActivity.class);
                startActivity(intent);
            }
            if (simpanDehi.equals("YaYaTidakYa")) {
                System.out.println("Dehidrasi Berat, konsumsi Oralit dengan dosis 100 ml cairan oralit per kg berat badan yang diminum dalam 4-6 jam sekali.");
            } else if (simpanDehi.equals("TidakTidakYaTidak")) {
                System.out.println("Dehidrasi Ringan, konsumsi Oralit dengan dosis50 ml cairan per kg berat badan yang diminum dalam 4-6 jam sekali.");
            } else {
                System.out.println("Alhamdulillah Anda Sehat");
            }
        }

    }//end of async task

//    private void cekDehidrasi (String yayaya) {
//        int selectedId = radioDehi1.getCheckedRadioButtonId();
//        // find the radiobutton by returned id
//        if (!(selectedId == -1)) {
//            radioButton = (RadioButton) findViewById(selectedId);
//            if (radioButton.getText().equals("Ya")) {
//                simpanDehi += "Ya";
//            } else if (radioButton.getText().equals("Tidak")) {
//                simpanDehi += "Tidak";
//            }
//        }
//
//        selectedId = radioDehi2.getCheckedRadioButtonId();
//        // find the radiobutton by returned id
//        if (!(selectedId == -1)) {
//            radioButton = (RadioButton) findViewById(selectedId);
//            if (radioButton.getText().equals("Ya")) {
//                simpanDehi += "Ya";
//            } else if (radioButton.getText().equals("Tidak")) {
//                simpanDehi += "Tidak";
//            }
//        }
//
//        selectedId = radioDehi3.getCheckedRadioButtonId();
//        // find the radiobutton by returned id
//        if (!(selectedId == -1)) {
//            radioButton = (RadioButton) findViewById(selectedId);
//            if (radioButton.getText().equals("Ya")) {
//                simpanDehi += "Ya";
//            } else if (radioButton.getText().equals("Normal")) {
//                simpanDehi += "Normal";
//            } else if (radioButton.getText().equals("Tidak")) {
//                simpanDehi += "Tidak";
//            }
//        }
//
//        selectedId = radioDehi4.getCheckedRadioButtonId();
//        // find the radiobutton by returned id
//        if (!(selectedId == -1)) {
//            radioButton = (RadioButton) findViewById(selectedId);
//            if (radioButton.getText().equals("Ya")) {
//                simpanDehi += "Ya";
//            } else if (radioButton.getText().equals("Tidak")) {
//                simpanDehi += "Tidak";
//            }
//        }
//
//        if (simpanDehi.equals("YaYaTidakYa")) {
//            System.out.println("Dehidrasi Berat");
//        } else if (simpanDehi.equals("TidakTidakYaTidak")) {
//            System.out.println("Dehidrasi Ringan");
//        } else {
//            System.out.println("Alhamdulillah Anda Sehat");
//        }
//    }
}
