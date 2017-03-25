package com.example.icol.navast;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double lat,longit,latklik,longklik,lattemp,longtemp,lat_close,long_close,lat_start,long_start;
    LatLng current,dest;
    int complete,minakhir,min,mapclickflag=0;
    LinearLayout topbar;
    RelativeLayout appbar,bot_bar;
    ImageButton pick,set_cur;
    Button navigate;
    Marker mark;
    BitmapDescriptor markers;
    TextView cek,jarak,kadar;
    final LatLng GOJEK = new LatLng(-6.27314, 106.81657);
    ArrayList<Double> nodelatitude;
    ArrayList<Double> nodelongitude;
    ArrayList<Integer> nodeid;
    ArrayList<String> nodename;
    MarkerOptions markerOptions;
    private void inisialisasi() {

        appbar = (RelativeLayout) findViewById(R.id.app_bar);
        topbar = (LinearLayout) findViewById(R.id.middle_bar);
        pick = (ImageButton) findViewById(R.id.pick);
        markers = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        cek = (TextView) findViewById(R.id.cek);
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
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(GOJEK, 13);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);
//        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
//
//        // Spinner adapter
//        mySpinner
//                .setAdapter(new ArrayAdapter<Double>(MapsActivity.this,
//                        android.R.layout.simple_spinner_dropdown_item,
//                        nodelatitude));
//
//        // Spinner on item click listener
//

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
                    lat_start = nodelatitude.get(minakhir);
                    long_start = nodelongitude.get(minakhir);
                    if(lat_start!=0) {
                        if (complete == 1) {
                            dest = new LatLng(lat_start,long_start);
                            markeroption(dest, markers, nodename.get(minakhir));
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
        current = new LatLng(location.getLatitude(), location.getLongitude());
        lat_start = location.getLatitude();
        long_start = location.getLongitude();
       // noawal = nodeid.get(coor_closer(lat_start, long_start));
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
