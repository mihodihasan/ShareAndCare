package bd.ac.mist.sharecare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends Activity implements View.OnClickListener {

    Button blogin;
    EditText etUserName, etPassword;
    TextView signin;
    String username, userpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        blogin = (Button) findViewById(R.id.blogin);
        signin = (TextView) findViewById(R.id.signin);

        blogin.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blogin:
                username = etUserName.getText().toString();
                userpass = etPassword.getText().toString();
                String method = "login";
                BackgroundTask4UserInfo bgtask = new BackgroundTask4UserInfo(this);
                bgtask.execute(method, username, userpass);
                break;
            case R.id.signin:

                startActivity(new Intent(Login.this, Signup.class));
                break;
        }
    }


    public class BackgroundTask4UserInfo extends AsyncTask<String, Void, String> {
        Context ctx;

        public BackgroundTask4UserInfo(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Registration Success.......!")) {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            } else if (result.equals("Login Succesful ...")) {
                startActivity(new Intent(Login.this, Post_Nav.class));
                Login.this.finish();
            } else if (result.equals("Unsuccessful")) {
                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            } else if (result.equals("No Man... U are Not Allowed....reg first")) {
                Toast.makeText(ctx, "Failed!", Toast.LENGTH_SHORT).show();
            } else if (result.equals("Data Not InsertedDuplicate entry 'A1' for key 'PRIMARY'")) {
                Toast.makeText(ctx, "Username Not Available!\nTry Another One", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String method = params[0];
            String reg_url = "http://shareandcare.eu.pn/register.php";
            String login_url = "http://shareandcare.eu.pn/login.php";
            if (method.equals("register")) {
                String ID, name, location, number, password, owner;
                ID = params[1];
                name = params[2];
                location = params[3];
                number = params[4];
                password = params[5];
                owner = params[6];
                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("id_app", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8") + "&" +
                            URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8") + "&" +
                            URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(number, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                            URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(owner, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    String response = "";
                    String line = "";
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    IS.close();
                    return response;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (method.equals("login")) {
                String login_name = params[1];
                String login_pass = params[2];
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                            URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "Unsuccessful";
        }
    }

}
