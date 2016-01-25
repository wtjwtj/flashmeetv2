package com.fm.johan.flashmeet;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Intent i;
    private float state;
    private Marker mMarker;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timer();
        }
    };

    private void timer() {
        LocationHelper.getLocation(MapsActivity.this);
        handler.postDelayed(runnable,10000);
    }


    String s;
    ParseObject myobject;

    LocationManager lm;
    LocationManager mLocationManager;
    LocationListener ll;

    double lat1 = 51.0504088;
    double long1 = 13.7372621;

    List<Marker> markers = new ArrayList<Marker>();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(mMarker != null)
                {
                    mMarker.remove();
                }
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();

                LatLng latLng = new LatLng(currentLatitude, currentLongitude);

                //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title("I am here!");
                mMarker=mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 1301);
                return;
            }
        }
        getBatteryLevel(MapsActivity.this);
        if(state >0.15) {
            lm.requestLocationUpdates(GPS_PROVIDER, 5000, 0, ll);
        }
        else{
            lm.requestLocationUpdates(GPS_PROVIDER, 40000, 0, ll);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dresden = new LatLng(51.0504088,13.7372621);

        mMap.addMarker(new MarkerOptions().position(dresden).title("Dresden"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dresden));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f));
        mMap.setOnInfoWindowClickListener(this);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng point) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
                dialog.setMessage("Do you want to create an new event at this location?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent ii = new Intent(getApplicationContext(), newflash_screen.class);
                                ii.putExtra("lat", point.latitude);
                                ii.putExtra("long", point.longitude);
                                startActivity(ii);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog acreate = dialog.create();
                acreate.setTitle("New Event");
                acreate.show();

            }
        });

        receiveData();
    }



    private void receiveData (){
        // +++++++++++++GET LAST LOCATION++++++++++++
       // Location location = getLastLocation();

        //lat1    = location.getLatitude();
        //long1   = location.getLongitude();


        final Date dateac = new Date();

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.HOUR_OF_DAY,-3);      // ak. Zeit minus 3 stunden;
        Date datefrom = cal2.getTime();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2); 			// aktuel. tag + 2 tage
        Date dateto = cal.getTime();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        query.whereGreaterThan("date", datefrom);
        query.whereLessThan("date", dateto);
        query.whereGreaterThan("lat", lat1-0.005);
        query.whereLessThan("lat", lat1+0.005);
        query.whereGreaterThan("long", long1-0.01);
        query.whereLessThan("long", long1+0.01);

        //breit +0.005 = 555 m
        //läng  +0.01  = 655 m

        // if user is offline, user local data
        if(-1 == myCheck.state(getApplicationContext()) ) {
            query.fromLocalDatastore();
            Toast.makeText(MapsActivity.this,"You are offline. Get Events from Local Data",Toast.LENGTH_LONG).show();
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {

                int count = 0;

                if (scoreList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Sorry,there are no events", Toast.LENGTH_LONG).show();
                    return;
                }
                if (e == null) {
                    // im hintergrund speichern
                    // ParseObject.pinAllInBackground(scoreList);
                    // display markers
                    while ((scoreList.size() - 1) >= count) {
                        myobject = scoreList.get(count);
                        Marker marker = mMap.addMarker(
                                new MarkerOptions().position(
                                        new LatLng(Double.parseDouble(myobject.get("lat").toString()),
                                                Double.parseDouble(myobject.get("long").toString())))
                                        .title(myobject.get("name").toString())
                                        .snippet(myobject.get("description").toString()));
                                        Date dateob = new Date();
                                        dateob = myobject.getDate("date");
                        if(dateac.getTime() >= dateob.getTime())
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        markers.add(marker);
                        count++;
                    }


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }


    private void delMarker (){
        int c = 0;
        while (markers.size() -1 >= c) {
            markers.get(c).remove();
            c++;
        }
        markers.clear();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        double markerlat = marker.getPosition().latitude;
        double markerlong = marker.getPosition().longitude;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("lat", markerlat);
        query.whereEqualTo("long", markerlong);
        // if user is offline, user local data
        if(-1 == myCheck.state(getApplicationContext()) ) {
            query.fromLocalDatastore();
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    SimpleDateFormat df = new SimpleDateFormat("'Date: 'E dd.MM.yyyy\n'Time: ' HH:mm");

                    ParseObject object = scoreList.get(0);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
                    dialog.setMessage(df.format(object.get("date")) + "\n\n" + "Description: " + "\n" + object.get("description").toString())
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });

                    AlertDialog acreate = dialog.create();
                    acreate.setTitle(object.get("name").toString());
                    acreate.show();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_sync:{
                delMarker();
                receiveData();
                Toast.makeText(MapsActivity.this,"map has been updated", Toast.LENGTH_LONG).show();
            }
            default: return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    public  float getBatteryLevel(Context context)
    {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        float level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        float scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        state= level/scale;

        return state;
    }

}


