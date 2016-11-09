package bd.ac.mist.sharecare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;


public class Post_Nav extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    ImageButton addPost;
    String jasonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        addPost = (ImageButton) findViewById(R.id.addpost);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adapter = new Adapter(this, R.layout.activity_post__nav);
        listView = (ListView) findViewById(R.id.post_list);
        listView.setAdapter(adapter);

        setUpList();

    }

    public void showDataList(String r) {
        try {
            jsonObject = new JSONObject(r);
            jsonArray = jsonObject.getJSONArray("post_array");
            int count = 0;
            String time, post, user_name;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                time = jo.getString("dt");
                post = jo.getString("des");
                user_name = jo.getString("name");

                Details details = new Details(post, user_name, time);
                adapter.add(details);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setUpList() {
        adapter.list=new ArrayList();
        BGTask backgroundTask = new BGTask(this);
        backgroundTask.execute();
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Toast.makeText(getBaseContext(), "Press again to exit!", Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post__nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Infos) {
            startActivity(new Intent(this, Tab.class));
        } else if (id == R.id.nav_GoogleMap) {
            startActivity(new Intent(this,MapsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addPost(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.post_input_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText post = (EditText) dialogView.findViewById(R.id.enter_post);
        final EditText name = (EditText) dialogView.findViewById(R.id.name);

        dialogBuilder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String postString = post.getText().toString(), name_post = name.getText().toString();

                if ((!postString.equals(null)) && (!postString.equals(""))) {
                    mAsyncTask asyncTask = new mAsyncTask(Post_Nav.this);//uncmnt this line
                    asyncTask.execute(postString, name_post);

                }

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    public class mAsyncTask extends AsyncTask<String, Void, String> {
        Context ctx;
        private String addPostUrl;

        public mAsyncTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            setUpList();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            addPostUrl = "http://shareandcare.eu.pn/add_post.php";
        }

        @Override
        protected String doInBackground(String... params) {
            String post = params[0];
            String user_name = params[1];
            try {
                URL url = new URL(addPostUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("post", "UTF-8") + "=" + URLEncoder.encode(post, "UTF-8") + "&" +
                        URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Posted";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Unsuccessful";
        }
    }


    public class BGTask extends AsyncTask<String, Void, String> {
        Context ctx;
        String jsonUrl;

        public BGTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            showDataList(result);
            jasonString = result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            jsonUrl = "http://shareandcare.eu.pn/json_get_post_data.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(jsonUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jasonString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jasonString + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String r = stringBuilder.toString().trim();
                return r;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Unsuccessful";
        }
    }


}
