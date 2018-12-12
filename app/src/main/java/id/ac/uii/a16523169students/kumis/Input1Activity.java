package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static java.lang.Math.abs;

public class Input1Activity extends AppCompatActivity implements View.OnClickListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    EditText berat_badan, tinggi_badan;
    Button submit;
    TextView tv_tubuh_ideal;
    int BB, TB;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input1);

        berat_badan = (EditText) findViewById(R.id.berat_badan);
        tinggi_badan = (EditText) findViewById(R.id.tinggi_badan);
        tv_tubuh_ideal = (TextView) findViewById(R.id.tv_tubuh_ideal);
        submit = (Button) findViewById(R.id.submit);

        Button signOut = findViewById(R.id.button_sign_out);
        findViewById(R.id.button_sign_out).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_out:
                signOut();
                break;
            // ...
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        Intent intent = new Intent(Input1Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
        this.getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().clear().commit();

    }
}
