package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.sql.SQLOutput;

public class KeluhanActivity extends AppCompatActivity {
    private String kurangVit;
    public static final String PREFS_NAME = "CekSehat";
    public static final String PREF_VITAMIN = "vitamin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keluhan);

        kurangVit = "Anda kekurangan Vitamin : \n\n";
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.keluhan1:
                if (checked){
                    kurangVit += "A ";
                }
                break;
            case R.id.keluhan2:
                if (checked) {
                    kurangVit += "B1 ";
                }
                break;
            case R.id.keluhan3:
                if (checked) {
                    kurangVit += "B2 ";
                }
                break;
            case R.id.keluhan4:
                if (checked) {
                    kurangVit += "B3 ";
                }
                break;
            case R.id.keluhan5:
                if (checked) {
                    kurangVit += "B6 ";
                }
                break;
            case R.id.keluhan6:
                if (checked) {
                    kurangVit += "B9 ";
                }
                break;
            case R.id.keluhan7:
                if (checked) {
                    kurangVit += "B12 ";
                }
                break;
            case R.id.keluhan8:
                if (checked) {
                    kurangVit += "C ";
                }
                break;
            case R.id.keluhan9:
                if (checked) {
                    kurangVit += "D ";
                }
                break;
            case R.id.keluhan10:
                if (checked) {
                    kurangVit += "E ";
                }
                break;
            case R.id.keluhan11:
                if (checked) {
                    kurangVit += "K ";
                }
                break;
        }
    }

    public void nextPage(View view) {
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_VITAMIN, kurangVit)
                .apply();
        Intent intent = new Intent (this, AlergiActivity.class);
        startActivity(intent);

        System.out.println(kurangVit);
        kurangVit = "Anda Kekurangan Vitamin : \n\n";
    }
}
