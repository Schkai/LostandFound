package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private ArrayList<LostItem> itemsMissing;
    private ArrayAdapter<String> lostAdapter;
    private ArrayList<LostItem> itemsFound;
    private ItemArrayAdapter adapter;
    private TabHost tabHost;
    private Button addEntryButton;
    private Button setLocButton;

    public static final int MAP_REQUEST = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected TextView locationBar;


    private GoogleApiClient mGoogleApiClient;
    protected static Location mLastLocation;
    private LatLng theFindSpot;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    //FIREBASE -------------
    private ListView lostListView;
    private ListView foundListView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        initButtons();
        initBars();
        initTabs();
        initLists();
        getFireBaseLostData();
        getFireBaseFoundData();


        buildGoogleApiClient();
    }

    private void getFireBaseLostData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lostRef = ref.child("LostItem");


        FirebaseListAdapter<LostItem> adapter = new FirebaseListAdapter<LostItem>(this, LostItem.class, android.R.layout.two_line_list_item, lostRef) {
            @Override
            protected void populateView(View v, LostItem model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView)v.findViewById(android.R.id.text2)).setText(model.getDescription());
            }
        };
        lostListView.setAdapter(adapter);
    }

    private void getFireBaseFoundData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference foundRef = ref.child("FoundItem");

        FirebaseListAdapter<FoundItem> foundItemFirebaseListAdapter = new FirebaseListAdapter<FoundItem>(this, FoundItem.class, android.R.layout.two_line_list_item, foundRef) {
            @Override
            protected void populateView(View v, FoundItem model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView)v.findViewById(android.R.id.text2)).setText(model.getDescription());
            }
        };
        foundListView.setAdapter(foundItemFirebaseListAdapter);
    }

    private void initBars() {
        getSupportActionBar().hide();
        locationBar = (TextView) findViewById(R.id.textView);

    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initButtons() {
        addEntryButton = (Button) findViewById(R.id.new_entry_button);
        setLocButton = (Button) findViewById(R.id.set_loc_button);

        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewEntryActivity();
            }
        });
        setLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });
    }

    private void openMapsActivity() {

        Intent i = new Intent(this, MapsActivity.class);
        if (theFindSpot != null) {
            i.putExtra("last_loc", theFindSpot);
        } else {
            LatLng defaultLatLng = new LatLng(0, 0);
            i.putExtra("last_loc", defaultLatLng);
        }
        startActivityForResult(i, 1);
    }

    private void openNewEntryActivity() {
        Intent i = new Intent(this, NewEntryActivity.class);

        //add location extra here later
        startActivity(i);
    }

    private void initTabs() {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Missing");
        tabSpec.setContent(R.id.lost);
        tabSpec.setIndicator("Missing");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("found");
        tabSpec.setContent(R.id.found);
        tabSpec.setIndicator("found");
        tabHost.addTab(tabSpec);
    }

    private void initViews(){
     //   mRecyclerView = (RecyclerView) findViewById(R.id.lostView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }



    private void initLists() {
        lostListView = (ListView)findViewById(R.id.lost_list);
        foundListView = (ListView)findViewById(R.id.found_list);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */

    @Override
    public void onConnected(Bundle connectionHint) {

        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, "Daten gefunden");

            if (theFindSpot == null) {
                theFindSpot = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                updateLocationBar();
                Intent intent = new Intent();
                intent.putExtra("latitude", mLastLocation.getLatitude());
                intent.putExtra("longitude", mLastLocation.getLongitude());
                setResult(1, intent);
            }

        } else {
            if (theFindSpot == null) {
                Toast.makeText(this, "no Location detected", Toast.LENGTH_LONG).show();
                locationBar.setText("Location nicht gefunden!");
            }
        }
    }

    private void updateLocationBar() {
        String locationName = "items near ";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(theFindSpot.latitude, theFindSpot.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                locationName += addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //handle exception here
        }
        locationBar.setText(locationName);
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.d(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);


        if (/*requestCode == MAP_REQUEST && */resultCode == Activity.RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);

            theFindSpot = new LatLng(latitude, longitude);
            updateLocationBar();
        }

    }
}

