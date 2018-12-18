package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class AlergiActivity extends AppCompatActivity {
    private String alergi;
    public static final String PREFS_NAME = "CekSehat";
    public static final String PREF_MAKANAN = "makanan";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alergi);

        alergi = "Baiknya Anda Mengonsumsi : \n\n";
    }

    public void onCheckboxClickedAlergi(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.alergi1:
                if (checked){
                    alergi += "Tomat, Wortel, Bayam, Pepaya";
                } else {
                    alergi += "Susu, Keju, dan Mentega ";
                }
                break;
            case R.id.alergi2:
                if (checked) {
                    alergi+= "Semangka, Hati, Tiram ";
                } else {
                    alergi += "Biji - Bijian ";
                }
                break;
            case R.id.alergi3:
                if (checked) {
                    alergi+= "Susu, Keju, Sayuran Hijau, Daging";
                }else {
                    alergi += "Telur";
                }
                break;
            case R.id.alergi4:
                if (checked) {
                    alergi+= "Ikan, roti, Sereal";
                }else{
                    alergi+= "Daging dan Unggas ";
                }
                break;
            case R.id.alergi5:
                if (checked) {
                    alergi+= "Kentang, Sayuran Hijau, dan Buah Berwarna Ungu";
                }else{
                    alergi+="Ikan";
                }
                break;
            case R.id.alergi6:
                if (checked) {
                    alergi+= "Hati dan daging";
                }else{
                    alergi+="Sayur - Sayuran";
                }
                break;
            case R.id.alergi7:
                if (checked) {
                    alergi+= "Oncom dan Kacang Tanah";
                }else{
                    alergi +="Produk Fermentasi(Kecap, Tauco, Tempe, dan Tape)";
                }
                break;
            case R.id.alergi8:
                if (checked) {
                    alergi+= "Brokoli, Kobis, Melon, Daun Pepaya, Singkong";
                }else{
                    alergi+="Jeruk, Stroberry, dan Tomat";
                }
                break;
            case R.id.alergi9:
                if (checked) {
                    alergi+= "Telur, Jamur, Mentega";
                }else{
                    alergi+="Ikan Lele, Salmon, Sarden, Tuna, Hati Sapi";
                }
                break;
            case R.id.alergi10:
                if (checked) {
                    alergi+= "Asparagus, Telur, Susu, dan Bayam";
                }else{
                    alergi+="Alpukat, Kacan Almond, Gandum, Kecambah";
                }
                break;
            case R.id.alergi11:
                if (checked) {
                    alergi+= "Kobis, Buah buahan, Susu, Teh hijau ";
                }else{
                    alergi+="Hati dan Yogurth";
                }
                break;
        }
    }

    public void keRiwayat(View view) {
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_MAKANAN, alergi)
                .apply();

        Intent intent = new Intent(this, BBTBActivity.class);
        startActivity(intent);

        System.out.println(alergi);
        alergi = "Baiknya Anda Mengonsumsi : \n\n";
    }
}
