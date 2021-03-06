package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.lib.recaptcha.ReCaptcha;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class NewEntryActivity extends AppCompatActivity implements ReCaptcha.OnShowChallengeListener, ReCaptcha.OnVerifyAnswerListener {

    private Spinner lfSpinner;
    private Spinner categorySpinner;
    private EditText placeEdit;
    private LocationHelper locationHelper;
    private double latitude;
    private double longitude;
    //private static final String PUBLIC_KEY = "6LdSBQgUAAAAALoW1i3hecAq7RV2erkWf8mwfOo3";
    //private static final String PRIVATE_KEY = "6LdSBQgUAAAAAAo9kbNB8CCNgrWvpojlQNG02Utk";
    private static final String PUBLIC_KEY = "6LekFQgUAAAAAGEbVr16svEp9F-L1_5vS_lo2n7n";
    private static final String PRIVATE_KEY = "6LekFQgUAAAAAL1Yy04P5FYgomMk8BYW2Qe5r_Wh";
    DatabaseReference db;
    Firebase ref = new Firebase("https://lostandfound-d91c9.firebaseio.com");

    private ReCaptcha reCaptcha;
    private boolean captchaSolved;
    private EditText captchaInput;
    private Button verify;
    private Button reloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        captchaSolved = false;
        locationHelper = new LocationHelper(this);

        initDateInputField();
        initPlaceInputField();
        initlfSpinner();
        initCategorySpinner();
        initCaptcha();
        initPublishEntryButton();
        // back-button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize fb
        db = FirebaseDatabase.getInstance().getReference();


        showChallenge();



    }

    /**
     * Material Spinner used for picking a category of the item which was lost our found
     */

    private void initCategorySpinner() {
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        List<String> list = new ArrayList<String>();

        list.add("Other");
        list.add("Key");
        list.add("Purse");
        list.add("Card");
        list.add("Clothing");
        list.add("Electronic Device");
        list.add("Jewelry");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);


    }

    /**
     * Since the MainActivity provides this activity with the current location, it will automatically
     * fill the respective entry. If there is no entry, a google maps activity will be opened for the
     * user to select the location of the lost or found item
     */

    private void initPlaceInputField() {
        placeEdit = (EditText) findViewById(R.id.input_place_edit);
        final boolean lastLocUnknown = getIntent().getBooleanExtra("loc_unknown", false);
        LatLng lastLoc;
        placeEdit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //open MapsActivity
                Intent i = new Intent(NewEntryActivity.this, MapsActivity.class);
                boolean locationUnknown = false;
                if (!lastLocUnknown) {
                    LatLng loc = getIntent().getExtras().getParcelable("last_loc");
                    i.putExtra("last_loc", loc);
                } else {

                    locationUnknown = true;
                }
                i.putExtra("loc_unknown", locationUnknown);

                startActivityForResult(i, 1);

            }
        });


        if (!lastLocUnknown) {

            lastLoc = getIntent().getExtras().getParcelable("last_loc");
            String address = locationHelper.getAddressString(lastLoc.latitude, lastLoc.longitude);
            placeEdit.setText("near " + address);
            latitude = lastLoc.latitude;
            longitude = lastLoc.longitude;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            String address = locationHelper.getAddressString(latitude, longitude);
            placeEdit.setText("near " + address);
        }
    }

    /**
     * Spinner used for choosing whether the item is lost or found
     */

    private void initlfSpinner() {
        lfSpinner = (Spinner) findViewById(R.id.post_option_spinner);

        List<String> list = new ArrayList<String>();
        list.add("Lost");
        list.add("Found");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lfSpinner.setAdapter(dataAdapter);

        //Spinner item selection Listener
        addListenerOnSpinnerItemSelected(lfSpinner);


    }


    /**
     * Custom Sspinner behavior
     * @param spinner
     */

    private void addListenerOnSpinnerItemSelected(Spinner spinner) {
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    /**
     * This method pushes the new entry to their respective child nodes in the database.
     * DO NOT (!!!) delete the .toString() in the child query even though android studio won't
     * shut up about it being redundant! Just don't do it! It will fuck up the whole database structure!
     */

    private void initPublishEntryButton() {
        Button publishEntryButton = (Button) findViewById(R.id.publish_entry_button);
        publishEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (captchaSolved) {

                    if (String.valueOf(lfSpinner.getSelectedItem()) == "Lost") {

                        String postalCode = locationHelper.getPostalCodeFromLatLng(latitude, longitude);

                        Firebase newItem = ref.child("LostItem").child(postalCode.toString());
                        newItem.push().setValue(createItemFromInput());

                    } else {

                        String postalCode = locationHelper.getPostalCodeFromLatLng(latitude, longitude);

                        Firebase newItem = ref.child("FoundItem").child(postalCode.toString());
                        newItem.push().setValue(createItemFromInput());

                    }

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Item saved", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    finish();
                }else{Toast.makeText(NewEntryActivity.this, "Please enter the captcha code", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Create a new item for initPublishEntryButton to push to database.
     * @return item
     */

    private LostItem createItemFromInput() {

        //GET DATA
        EditText nameEdit = (EditText) findViewById(R.id.input_name_edit);
        EditText dateEdit = (EditText) findViewById(R.id.input_date_edit);
        //Double latitude = getIntent().getDoubleExtra("latitude", 0);
        //Double longitude = getIntent().getDoubleExtra("longitude", 0);
        EditText descEdit = (EditText) findViewById(R.id.input_description_edit);
        EditText contactEdit = (EditText) findViewById(R.id.input_contact_edit);


        String name = nameEdit.getText().toString();
        String date = dateEdit.getText().toString();
        String category = String.valueOf(categorySpinner.getSelectedItem());
        String description = descEdit.getText().toString();
        String contact = contactEdit.getText().toString();

        LostItem item = new LostItem(name, date, latitude, longitude, category, description, contact);

        //warum setzen wir die Werte ein 2. mal? /Alex
        //weil halt.

        if (name.equals("") || description.equals("")) {
            Toast.makeText(this, "Please enter a name and a description for your item!", Toast.LENGTH_SHORT).show();
        } else {
            item.setName(name);
            item.setDate(date);
            item.setLatitude(latitude);
            item.setLongitude(longitude);
            item.setCategory(category);
            item.setDescription(description);
            item.setPostalCode(locationHelper.getPostalCodeFromLatLng(item.getLatitude(), item.getLongitude()));
            item.setContact(contact);

            return item;
        }
        return null;
    }


    private void initDateInputField() {

        EditText dateEdit = (EditText) findViewById(R.id.input_date_edit);
        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openDatePickerDialogue();
            }
        });
    }

    private void openDatePickerDialogue() {
        DialogFragment dateFragment = new DatePickFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    //backButton
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {

        switch (mi.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }


    /**
     * Pick the date with a beautiful date picker.
     */

    public static class DatePickFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //set current date as he default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);

        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            TextView tv = (TextView) getActivity().findViewById(R.id.input_date_edit);
            GregorianCalendar gregCal = new GregorianCalendar(year, month, dayOfMonth);

            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
            String date = df.format(gregCal.getTime());
            tv.setText(date);
        }
    }

    /**
     * Captcha for spam protection
     */

    private void initCaptcha() {
        reCaptcha = (ReCaptcha) findViewById(R.id.recaptcha);
        reCaptcha.showChallengeAsync("your-public-key", this);

        captchaInput = (EditText) findViewById(R.id.captcha_input);
        verify = (Button) findViewById(R.id.verify_button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (captchaInput.getText().toString().equals("")) {
                    Toast.makeText(NewEntryActivity.this, "Please enter the upper code", Toast.LENGTH_SHORT).show();

                } else {

                    reCaptcha.verifyAnswerAsync(PRIVATE_KEY, captchaInput.getText().toString(), NewEntryActivity.this);
                }
            }
        });

        reloadButton =(Button)findViewById(R.id.reload_button);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChallenge();
            }
        });



    }

    @Override
    public void onChallengeShown(boolean shown) {

        if (shown) {

            reCaptcha.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnswerVerified(boolean success) {
        if (success) {
            Toast.makeText(this, "Captcha solved", Toast.LENGTH_SHORT).show();
            captchaSolved = true;
            reCaptcha.setVisibility(View.INVISIBLE);
            captchaInput.setVisibility(View.INVISIBLE);
            verify.setVisibility(View.INVISIBLE);
            reloadButton.setVisibility(View.INVISIBLE);

        } else {
            Toast.makeText(this, "You entered the wrong code", Toast.LENGTH_SHORT).show();
            captchaInput.setText("");
            showChallenge();
        }

    }

    private void showChallenge() {
        reCaptcha.setVisibility(View.GONE);

        reCaptcha.setLanguageCode("en");
        reCaptcha.showChallengeAsync(PUBLIC_KEY, this);
    }

}
