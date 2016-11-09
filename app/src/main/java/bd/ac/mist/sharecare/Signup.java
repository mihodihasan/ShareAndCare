package bd.ac.mist.sharecare;



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class Signup extends Activity implements View.OnClickListener {
    private EditText etID;
    private EditText etName;
    private EditText etLocation;
    private EditText etContact;
    private EditText etNewPassword;
    CheckBox checkBox;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etContact = (EditText) findViewById(R.id.etContact);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        checkBox= (CheckBox) findViewById(R.id.checkBox);
        Button bsignin = (Button) findViewById(R.id.bsignin);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroupOwner);
        if (bsignin != null) {
            bsignin.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bsignin) {
            if(checkBox.isChecked()){
                registerUser();
            }
            else {
                Toast.makeText(Signup.this,"You Must Agree With Our Policy",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser() {
        String ID = etID.getText().toString();
        String name = etName.getText().toString();
        String location = etLocation.getText().toString();
        String number = etContact.getText().toString();
        String password = etNewPassword.getText().toString();
        radioButton= (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        String owner=radioButton.getText().toString();


        String method="register";
        Login.BackgroundTask4UserInfo backgroundTask=new Login().new BackgroundTask4UserInfo(this);
        backgroundTask.execute(method,ID,name,location,number,password,owner);
        finish();

    }
}