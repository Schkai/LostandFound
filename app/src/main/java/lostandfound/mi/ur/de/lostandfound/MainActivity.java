package lostandfound.mi.ur.de.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<LostItem> itemsMissing;
    private ArrayList<LostItem> itemsFound;
    private ItemArrayAdapter adapter ;
    private TabHost tabHost;
    private Button addEntryButton;
    private Button setLocButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLists();
        initTabs();
        initButtons();

        //test
        LostItem i1 = new LostItem("test","test",null,"test",1);
        LostItem i2 = new LostItem("test2","test",null,"test",2);
        itemsMissing.add(i1);
        itemsFound.add(i2);

    }

    private void initButtons() {
        addEntryButton = (Button) findViewById(R.id.new_entry_button);
        setLocButton = (Button) findViewById(R.id.set_loc_button);

        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewEntryActivity();
            }
        });

    }

    private void openNewEntryActivity() {
        Intent i = new Intent(this, NewEntryActivity.class);
        //add location extra here later
        startActivity(i);
    }

    private void initTabs() {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Missing");
        tabSpec.setContent(R.id.lost);
        tabSpec.setIndicator("Missing");
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
