package lostandfound.mi.ur.de.lostandfound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<LostItem> itemsMissing;
    private ArrayList<LostItem> itemsFound;
    private ItemArrayAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();


    }
    private void initUI() {
        ListView missingList = (ListView) findViewById(R.id.missing_list);
        ListView foundList = (ListView) findViewById(R.id.found_list);

        itemsMissing = new ArrayList<LostItem>();
        adapter = new ItemArrayAdapter(MainActivity.this, itemsMissing);
        missingList.setAdapter(adapter);

        itemsFound = new ArrayList<LostItem>();
        adapter = new ItemArrayAdapter(MainActivity.this, itemsFound);
        foundList.setAdapter(adapter);
    }
}
