package gov.cipam.gi.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;

import gov.cipam.gi.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    String sellerAddress;
    double latitude,longitude;
    LatLng sellerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapActivity);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            latitude= (double) bundle.get("latitude");
            longitude= (double) bundle.get("longitude");
            sellerAddress=bundle.getString("address");
        }


        mapFragment.getMapAsync(this);
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
        // Add a marker in Sydney and move the camera
        sellerLocation = new LatLng(latitude, longitude);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sellerLocation));
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }else{
            setmMap();
            // Write you code here if permission already given.
        }

    }

    private void setmMap(){
        mMap.addMarker(new MarkerOptions().position(sellerLocation).title(""+sellerAddress));


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                sellerLocation,12));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude,longitude))      // Sets the center of the map to location user
                .zoom(10)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
