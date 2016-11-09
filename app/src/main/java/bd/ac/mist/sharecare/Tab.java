package bd.ac.mist.sharecare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Tab extends Activity {
    ListView res, orp;
    String jasonString1,jasonString2;

    JSONObject jsonObject1,jsonObject2;
    JSONArray jsonArray1,jsonArray2;
    mListAdapter adapterRes,adapterOrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        TabHost tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();
        TabHost.TabSpec tabspec = tabhost.newTabSpec("restaurants");
        tabspec.setContent(R.id.Restaurants);
        tabspec.setIndicator("Restaurants");
        tabhost.addTab(tabspec);
        tabspec = tabhost.newTabSpec("orphanages");
        tabspec.setContent(R.id.Orphanages);
        tabspec.setIndicator("Orphanages");
        tabhost.addTab(tabspec);

        res = (ListView) findViewById(R.id.listViewRes);
        orp = (ListView) findViewById(R.id.listViewOrphan);
        adapterRes=new mListAdapter(Tab.this,R.layout.activity_tab);
        adapterOrp=new mListAdapter(Tab.this,R.layout.activity_tab);
        res.setAdapter(adapterRes);
        orp.setAdapter(adapterOrp);

        setUpList1();
        setUpList2();

        res.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Info info= (Info) adapterRes.getItem(position);
                String Id,name,contact,location;
                Id=info.getID();
                name=info.getName();
                contact=info.getContact();
                location=info.getLocation();
                Intent i=new Intent(Tab.this,AllDetailsActivity.class);
                i.putExtra("ID",Id);
                i.putExtra("name",name);
                i.putExtra("contact",contact);
                i.putExtra("location",location);
                startActivity(i);
//                Toast.makeText(Tab.this,Id+name+contact+location,Toast.LENGTH_SHORT).show();
            }
        });
        orp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Info info= (Info) adapterOrp.getItem(position);
                String Id,name,contact,location;
                Id=info.getID();
                name=info.getName();
                contact=info.getContact();
                location=info.getLocation();
                Intent i=new Intent(Tab.this,AllDetailsActivity.class);
                i.putExtra("ID",Id);
                i.putExtra("name",name);
                i.putExtra("contact",contact);
                i.putExtra("location",location);
                startActivity(i);
//                Toast.makeText(Tab.this,Id+name+contact+location,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showDataList1(String r) {
        try {
            jsonObject1 = new JSONObject(r);
            jsonArray1 = jsonObject1.getJSONArray("res_array");
            int count = 0;
            String name, location,contact,id;
            while (count < jsonArray1.length()) {
                JSONObject jo = jsonArray1.getJSONObject(count);
                name = jo.getString("name");
                location = jo.getString("location");
                contact=jo.getString("contact");
                id=jo.getString("id");
                Info details = new Info( location,name,contact,id);
                adapterRes.add(details);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showDataList2(String r) {
        try {
            jsonObject2 = new JSONObject(r);
            jsonArray2 = jsonObject2.getJSONArray("orp_array");
            int count = 0;
            String name, location,contact,id;
            while (count < jsonArray2.length()) {
                JSONObject jo = jsonArray2.getJSONObject(count);
                name = jo.getString("name");
                location = jo.getString("location");
                contact=jo.getString("contact");
                id=jo.getString("id");
                Info details = new Info( location,name,contact,id);
                adapterOrp.add(details);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUpList1() {
        NetThreadHandler1 backgroundTask = new NetThreadHandler1(this);
        backgroundTask.execute();
    }
    public void setUpList2() {
        NetThreadHandler2 backgroundTask = new NetThreadHandler2(this);
        backgroundTask.execute();
    }

    public class NetThreadHandler2 extends AsyncTask<String, Void, String> {
        Context ctx;
        String orpUrl;


        public NetThreadHandler2(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            showDataList2(result);
            jasonString2 = result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            orpUrl="http://shareandcare.eu.pn/orp_json.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(orpUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jasonString2 = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jasonString2 + "\n");
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

    public class NetThreadHandler1 extends AsyncTask<String, Void, String> {
        Context ctx;
        String resUrl;


        public NetThreadHandler1(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            showDataList1(result);
            jasonString1 = result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            resUrl="http://shareandcare.eu.pn/res_json.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(resUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jasonString1 = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jasonString1 + "\n");
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

    class mListAdapter extends ArrayAdapter {
        List list = new ArrayList();

        public mListAdapter(Context context, int resource) {
            super(context, resource);
        }

        public void add(Info object) {
            super.add(object);
            list.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            row = convertView;
            mHolder holder;
            if (row == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.tab_list_layout, parent, false);
                holder = new mHolder();
                holder.Name = (TextView) row.findViewById(R.id.nameTv);
                row.setTag(holder);
            } else {
                holder = (mHolder) row.getTag();
            }
            Info details = (Info) this.getItem(position);
            holder.Name.setText(details.getName());
            return row;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }


    }
    class mHolder {
        TextView Name;
    }
    class Info{
        String Location,Name,Contact,ID;

        public Info(String location, String name, String contact, String ID) {
            Location = location;
            Name = name;
            Contact = contact;
            this.ID = ID;
        }

        public String getLocation() {
            return Location;
        }

        public String getName() {
            return Name;
        }

        public String getContact() {
            return Contact;
        }

        public String getID() {
            return ID;
        }
    }


}
