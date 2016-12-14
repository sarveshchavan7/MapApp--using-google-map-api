package sarveshchavan777csgo.mapsapp;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServiceAvailable()) {
            Toast.makeText(this, "you have googleplay service", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
            initMap();
        } else {
            //no google map layout
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }

    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int avi = api.isGooglePlayServicesAvailable(this);
        if (avi == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(avi)) {
            Dialog d = api.getErrorDialog(this, avi, 0);
            d.show();
        } else {
            Toast.makeText(this, "Failure in loading googleplay service", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //goTolocationZoom(18.939832,72.8330603, 15);


        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          // if (checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
          // ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
         // return;
       // }
        //}
        //mGoogleMap.setMyLocationEnabled(true);
     /*  mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();*/


    }


    private void goTolocationZoom(double lat, double lng, float zooom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zooom);
        mGoogleMap.animateCamera(update);
    }
    Marker m;
   public void geoLocate(View view) throws IOException {
        EditText et = (EditText) findViewById(R.id.editText);
       InputMethodManager mm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
       mm.hideSoftInputFromWindow(et.getWindowToken(),0);
       String e= et.getText().toString();
       if(e.matches("")) {
          Toast.makeText(this,"please type some value",Toast.LENGTH_LONG).show();
       } else {


           String location = et.getText().toString();

           Geocoder gc = new Geocoder(this);
           final List<Address> list = gc.getFromLocationName(location, 1);
           Address address = list.get(0);
           final String locality = address.getLocality();

           Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

           double lat = address.getLatitude();
           double lng = address.getLongitude();
            if(m != null){
                m.remove();
            }

           goTolocationZoom(lat, lng, 15);

           MarkerOptions options=new MarkerOptions()
           .title(locality)
                   .position(new LatLng(lat,lng))
                   .draggable(true);

                  m=  mGoogleMap.addMarker(options);
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

               }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder gc=new Geocoder(MainActivity.this);
                        LatLng ll=marker.getPosition();
                        double lat=ll.latitude;
                        double lng=ll.longitude;
                        List<Address>list=null;
                        try{
                        list=gc.getFromLocation(lat,lng,1);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        Address add=list.get(0);
                        marker.setTitle(add.getLocality());
                        marker.showInfoWindow();
                }
            });


       }
   }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.maptypenone:
                Intent my1=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(my1);

                // mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.maptypenormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.maptypeterrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.maptypesatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.maptypehybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.locateu:
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);





        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            LatLng latLng=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");

            mGoogleMap.addMarker(markerOptions);
        }







       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
    if(location==null)
    {
        Toast.makeText(this,"wtf",Toast.LENGTH_LONG).show();
    }else
    {
        LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());


        CameraUpdate update=CameraUpdateFactory.newLatLngZoom(ll,15);
        mGoogleMap.animateCamera(update);
    }
    }
}




