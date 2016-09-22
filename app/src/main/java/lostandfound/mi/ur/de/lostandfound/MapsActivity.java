package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        LatLng lastLoc;
        boolean lastLocUnknown = getIntent().getBooleanExtra("loc_unknown", false);


        // Add a marker and move the camera

        if (lastLocUnknown) {
            lastLoc = new LatLng(0, 0);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLoc));

        } else{
            lastLoc = getIntent().getExtras().getParcelable("last_loc");
            float zoomLevel = (float) 15.0;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel));}

        marker = mMap.addMarker(new MarkerOptions().position(lastLoc).title("Deine Position"));




        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions markerOpt = new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("New Marker");
                marker.remove();
                marker = mMap.addMarker(markerOpt);


                //build dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        MapsActivity.this);
                dialog.setTitle(R.string.mapsDialogTitle);
                dialog.setMessage(R.string.mapsDialogTitle);
                dialog.setNegativeButton(R.string.mapsDialogNo, null);
                dialog.setPositiveButton(R.string.mapsDialogYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Activity.RESULT_OK,
                                new Intent().putExtra("latitude", marker.getPosition().latitude).putExtra("longitude", marker.getPosition().longitude));

                        finish();
                    }
                });

                dialog.show();

            }
        });
    }
}
