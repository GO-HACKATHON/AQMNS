package com.example.icol.navast;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LatLng latLong;
    private GoogleApiClient mGoogleApiClient;
    double lat,longit,latklik,longklik,lattemp,longtemp,lat_close,long_close,lat_start,long_start;
    LatLng current,dest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    int complete,minakhir,min,mapclickflag=0,noawal,notuj;
    LinearLayout topbar;
    RelativeLayout appbar,bot_bar;
    ImageButton pick,set_cur;
    Button navigate;
    Marker mark;
    BitmapDescriptor markers;
    TextView cekawal,cektujuan,jarak,kadar;
    final LatLng GOJEK = new LatLng(-6.27314, 106.81657);
    ArrayList<Double> nodelatitude,nodelongitude;
    ArrayList<Integer> nodeid,route,cost;
    ArrayList<String> nodename;
    MarkerOptions markerOptions;
    JSONObject jsonobject;
    private void inisialisasi() {

        appbar = (RelativeLayout) findViewById(R.id.app_bar);
        topbar = (LinearLayout) findViewById(R.id.middle_bar);
        pick = (ImageButton) findViewById(R.id.pick);
        markers = BitmapDescriptorFactory.fromResource(R.drawable.marker);
          cekawal = (TextView) findViewById(R.id.awal);
        cektujuan = (TextView) findViewById(R.id.tujuan);
        bot_bar= (RelativeLayout) findViewById(R.id.bot_bar);
        kadar = (TextView) findViewById(R.id.kadar);
        jarak = (TextView) findViewById(R.id.jarak);
        set_cur = (ImageButton)findViewById(R.id.set_cur);
        navigate= (Button) findViewById(R.id.navigate);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        inisialisasi();
        navigate.setVisibility(View.GONE);
        kadar.setVisibility(View.GONE);
        jarak.setVisibility(View.GONE);
        bot_bar.setVisibility(View.GONE);
        nodename = (ArrayList<String>) getIntent().getSerializableExtra("noname");
        nodelatitude = (ArrayList<Double>) getIntent().getSerializableExtra("nolat");
        nodelongitude = (ArrayList<Double>) getIntent().getSerializableExtra("nolong");
        nodeid = (ArrayList<Integer>) getIntent().getSerializableExtra("noid");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        complete=0;

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        settingsrequest();
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(GOJEK, 13);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);

    }
    public void set_cur(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        current=latLong;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(100), 2000, null);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(mMap.getCameraPosition())
                .zoom(13)
                .bearing(0)
                .tilt(0)
                .build()));
    }
    private void markeroption(LatLng koor, BitmapDescriptor marker,String nama){
        markerOptions= new MarkerOptions().position(koor)
                .title(nama)
                .snippet("Pilih")
                .icon(marker);
    }
    public static double haversine(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);
        return dist;
    }
    public int findMinIdx(int[] numbers) {
        if (numbers == null || numbers.length == 0)
            return -1;
        int minVal = numbers[0];
        int minIdx = 0;
        for(int idx=1; idx<numbers.length; idx++) {
            if(numbers[idx] < minVal) {
                minVal = numbers[idx];
                minIdx = idx;
            }
        }
        return minIdx;
    }
    public int coor_closer(double lats,double longs){
        int[] dists = new int[nodelatitude.size()];
        int min;
        for (int i = 0; i < nodelatitude.size(); i++) {
            lat = nodelatitude.get(i);
            longit = nodelongitude.get(i);
            dists[i] = (int) haversine(lats, longs, lat, longit);
        }
        min = findMinIdx(dists);
        return min;
    }

    public void drawroute(){
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        points = new ArrayList<>();
        lineOptions = new PolylineOptions();
        double ruteawlat,ruteawlong,rutelat,rutelong,rutelat2,rutelong2;
        LatLng pos1,pos2,posakhir;

        rutelat = nodelatitude.get(0);
        rutelong = nodelatitude.get(0);
        ruteawlat=nodelatitude.get(noawal);
        ruteawlong=nodelongitude.get(noawal);
        pos1 = new LatLng(ruteawlat,ruteawlong);
        mMap.addPolyline(new PolylineOptions()
                .add(current, pos1)
                .color(Color.rgb(69, 134, 71)));

        for(int i=0;i<route.size();i++) {

            rutelat = nodelatitude.get(i);
            rutelong = nodelatitude.get(i);
            pos1=new LatLng(rutelat,rutelong);
            points.add(pos1);
        }
        lineOptions.addAll(points);
        lineOptions.width(10);
        lineOptions.color(Color.rgb(69, 134, 71));
        lineOptions.addAll(points);
        mMap.addPolyline(lineOptions);

    }
    private class Pathing extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            route = new ArrayList<Integer>();
            cost = new ArrayList<Integer>();

            String url="?src="+noawal+"&dst="+notuj;
            jsonobject = JSONParser.getJSONfromURL("http://10.17.10.171/hackathon/pathfinding.php"+url);

            try {
                cost.add(jsonobject.optInt("cost"));
                for (int i = 0; i < jsonobject.length(); i++) {
                    route.add(jsonobject.optInt(String.valueOf(i)));
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);

            mySpinner
                    .setAdapter(new ArrayAdapter<Integer>(MapsActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                           route));
       }
    }

    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsrequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }
    private void startLocationUpdates() {
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    public void navigate(View view){
        complete=complete+1;
        //final ImageView iconawal = (ImageView) findViewById(R.id.iconsrc);


        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(100), 2000, null);
// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(mMap.getCameraPosition())
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(65)                   // Sets the tilt of the camera to 30 degrees
                .build()));
    }
    protected void Mapclick( final BitmapDescriptor markers) {

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if(nodelatitude != null){
                    bot_bar.setVisibility(View.VISIBLE);
                    navigate.setVisibility(View.VISIBLE);
                    kadar.setVisibility(View.VISIBLE);
                    jarak.setVisibility(View.VISIBLE);
                    latklik = point.latitude;
                    longklik = point.longitude;
                    minakhir = coor_closer(latklik,longklik);
                    lattemp = nodelatitude.get(minakhir);
                    longtemp = nodelongitude.get(minakhir);
                    if(lattemp!=0) {
                        if (complete == 1) {
                            dest = new LatLng(lattemp,longtemp);
                            markeroption(dest, markers, nodename.get(minakhir));
                            notuj = nodeid.get(coor_closer(latklik, longklik));
                            cekawal.setText(String.valueOf(noawal));
                            cektujuan.setText(String.valueOf(notuj));
                            new Pathing().execute();
                            drawroute();
                        }
                        if (complete != 2) {
                            if (mapclickflag == 0) {
                                if (mark != null) {
                                    mark.remove();
                                    mark = mMap.addMarker(markerOptions);
                                    mapclickflag++;
                                } else {
                                    mark = mMap.addMarker(markerOptions);
                                    mapclickflag++;
                                }
                            } else {
                                mark.remove();
                                mark = mMap.addMarker(markerOptions);
                                mapclickflag++;
                            }
                        } else {
                        }
                    }
                    else {
                        Toast.makeText(MapsActivity.this, "Tidak Dapat Sambung ke Database", Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    Toast.makeText(MapsActivity.this, "Gangguan Koneksi", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public void pick_button(View view){
        topbar.setVisibility(View.GONE);
        pick.setVisibility(View.GONE);
        Toast.makeText(this,"Sentuh Peta",Toast.LENGTH_SHORT).show();
        Mapclick(markers);
        complete++;

    }

    @Override
    public void onLocationChanged(Location location) {
        latLong = new LatLng(location.getLatitude(), location.getLongitude());
        lat_start = location.getLatitude();
        long_start = location.getLongitude();
        current = latLong;
        noawal = nodeid.get(coor_closer(lat_start, long_start));
    }



    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLong = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLong);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        }
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); //5 seconds
        locationRequest.setFastestInterval(3000); //3 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
