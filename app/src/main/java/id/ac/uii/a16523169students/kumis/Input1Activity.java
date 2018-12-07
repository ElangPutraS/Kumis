package id.ac.uii.a16523169students.kumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.abs;

public class Input1Activity extends AppCompatActivity {

    EditText berat_badan, tinggi_badan;
    Button submit;
    TextView tv_tubuh_ideal;
    int BB, TB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input1);

        berat_badan = (EditText) findViewById(R.id.berat_badan);
        tinggi_badan = (EditText) findViewById(R.id.tinggi_badan);
        tv_tubuh_ideal = (TextView) findViewById(R.id.tv_tubuh_ideal);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HasilIdeal();
            }
        });
    }
    public void HasilIdeal(){
        BB = Integer.parseInt(berat_badan.getText().toString());
        TB = Integer.parseInt(tinggi_badan.getText().toString());


        int hasil = TB - 110;
        int berat = hasil - BB;

        if (hasil == BB){
            tv_tubuh_ideal.setText(String.valueOf("Kondisi tubuh anda Sudah IDEAL."));
        }
        else if (hasil > BB){
            tv_tubuh_ideal.setText(String.valueOf("Kondisi tubuh anda Kurang Ideal, Perbaiki Gizi anda "+abs(berat)+" Kg"));
        }
        else if (hasil < BB){
            tv_tubuh_ideal.setText(String.valueOf("Kondisi tubuh anda OBESITAS, Kurangi berat badan anda "+abs(berat)+" Kg"));
        }
    }
}
