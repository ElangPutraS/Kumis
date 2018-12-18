package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BBTBActivity extends AppCompatActivity {

    private EditText tb_user, bb_user;
    private Button mButton;
    private String bbKosong, tbKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbtb);

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

    }
}
