package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HasilPakarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_pakar);
    }

    public void balikPakar(View view) {
        Intent intent = new Intent(this, HomeDokterActivity.class);
        startActivity(intent);
    }

    public void keRevisi(View view) {
        Intent intent = new Intent(this, RevisiActivity.class);
        startActivity(intent);
    }

    public void hasilRevisi(View view) {
        Intent intent = new Intent(this, HomeDokterActivity.class);
        startActivity(intent);
    }
}
