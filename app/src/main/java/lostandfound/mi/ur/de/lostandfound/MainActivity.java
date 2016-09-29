package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


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


    RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private LocationHelper locationHelper;

    //FIREBASE --------------
    private RecyclerView lostListView;
    private RecyclerView foundListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        locationHelper= new LocationHelper(getApplicationContext());
        initButtons();
        initBars();
        initTabs();
        initLists();



        buildGoogleApiClient();

    }



    private void getFireBaseData(RecyclerView recyclerView, String refChild) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String postalCode = "unknown";

        if (theFindSpot != null){
            postalCode =locationHelper.getPostalCodeFromLatLng(theFindSpot.latitude,theFindSpot.longitude);

            DatabaseReference lostRef = ref.child(refChild).child(postalCode.toString());
            FirebaseRecyclerAdapter<LostItem, MessageViewHolder> adapter =
                    new FirebaseRecyclerAdapter<LostItem, MessageViewHolder>(LostItem.class, R.layout.item_view, MessageViewHolder.class, lostRef) {
                        @Override
                        protected void populateViewHolder(MessageViewHolder viewHolder, LostItem model, int position) {
                            viewHolder.mText.setText(model.getName());
                            viewHolder.mCategory.setText(model.getCategory());
                            viewHolder.mLocation.setText(locationHelper.getAddressString(model.getLatitude(),model.getLongitude()));
                            viewHolder.mDate.setText(model.getDate());
                        }
                    };
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

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
        boolean locationUnknown = false;
        if (theFindSpot != null) {
            i.putExtra("last_loc", theFindSpot);
        } else {
            /*LatLng defaultLatLng = new LatLng(0, 0);
            i.putExtra("last_loc", defaultLatLng);*/
            locationUnknown = true;
        }
        i.putExtra("loc_unknown", locationUnknown);
        startActivityForResult(i, 1);
    }

    /**
     * Enters DesignTestActivity for layout testing purposes!
     * TODO: Change back to NewEntryActivity after new layout is finished!
     */

    private void openNewEntryActivity() {
        Intent i = new Intent(this, NewEntryActivity.class);
        boolean locationUnknown = false;
        if (theFindSpot != null) {
            i.putExtra("last_loc", theFindSpot);
        } else {
            /*LatLng defaultLatLng = new LatLng(0, 0);
            i.putExtra("last_loc", defaultLatLng);*/
            locationUnknown = true;
        }
        i.putExtra("loc_unknown", locationUnknown);



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

    private void initViews() {
        //   mRecyclerView = (RecyclerView) findViewById(R.id.lostView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }


    private void initLists() {
        lostListView = (RecyclerView) findViewById(R.id.lost_list);
        foundListView = (RecyclerView) findViewById(R.id.found_list);
        lostListView.setHasFixedSize(true);
        lostListView.setLayoutManager(new LinearLayoutManager(this));
        foundListView.setHasFixedSize(true);
        foundListView.setLayoutManager(new LinearLayoutManager(this));
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
                onLocationChanged();
                Intent intent = new Intent();
                intent.putExtra("latitude", mLastLocation.getLatitude());
                intent.putExtra("longitude", mLastLocation.getLongitude());
                setResult(1, intent);//lol was
            }
        } else {
            if (theFindSpot == null) {
                Toast.makeText(this, "no Location detected", Toast.LENGTH_LONG).show();
                locationBar.setText("Location nicht gefunden!");
                }
        }
    }

    private void onLocationChanged() {
        getFireBaseData(lostListView, "LostItem");
        getFireBaseData(foundListView, "FoundItem");
        String locationName = "items near ";


        locationName+=locationHelper.getAddressString(theFindSpot.latitude,theFindSpot.longitude);
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
            onLocationChanged();
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        TextView mCategory;
        TextView mLocation;
        TextView mDate;

        public MessageViewHolder(View v) {
            super(v);
            mText = (TextView) v.findViewById(R.id.item_name);
            mCategory = (TextView) v.findViewById(R.id.item_category);
            mLocation = (TextView) v.findViewById(R.id.item_location);
            mDate = (TextView) v.findViewById(R.id.date);
        }
    }


}

