package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editUser, editNama, editEmail, editPass, confPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editUser = (EditText) findViewById(R.id.edit_username);
        editNama = (EditText) findViewById(R.id.edit_nama);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPass = (EditText) findViewById(R.id.edit_pass);
        confPass = (EditText) findViewById(R.id.conf_pass);
    }

    public void editProfile(View view) {

    }
}
