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
    private RadioButton radioDehidrasiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gejala);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioDehidrasi);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioDehidrasiLagi);
        radioGroup3 = (RadioGroup) findViewById(R.id.radioDehidrasiManeh);
        radioGroup4 = (RadioGroup) findViewById(R.id.radioDehidrasiAgain);

    }

    public void nextPageGejala(View view) {
        Intent intent = new Intent(this, HasilPakarActivity.class);
        startActivity(intent);

        int selectedId = radioGroup1.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioDehidrasiBtn = (RadioButton) findViewById(selectedId);

        Toast.makeText(FormGejalaActivity.this,
                radioDehidrasiBtn.getText(), Toast.LENGTH_SHORT).show();
    }
}