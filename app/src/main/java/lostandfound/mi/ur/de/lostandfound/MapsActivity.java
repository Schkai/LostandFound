package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback/*, PlaceSelectionListener */{

    private GoogleMap mMap;
    private Marker marker;
    private LocationHelper mLocHelper;

   // private TextView mPlaceDetailsText;

    //private TextView mPlaceAttribution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocHelper = new LocationHelper(this);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpSearchBar();
/*
        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);
        // Retrieve the TextViews that will display details about the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);
*/

    }

    private void setUpSearchBar() {

        Button searchButton = (Button) findViewById((R.id.search_button));
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText SearchEdit = (EditText) findViewById(R.id.search_edit_text);
                String location = SearchEdit.getText().toString();
                List<android.location.Address> addressList = null;
                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                       // Toast.makeText(MapsActivity.this, "Address unknown", Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }

                    if (addressList != null&&addressList.size()>0) {
                        android.location.Address address = addressList.get(0);
                        LatLng latLongLoc = new LatLng(address.getLatitude(), address.getLongitude());
                        MarkerOptions markerOpt = new MarkerOptions()
                                .position(latLongLoc)
                                .title("Your Position?");

                        marker.remove();
                        float zoomLevel = (float) 15.0;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongLoc, zoomLevel));
                        marker = mMap.addMarker(markerOpt);

                        showLocationSetDialog(location);
                    } else {
                        Log.d("address is null ","address is null");
                        Toast.makeText(MapsActivity.this, "Address unknown", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void showLocationSetDialog(String destination) {
        //build dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                MapsActivity.this);
        String Message = getString(R.string.mapsDialogMessage) +" "+destination+"?";
        dialog.setTitle(R.string.mapsDialogTitle);
        dialog.setMessage(Message);
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

        } else {
            lastLoc = getIntent().getExtras().getParcelable("last_loc");
            float zoomLevel = (float) 15.0;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel));
        }

        marker = mMap.addMarker(new MarkerOptions().position(lastLoc).title("Your position?"));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions markerOpt = new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("New Marker");
                marker.remove();
                marker = mMap.addMarker(markerOpt);
                showLocationSetDialog(mLocHelper.getAddressString(marker.getPosition().latitude,marker.getPosition().longitude));

            }
        });
    }
/*
    @Override
    public void onPlaceSelected(Place place) {
        Log.i("", "Place Selected: " + place.getName());

        // Format the returned place's details and display them in the TextView.


        CharSequence attributions = place.getAttributions();
        if (!TextUtils.isEmpty(attributions)) {
            mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
        } else {
            mPlaceAttribution.setText("");
        }
    }

    @Override
    public void onError(Status status) {

    }
*/


}
