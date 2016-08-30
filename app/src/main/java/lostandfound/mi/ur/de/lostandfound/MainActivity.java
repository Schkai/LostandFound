package lostandfound.mi.ur.de.lostandfound;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<LostItem> itemsMissing;
    private ArrayList<LostItem> itemsFound;
    private ItemArrayAdapter adapter ;
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLists();
        initTabs();


    }
    private void initTabs() {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Lost");
        tabSpec.setContent(R.id.lost);
        tabSpec.setIndicator("Lost");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("found");
        tabSpec.setContent(R.id.found);
        tabSpec.setIndicator("found");
        tabHost.addTab(tabSpec);
    }

    private void initLists() {
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
