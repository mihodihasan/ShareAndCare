package bd.ac.mist.sharecare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AllDetailsActivity extends AppCompatActivity {

    View mLayout;
    private static final int CALL_PHONE=1;
    Intent i;

    TextView name,location,id,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details);

        name= (TextView) findViewById(R.id.textView);
        id= (TextView) findViewById(R.id.textView2);
        location=(TextView)findViewById(R.id.textView3);
        contact=(TextView)findViewById(R.id.textView4);

        name.setText(getIntent().getStringExtra("name"));
        id.setText(getIntent().getStringExtra("ID"));
        location.setText(getIntent().getStringExtra("location"));
        contact.setText(getIntent().getStringExtra("contact"));

        mLayout=findViewById(R.id.layout_details);
    }

    public void callMe(View view) {
        Intent sIntent = new Intent(Intent.ACTION_CALL, Uri


                .parse("tel:"+contact.getText().toString()));


        sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        i=sIntent;
        AskPermission();
    }


    public void AskPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();

        } else {
            startActivity(i);
        }

    }

    private void requestCallPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Snackbar.make(mLayout, "Need Call Permission to Initiate a Call to This Number",
                    Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(AllDetailsActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    CALL_PHONE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Permission Available",
                        Snackbar.LENGTH_LONG).show();
                startActivity(i);

            } else {
                Snackbar.make(mLayout, "permission was NOT granted",
                        Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(AllDetailsActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                CALL_PHONE);
                    }
                }).show();

            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
