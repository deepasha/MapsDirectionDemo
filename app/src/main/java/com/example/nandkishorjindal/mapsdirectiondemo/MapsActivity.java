  package com.example.nandkishorjindal.mapsdirectiondemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocation;
    private  Marker position1;
    private  Marker position2;
    private  Marker position3;
    private  Marker position4;





    public static final int PERMISSION_REQUEST_LOCATION_CODE =99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            checkLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_LOCATION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission is granted
                    if(ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if (client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled( true );

                    }
                    else //permission is denied
                    {
                        Toast.makeText( this, "Permission denied",Toast.LENGTH_SHORT ).show();
                    }
                }
        }
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

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )==PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();

            mMap.setMyLocationEnabled( true );
             position1= mMap.addMarker(new MarkerOptions().position(new LatLng(10.066150, 76.6199765)).title("station 1"));
         position2=   mMap.addMarker(new MarkerOptions().position(new LatLng(10.065960,76.676653)).title("station 2"));
          position3=  mMap.addMarker(new MarkerOptions().position(new LatLng(10.060763,76.63443)).title("station 3"));
          position4 =  mMap.addMarker(new MarkerOptions().position(new LatLng(10.04097,76.622968)).title("station 4").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));



        }


    }

    protected synchronized void buildGoogleApiClient() {

        client = new GoogleApiClient.Builder( this ).addConnectionCallbacks( this ).addOnConnectionFailedListener( this ).addApi( LocationServices.API )
                .build();

        client.connect();


    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;

        if (currentLocation != null) {
            currentLocation.remove();
        }
        LatLng latLng = new LatLng( location.getLatitude(), location.getLongitude() );
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position( latLng );
        markerOptions.title( "Current Location" );
        markerOptions.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE ) );

        currentLocation = mMap.addMarker( markerOptions );
        mMap.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
        mMap.animateCamera( CameraUpdateFactory.zoomBy( 1 ) );

        if (client != null) {
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates( client, locationRequest, this );
        }

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval( 1000 );
        locationRequest.setFastestInterval( 1000 );
        locationRequest.setPriority( LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY );

        if(ContextCompat.checkSelfPermission( this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED )
        {
            LocationServices.FusedLocationApi.requestLocationUpdates( client, locationRequest, this );
        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.ACCESS_FINE_LOCATION ))
            {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION} ,PERMISSION_REQUEST_LOCATION_CODE );

            }
            else {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION} ,PERMISSION_REQUEST_LOCATION_CODE );

            }
            return false;
        }
        else
            return true;
    }




    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
