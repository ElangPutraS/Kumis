package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BBTBActivity extends AppCompatActivity {

    private EditText tb_user, bb_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbtb);

        tb_user = (EditText) findViewById(R.id.tinggi_pengguna);
        bb_user = (EditText) findViewById(R.id.berat_badan);
    }

    public void submitData(View view) {
        Intent intent = new Intent(this, HasilActivity.class);
        startActivity(intent);
    }
}
