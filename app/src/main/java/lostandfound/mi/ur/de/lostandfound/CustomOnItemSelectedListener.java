package lostandfound.mi.ur.de.lostandfound;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by Konstantin on 22.09.2016.
 */

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(adapterView.getContext(),
                "Oh, you have " + adapterView.getItemAtPosition(i).toString() + " something!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
