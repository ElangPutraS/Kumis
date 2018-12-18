package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormGejalaActivity extends AppCompatActivity {

    private RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4;
    private RadioButton radioDehi1;
    private String dehiBerat, dehiRingan, tdkDehi, simpDehi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gejala);
   }

    public void onCheckboxClickedDehidrasi(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioDehidrasi1:
                if (checked)
                    System.out.println("Ya turun sadar");
                break;
            case R.id.radioDehidrasi2:
                if (checked)
                    System.out.println("Tidak turun sadar");
                break;
            case R.id.radioDehidrasi3:
                if (checked)
                    System.out.println("Ya mata cekung");
                break;
            case R.id.radioDehidrasi4:
                if (checked)
                    System.out.println("Mata tidak cekung");
                break;
            case R.id.radioDehidrasi5:
                if (checked)
                    System.out.println("Ya haus");
                break;
            case R.id.radioDehidrasi6:
                if (checked)
                    System.out.println("Merasa normal");
                break;
            case R.id.radioDehidrasi7:
                if (checked)
                    System.out.println("Tidak haus");
                break;
            case R.id.radioDehidrasi8:
                if (checked)
                    System.out.println("Ya cubit lama");
                break;
            case R.id.radioDehidrasi9:
                if (checked)
                    System.out.println("Cubit tidak lama");
                break;
        }
    }

    public void nextPageGejala(View view) {
        Intent intent = new Intent(this, HasilPakarActivity.class);
        startActivity(intent);
    }
}
