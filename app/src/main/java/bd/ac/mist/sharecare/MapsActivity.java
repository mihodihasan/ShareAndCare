package bd.ac.mist.sharecare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LatLng dhk;
    private GoogleMap mMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    UiSettings us;
    View mLayout;
    public static final int REQUEST_LOCATION_FINE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /////////////////////////MY CODE///////////////////////
        Log.d("LSN", "I am in onCreate");
        mMap = mapFragment.getMap();
        Log.d("LSN", "" + mMap);
        Button btn_find = (Button) findViewById(R.id.btn_find);
        mLayout = findViewById(R.id.layout_parent);
        View.OnClickListener findClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskPermission();
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                String location = etLocation.getText().toString();

                if (location != null && !location.equals("")) {
                    new GeocoderTask().execute(location);
                }
            }
        };

        btn_find.setOnClickListener(findClickListener);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        us = mMap.getUiSettings();
        us.setMapToolbarEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                AskPermission();
                mMap.clear();
                String addressString;
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getCountryName());
                    }
                    addressString = sb.toString();
                } catch (Exception e) {
                    addressString = "";
                    Toast.makeText(MapsActivity.this, "Error Detected!See LOGCAT for details!!", Toast.LENGTH_SHORT).show();
                    Log.d("BUG", e.toString());
                }
                mMap.addCircle(new CircleOptions().center(latLng).radius(1000.0).strokeColor(0xff00ff00)
                        .strokeWidth(10).visible(true));
                mMap.addMarker(new MarkerOptions().position(dhk).title("Home _ Dhaka"));
                Toast.makeText(MapsActivity.this, addressString, Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                AskPermission();
                mMap.clear();
                String addressString;
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getCountryName());
                    }
                    addressString = sb.toString();
                } catch (Exception e) {
                    addressString = "";
                    Toast.makeText(MapsActivity.this, "Error Detected!See LOGCAT for details!!", Toast.LENGTH_SHORT).show();
                    Log.d("BUG", e.toString());
                }
                mMap.addMarker(new MarkerOptions().position(latLng).title(addressString));
                mMap.addMarker(new MarkerOptions().position(dhk).title("Home _ Dhaka"));
            }
        });
        dhk = new LatLng(23.8103, 90.4125);
        mMap.addMarker(new MarkerOptions().position(dhk).title("Home _ Dhaka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhk));
        AskPermission();
    }

    public void AskPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();

        } else {
            us.setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
        }

    }

    private void requestLocationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(mLayout, "Need Location Permission to access your location",
                    Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION_FINE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_FINE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION_FINE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Permission Available",
                        Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(mLayout, "permission was NOT granted",
                        Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION_FINE);
                    }
                }).show();

            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(MapsActivity.this);//changed
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found\nTry Something Else!", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            mMap.clear();

            // Adding Markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {

                Address address = addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                mMap.addMarker(markerOptions);

                // Locate the first location
                if (i == 0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

}
