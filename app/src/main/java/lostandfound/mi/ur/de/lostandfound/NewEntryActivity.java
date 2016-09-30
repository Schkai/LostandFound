package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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

/**
 * Created by Alexander on 31.08.2016.
 */
public class NewEntryActivity extends AppCompatActivity {

    private Spinner lfSpinner;
    private Spinner categorySpinner;
    private EditText placeEdit;
    private LocationHelper locationHelper;
    private double latitude;
    private double longitude;


    DatabaseReference db;
    Firebase ref = new Firebase("https://lostandfound-d91c9.firebaseio.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        latitude = 0;
        longitude = 0;
        locationHelper = new LocationHelper(this);

        initDateInputField();
        initPlaceInputField();
        initlfSpinner();
        initCategorySpinner();
        //  initCategorySpinner();
        initPublishEntryButton();
        // back-button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize fb
        db = FirebaseDatabase.getInstance().getReference();
    }

    private void initCategorySpinner() {
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        List<String> list = new ArrayList<String>();

        list.add("Other");
        list.add("Key");
        list.add("Wallet");
        list.add("Card");
        list.add("Clothing");
        list.add("Electronic device");
        list.add("Jewelry");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);


    }

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
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            String address = locationHelper.getAddressString(latitude, longitude);
            placeEdit.setText("near " + address);
        }
    }

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

    private void addListenerOnSpinnerItemSelected(Spinner spinner) {
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    /**
     * DO NOT (!!!) delete the .toString() in the child query even though android studio won't shut up about it being redundant!
     * Just don't do it! It will fuck up the whole database!
     */

    private void initPublishEntryButton() {
        Button publishEntryButton = (Button) findViewById(R.id.publish_entry_button);
        publishEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                //   Toast.makeText(NewEntryActivity.this, "Item saved!", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

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
            Toast.makeText(this, "Please enter a name or a description for your item!", Toast.LENGTH_SHORT).show();
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
                /*if (latitude != 0 && longitude != 0) {
                    setResult(Activity.RESULT_OK,
                            new Intent().putExtra("latitude", latitude).putExtra("longitude", longitude));
                    finish();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }*/
                onBackPressed();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }


    //DateDialogue
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
            //Note: can we base the calendar on the location?
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
            String date = df.format(gregCal.getTime());
            tv.setText(date);
        }
    }


}
