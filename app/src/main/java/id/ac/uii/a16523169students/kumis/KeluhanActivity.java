package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class KeluhanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keluhan);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        /*boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.keluhan1:
                if (checked)
                // Put some meat on the sandwich
            else
                // Remove the meat
                break;
            case R.id.keluhan2:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            case R.id.keluhan3:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            case R.id.keluhan4:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            case R.id.keluhan5:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            case R.id.keluhan6:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            case R.id.keluhan7:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            case R.id.keluhan8:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
        }*/
    }


    public void nextPage(View view) {
        Intent intent = new Intent (this, RiwayatPenyakitActivity.class);
        startActivity(intent);
    }
}
