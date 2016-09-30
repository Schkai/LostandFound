package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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


public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private TabHost tabHost;
    private FloatingActionButton addEntryButton;
    private Button setLocButton;


    public static final int DETAIL_REQUEST = 2;
    public static final int MAP_REQUEST = 1;
    public static final int NEW_ENTRY_REQUEST = 0;
    private static final String TAG = MainActivity.class.getSimpleName();

    protected TextView locationBar;
    private GoogleApiClient mGoogleApiClient;
    protected static Location mLastLocation;
    private LatLng theFindSpot;
    private LocationHelper locationHelper;

    private RecyclerView lostListView;
    private RecyclerView foundListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        locationHelper= new LocationHelper(getApplicationContext());
        initBars();
        buildGoogleApiClient();
        initButtons();

        initLists();
        initTabs();

    }


    /**
     * Set our location bar where the current location will be displayed
     */

    private void initBars () {
            locationBar = (TextView) findViewById(R.id.textView);
            getSupportActionBar().hide();

        }

    /**
     *
     * This is the heart of the recyclerView. This method automatically loads and populates the
     * recyclerView while connecting to the node child in our database which is the postalCode
     * of the user's current position. The Adapter fetches and refreshes data in realtime.
     * Problem: NestedScrollView in layout.xml doesn't show any entries, using a LinearLayout is
     * very slow as it loads data one at a time. We had to make a compromiss in the short time span.
     *
     * @param recyclerView
     * @param refChild
     */

       private void getFireBaseData(RecyclerView recyclerView, final String refChild) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            String postalCode = "unknown";
             if (theFindSpot != null) {postalCode = locationHelper.getPostalCodeFromLatLng(theFindSpot.latitude, theFindSpot.longitude);

    final DatabaseReference lostRef = ref.child(refChild).child(postalCode.toString());

                 FirebaseRecyclerAdapter < LostItem, MessageViewHolder > adapter =
                        new FirebaseRecyclerAdapter<LostItem, MessageViewHolder>(LostItem.class, R.layout.item_view, MessageViewHolder.class, lostRef) {
                            @Override
                            protected void populateViewHolder(MessageViewHolder viewHolder, final LostItem model, final int position) {
                                viewHolder.mText.setText(model.getName());

                                String modelCategory = model.getCategory();

                                switch (modelCategory) {
                                    case "Key": viewHolder.mCategory.setImageResource(R.drawable.key);
                                        break;
                                    case "Other": viewHolder.mCategory.setImageResource(R.drawable.help);
                                        break;
                                    case "Purse": viewHolder.mCategory.setImageResource(R.drawable.briefcase);
                                        break;

                                    case "Card": viewHolder.mCategory.setImageResource(R.drawable.credit_card);
                                        break;
                                    case "Clothing": viewHolder.mCategory.setImageResource(R.drawable.tshirt_crew);
                                        break;
                                    case "Electronic Device": viewHolder.mCategory.setImageResource(R.drawable.cellphone);
                                        break;
                                    case "Jewelry": viewHolder.mCategory.setImageResource(R.drawable.anchor);
                                        break;
                                    case "Wallet": viewHolder.mCategory.setImageResource(R.drawable.briefcase);
                                        break;
                                }

                                viewHolder.mLocation.setText(locationHelper.getAddressString(model.getLatitude(), model.getLongitude()));
                                viewHolder.mDate.setText(model.getDate());
                                Log.d("Mongo", "ich bin populate view und habe" + model.getName() + " " + viewHolder.mText.getText().toString());

                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        Log.d("Tabbug", "You clicked on position " + position);
                                        Intent detailIntent = new Intent(view.getContext(), DetailViewActivity.class);
                                        detailIntent.putExtra("itemName", model.getName());
                                        detailIntent.putExtra("itemCategory", model.getCategory());
                                        detailIntent.putExtra("itemDescription", model.getDescription());
                                        detailIntent.putExtra("itemContact", model.getContact());
                                        detailIntent.putExtra("itemDate", model.getDate());
                                        detailIntent.putExtra("itemLatitude", model.getLatitude());
                                        detailIntent.putExtra("itemLongitude", model.getLongitude());
                                        detailIntent.putExtra("last_loc", theFindSpot);
                                        startActivityForResult(detailIntent,DETAIL_REQUEST);
                                    }
                                });

                            }
                        };
                Log.d("Tabbug", "getFireBaseData, theFindSpot with Lat: " + theFindSpot.latitude + " long: " + theFindSpot.longitude + " address: " + locationHelper.getAddressString(theFindSpot.latitude, theFindSpot.longitude) + " PLZ: " + locationHelper.getPostalCodeFromLatLng(theFindSpot.latitude, theFindSpot.longitude));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                 } else {
                Log.d("Tabbug", "Findspot is null");
            }
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

    /**
     * Initialization of our Buttons.
     */

    private void initButtons() {
        addEntryButton = (FloatingActionButton) findViewById(R.id.new_entry_button);
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


    /**
     * When the phone is not able to get the current position, we can manually set our position
     * in a new activity with a google maps map.
     *
     */
    private void openMapsActivity() {

        Intent i = new Intent(this, MapsActivity.class);
        boolean locationUnknown = false;
        if (theFindSpot != null) {
            i.putExtra("last_loc", theFindSpot);
        } else {

            locationUnknown = true;
        }
        i.putExtra("loc_unknown", locationUnknown);

        startActivityForResult(i, MAP_REQUEST);
    }


    /**
     * Opens the NewEntryActivity which allows the user to post lost and found items.
     * Also, the current location will be provided for the class to use.
     */

    private void openNewEntryActivity() {
        Intent i = new Intent(this, NewEntryActivity.class);
        boolean locationUnknown = false;
        if (theFindSpot != null) {
            i.putExtra("last_loc", theFindSpot);
        } else {

            locationUnknown = true;
        }
        i.putExtra("loc_unknown", locationUnknown);




        startActivityForResult(i,NEW_ENTRY_REQUEST);
    }


    /**
     * Initialization of both tabs.
     */
    private void initTabs() {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("lost");
        tabSpec.setContent(R.id.lost);
        tabSpec.setIndicator("lost");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("found");
        tabSpec.setContent(R.id.found);
        tabSpec.setIndicator("found");
        tabHost.addTab(tabSpec);
    }


    /**
     * Initialization of both recyclerViews used for the lost and found tabs
     */

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
     * Gets the user's current location and updates it when being changed.
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
            }
        } else {
            if (theFindSpot == null) {
                Toast.makeText(this, "no Location detected", Toast.LENGTH_LONG).show();
                locationBar.setText("Location nicht gefunden!");
                }
        }
    }

    private void onLocationChanged() {
        getFireBaseData(foundListView, "FoundItem");
        getFireBaseData(lostListView, "LostItem");

        String locationName = "items near ";

        Log.d("Tabbug", "onLocationChanged, theFindSpot with Lat: "+theFindSpot.latitude+" long: "+ theFindSpot.longitude+ " address: "+locationHelper.getAddressString(theFindSpot.latitude, theFindSpot.longitude)+" PLZ: "+locationHelper.getPostalCodeFromLatLng(theFindSpot.latitude, theFindSpot.longitude));
        locationName+=locationHelper.getAddressString(theFindSpot.latitude,theFindSpot.longitude)+" ("+locationHelper.getCityNameFromLatLng(theFindSpot.latitude,theFindSpot.longitude)+")";
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
            Log.d("Tabbug", "onActivity Result, theFindSpot with Lat: "+latitude+" long: "+ longitude+ " address: "+locationHelper.getAddressString(latitude, longitude)+" PLZ: "+locationHelper.getPostalCodeFromLatLng(latitude, longitude));
            onLocationChanged();
        }
    }

    /**
     * Class needed for the firebaserecycleradapter to correctly populate recyclerviews.
     */

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ImageView mCategory;
        TextView mLocation;
        TextView mDate;
        View mView;

        public MessageViewHolder(View v) {
            super(v);
            this.mView = v;
            mText = (TextView) v.findViewById(R.id.item_name);
            mCategory = (ImageView) v.findViewById(R.id.item_category);
            mLocation = (TextView) v.findViewById(R.id.item_location);
            mDate = (TextView) v.findViewById(R.id.date);
        }
    }


}

