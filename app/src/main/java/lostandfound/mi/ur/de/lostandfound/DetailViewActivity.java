package lostandfound.mi.ur.de.lostandfound;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Konstantin on 29.09.2016.
 */

public class DetailViewActivity extends AppCompatActivity {

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
    private String contact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initTextViews();
        getIntents();
        populateTextViews();

    }

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

    private void getIntents(){
        name = getIntent().getExtras().getString("itemName");
        category  = getIntent().getExtras().getString("itemCategory");
        description = getIntent().getExtras().getString("itemDescription");
        date = getIntent().getExtras().getString("itemDate");
        contact = getIntent().getExtras().getString("itemContact");
    }

    private void populateTextViews() {
        mNameTxt.setText(name);
        mCategoryTxt.setText(category);
        mDescriptionTxt.setText(description);
        mDateTxt.setText(date);
        mContactTxt.setText(contact);
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
}

