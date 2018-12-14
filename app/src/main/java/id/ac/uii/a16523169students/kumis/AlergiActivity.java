package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlergiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alergi);
    }

    public void onCheckboxClickedAlergi(View view) {
    }

    public void keRiwayat(View view) {
        Intent intent = new Intent(this, RiwayatPenyakitActivity.class);
        startActivity(intent);
    }
}
