package lostandfound.mi.ur.de.lostandfound;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import lostandfound.mi.ur.de.lostandfound.FireBase.FirebaseHelper;
import lostandfound.mi.ur.de.lostandfound.FireBase.MyAdapter;

/**
 * Created by Alexander on 31.08.2016.
 */
public class NewEntryActivity extends AppCompatActivity {

    private Spinner spinner;
    private LostItem lostitem;


    DatabaseReference db;
    FirebaseHelper helper;
    MyAdapter adapter;
    Firebase ref = new Firebase("https://lostandfound-d91c9.firebaseio.com");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        initDateInputField();
        //  initCategorySpinner();
        initPublishEntryButton();
        // back-button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize fb
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db);
        //


    }

    /*private void initCategorySpinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Handy");
        categories.add("Schlüssel");
        categories.add("Bier");

        //Create adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }
    */

    private void initPublishEntryButton() {
        Button publishEntryButton = (Button)findViewById(R.id.publish_entry_button);
        publishEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                EditText nameEdit = (EditText) findViewById(R.id.input_name_edit);
                EditText dateEdit = (EditText) findViewById(R.id.input_date_edit);
                EditText contentEdit = (EditText) findViewById(R.id.input_content_edit);
                EditText descEdit = (EditText) findViewById(R.id.input_description_edit);
                EditText contactEdit = (EditText) findViewById(R.id.input_contact_edit);

                String name = nameEdit.getText().toString();
                String date = dateEdit.getText().toString();
                String content = contentEdit.getText().toString();
                String description = descEdit.getText().toString();
                String contact = contactEdit.getText().toString();


                Firebase newItem = ref.child("LostItem");
                LostItem item = new LostItem(name, date,0,0, content, description, description, description);


                //SET DATA
               /* LostItem l = new LostItem();
                l.setName(name);
                l.setContent(content);
                l.setDescription(description);*/

                item.setName(name);
                item.setDate(date);
                item.setLongitude(0); //0 for now, should be intent extra
                item.setLongitude(0); //same applies here
                item.setCategory(content);
                item.setDescription(description);
                item.setTown("Regensburg"); //Should be converted from gps and inserted
                item.setContact(contact);
                newItem.push().setValue(item);


                //SAVE

                    Toast.makeText(NewEntryActivity.this, "Item saved!", Toast.LENGTH_LONG).show();
                finish();


            }
        });
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
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }

    //DateDialogue
    public static class DatePickFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //set current date as he default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this,year,month,dayOfMonth);

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
