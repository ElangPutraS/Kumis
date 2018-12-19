package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class HasilActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "CekSehat";
    private static final String PREF_KET = "keterangan";
    private static final String PREF_PENYEBAB = "penyebab";
    private static final String PREF_OBAT = "obat";
    private static final String PREF_PANTANGAN = "pantangan";
    private static final String PREF_PERSEN = "persen";
    public static final String PREF_VITAMIN = "vitamin";
    public static final String PREF_MAKANAN = "makanan";
    public static final String PREF_IDEAL = "ideal";
    private static final String PREF_DEHI = "dehidrasi";
    private SharedPreferences pref;
    private String ket, penyebab, obat, pantangan, vitamin, makanan, ideal, dehi;
    private int persen;

    private TextView tKet, tPenyebab, tObat, tPantangan, tPersen, tVitamin, tMakanan, tIdeal, tDehi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        ket = pref.getString(PREF_KET, null);
        penyebab = pref.getString(PREF_PENYEBAB, null);
        obat = pref.getString(PREF_OBAT, null);
        pantangan = pref.getString(PREF_PANTANGAN, null);
        persen = pref.getInt(PREF_PERSEN, 0);
        vitamin = pref.getString(PREF_VITAMIN, null);
        makanan = pref.getString(PREF_MAKANAN, null);
        ideal = pref.getString(PREF_IDEAL, null);
        dehi = pref.getString(PREF_DEHI, null);

        tKet = (TextView) findViewById(R.id.teksKeteranganDiare);
        tPenyebab = (TextView) findViewById(R.id.teksPenyebabDiare);
        tObat = (TextView) findViewById(R.id.teksObatDiare);
        tPantangan = (TextView) findViewById(R.id.teksPantanganDiare);
        tPersen = (TextView) findViewById(R.id.teksPersen);
        tVitamin = (TextView) findViewById(R.id.teksVitamin);
        tMakanan = (TextView) findViewById(R.id.teksRekomendasi);
        tIdeal = (TextView) findViewById(R.id.teksBBTB);
        tDehi = (TextView) findViewById(R.id.teksDehi);

        tKet.setText("Keterangan Diare\n\n"+ket);
        tPenyebab.setText("Penyebab Diare\n\n"+penyebab);
        tObat.setText("Obat Tambahan\n\n"+obat);
        tPantangan.setText("Pantangan\n\n"+pantangan);
        tPersen.setText(persen + "%");
        tVitamin.setText(vitamin);
        tMakanan.setText(makanan);
        tIdeal.setText(ideal);
        tDehi.setText(dehi);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void balikHalaman(View view) {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
