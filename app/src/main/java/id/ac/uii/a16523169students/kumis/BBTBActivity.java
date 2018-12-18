package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static java.lang.Math.abs;

public class BBTBActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "CekSehat";
    public static final String PREF_IDEAL = "ideal";
    private EditText tb_user, bb_user;
    private int BB, TB;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbtb);

        tb_user = (EditText) findViewById(R.id.tinggi_pengguna);
        bb_user = (EditText) findViewById(R.id.berat_pengguna);
    }

    public void HasilIdeal(){
        BB = Integer.parseInt(bb_user.getText().toString());
        TB = Integer.parseInt(tb_user.getText().toString());


        int hasil = TB - 110;
        int berat = hasil - BB;

        if (hasil == BB){
            res = "Note : \n\nKondisi tubuh anda Sudah IDEAL.";
        }
        else if (hasil > BB){
            res = "Note : \n\nKondisi tubuh anda Kurang Ideal, Perbaiki Gizi anda dengan menaikkan berat badan sebesar "+abs(berat)+" Kg";
        }
        else if (hasil < BB){
            res = "Note : \n\nKondisi tubuh anda OBESITAS, Kurangi berat badan anda dengan menurunkan berat badan sebesar "+abs(berat)+" Kg";
        }

        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_IDEAL, res)
                .apply();
    }

    public void submitData(View view) {
        HasilIdeal();
        Intent intent = new Intent(this, HasilActivity.class);
        startActivity(intent);
    }
}
