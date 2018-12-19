package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Math.abs;

public class BBTBActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "CekSehat";
    public static final String PREF_IDEAL = "ideal";
    private EditText tb_user, bb_user;
    private int BB, TB;
    private String res;
    private Button mButton;
    private String bbKosong, tbKosong;

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

        mButton = (Button) findViewById(R.id.submit_data_diri);

        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tb_user = (EditText) findViewById(R.id.tinggi_pengguna);
                bb_user = (EditText) findViewById(R.id.berat_pengguna);

                if (tb_user.getText().toString().isEmpty()) {
                    //bbKosong = bb_user.getText().toString();
                    Toast.makeText(BBTBActivity.this, "Mohon Isi Tinggi Badan Anda", Toast.LENGTH_SHORT).show();
                }
                if (bb_user.getText().toString().isEmpty()) {
                    //tbKosong = tb_user.getText().toString();
                    Toast.makeText(BBTBActivity.this, "Mohon Isi Berat Badan Anda", Toast.LENGTH_SHORT).show();
                }
                if (tb_user.getText().toString().isEmpty() && bb_user.getText().toString().isEmpty()) {
                    Toast.makeText(BBTBActivity.this, "Mohon Isi Tinggi dan Berat Badan Anda", Toast.LENGTH_SHORT).show();
                }
                if (!("").equals(tb_user.getText().toString()) && !("").equals(bb_user.getText().toString())) {
                    Intent intent = new Intent(BBTBActivity.this, HasilActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    public void submitData(View view) {
        HasilIdeal();
        finish();
        Intent intent = new Intent(this, HasilActivity.class);
        startActivity(intent);
    }
}
