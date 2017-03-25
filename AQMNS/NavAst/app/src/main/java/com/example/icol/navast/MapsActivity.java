package com.example.icol.navast;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double lat,longs;
    LatLng current;
    int complete;
    LinearLayout topbar;
    RelativeLayout appbar;
    RelativeLayout middle_bar;
    ImageButton pick;
    final LatLng GOJEK = new LatLng(-6.27314, 106.81657);
    ArrayList<Double> nodelatitude;
    ArrayList<Double> nodelongitude;
    ArrayList<Integer> nodeid;
    ArrayList<String> nodename;
    private void inisialisasi() {

        appbar = (RelativeLayout) findViewById(R.id.app_bar);
        topbar = (LinearLayout) findViewById(R.id.middle_bar);
        pick = (ImageButton) findViewById(R.id.pick);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        inisialisasi();
        complete=0;
        //buildGoogleApiClient();
        //mGoogleApiClient.connect();
        // inf.setVisibility(View.GONE);
        // strt.setVisibility(View.GONE);
        // text_tujuan.setVisibility(View.GONE);
        // but_tujuan.setVisibility(View.GONE);
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(GOJEK, 13);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);

    }

    public void pick_button(View view){
        topbar.setVisibility(View.GONE);
        pick.setVisibility(View.GONE);
        Toast.makeText(this,"Sentuh Peta",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        longs=location.getLongitude();
        current =new LatLng(lat,longs);
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
