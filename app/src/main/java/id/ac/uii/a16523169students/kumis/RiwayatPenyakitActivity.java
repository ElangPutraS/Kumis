package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RiwayatPenyakitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_penyakit);
    }

    public void onCheckboxRiwayat(View view) {
    }

    public void nextPageRiwayat(View view) {
        Intent intent = new Intent(this, BBTBActivity.class);
        startActivity(intent);
    }
}
