package lostandfound.mi.ur.de.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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

    private Spinner spinner;
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

        locationHelper = new LocationHelper(this);

        initDateInputField();
        initPlaceInputField();
        initSpinner();
        //  initCategorySpinner();
        initPublishEntryButton();
        // back-button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize fb
        db = FirebaseDatabase.getInstance().getReference();
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

    private void initSpinner() {
        spinner = (Spinner) findViewById(R.id.post_option_spinner);

        List<String> list = new ArrayList<String>();
        list.add("Lost");
        list.add("Found");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        //Spinner item selection Listener
        addListenerOnSpinnerItemSelected();


    }

    private void addListenerOnSpinnerItemSelected() {
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    private void initPublishEntryButton() {
        Button publishEntryButton = (Button) findViewById(R.id.publish_entry_button);
        publishEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (String.valueOf(spinner.getSelectedItem()) == "Lost") {

                    Firebase newItem = ref.child("LostItem");
                    newItem.push().setValue(createItemFromInput());

                } else {


                    Firebase newItem = ref.child("FoundItem");
                    newItem.push().setValue(createItemFromInput());

                }


                Toast.makeText(NewEntryActivity.this, "Item saved!", Toast.LENGTH_LONG).show();
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
        EditText contentEdit = (EditText) findViewById(R.id.input_content_edit);
        EditText descEdit = (EditText) findViewById(R.id.input_description_edit);
        EditText contactEdit = (EditText) findViewById(R.id.input_contact_edit);


        String name = nameEdit.getText().toString();
        String date = dateEdit.getText().toString();
        // Double latitude = getIntent().getDoubleExtra("latitude", 0);
        //  Double longitude = getIntent().getDoubleExtra("longitude", 0);
        String content = contentEdit.getText().toString();
        String description = descEdit.getText().toString();
        String contact = contactEdit.getText().toString();

        LostItem item = new LostItem(name, date, latitude, longitude, content, description, contact);

        //warum setzen wir die Werte ein 2. mal? /Alex
        item.setName(name);
        item.setDate(date);
        item.setLatitude(latitude);
        item.setLongitude(longitude);
        item.setCategory(content);
        item.setDescription(description);
        locationHelper.updatePostalCodeForItem(item);
        item.setContact(contact);
        return item;
    }

    /*public void updatePostalCodeForItem(LostItem item) {
        String postalCode = "unknown";
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = gcd.getFromLocation(item.getLatitude(), item.getLongitude(), 1);


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {

            postalCode = addresses.get(0).getPostalCode();

        }
        item.setPostalCode(postalCode);
    }*/
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
                NavUtils.navigateUpFromSameTask(this);
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
