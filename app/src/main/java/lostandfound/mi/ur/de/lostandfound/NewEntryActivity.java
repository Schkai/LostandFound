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
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Alexander on 31.08.2016.
 */
public class NewEntryActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        initDateInputField();
        initPublishEntryButton();
        // back-button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initPublishEntryButton() {
        Button publishEntryButton = (Button)findViewById(R.id.publish_entry_button);
        publishEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEdit = (EditText) findViewById(R.id.input_name_edit);
                EditText dateEdit = (EditText) findViewById(R.id.input_date_edit);
                String name = nameEdit.getText().toString();
                String date = dateEdit.getText().toString();
                if (!name.equals("")) {
                    addNewEntry();
                }

            }
        });
    }

    private void addNewEntry() {
        // neues item generieren in die DB fotzen und Activity zerhauen
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
