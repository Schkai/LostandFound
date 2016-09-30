package lostandfound.mi.ur.de.lostandfound;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Konstantin on 29.09.2016.
 */

public class DetailViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView mNameTxt;
    private TextView mCategoryTxt;
    private TextView mDescriptionTxt;
    private TextView mDateTxt;
    private TextView mPlaceTxt;
    private TextView mContactTxt;

    private String name;
    private String category;
    private String description;
    private String date;
    private String place;
    private LatLng placeLatLng;
    private String contact;
    private GoogleMap mMap;
    private Marker marker;
    private LocationHelper mLocHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view_activity);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLocHelper = new LocationHelper(this);
        initTextViews();
        getIntents();
        populateTextViews();




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Initialize TextViews with custom typeface
     */

    private void initTextViews() {

       mNameTxt = (TextView) findViewById(R.id.name_detail_view);
       mCategoryTxt = (TextView) findViewById(R.id.category_detail_view);
       mDescriptionTxt = (TextView) findViewById(R.id.description_detail_view);
       mDateTxt = (TextView) findViewById(R.id.date_detail_view);
       mPlaceTxt = (TextView) findViewById(R.id.place_detail_view);
       mContactTxt = (TextView) findViewById(R.id.contact_detail_view);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/MavenPro-Medium.ttf");

        mNameTxt.setTypeface(face);
        mCategoryTxt.setTypeface(face);
        mDescriptionTxt.setTypeface(face);
        mDateTxt.setTypeface(face);
        mPlaceTxt.setTypeface(face);
        mContactTxt.setTypeface(face);

    }

    /**
     * Gets intent from respective RecyclerView Entry
     */

    private void getIntents(){
        name = getIntent().getExtras().getString("itemName");
        category  = getIntent().getExtras().getString("itemCategory");
        description = getIntent().getExtras().getString("itemDescription");
        date = getIntent().getExtras().getString("itemDate");
        contact = getIntent().getExtras().getString("itemContact");
        double lat =getIntent().getExtras().getDouble("itemLatitude");
        double lng = getIntent().getExtras().getDouble("itemLongitude");
        placeLatLng= new LatLng(lat,lng);
        place = mLocHelper.getAddressString(lat,lng);

    }

    /**
     * Set the Textviews content
     */

    private void populateTextViews() {
        mNameTxt.setText(name);
        mCategoryTxt.setText(category);
        mDescriptionTxt.setText(description);
        mDateTxt.setText(date);
        mContactTxt.setText(contact);
        mPlaceTxt.setText(place);
        checkPhoneNumber(mContactTxt.toString());
    }



    private void checkPhoneNumber(CharSequence number) {

        if(validMobileNumber((String) number)){
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "nummer gefunden!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {

        }
    }

    public boolean validMobileNumber(String number){
        return Patterns.PHONE.matcher(number).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {

        switch (mi.getItemId()) {
            case android.R.id.home:
               /* LatLng loc = getIntent().getExtras().getParcelable("last_loc");
                    setResult(Activity.RESULT_OK,
                            new Intent().putExtra("latitude", loc.latitude).putExtra("longitude", loc.longitude));
                    finish();*/
                onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {mMap = googleMap;
setupMarker();
    }

    private void setupMarker() {

            float zoomLevel = (float) 15.0;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, zoomLevel));


        marker = mMap.addMarker(new MarkerOptions().position(placeLatLng).title(name));

    }
}

