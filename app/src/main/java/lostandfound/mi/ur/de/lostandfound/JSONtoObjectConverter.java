package lostandfound.mi.ur.de.lostandfound;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alexander on 21.09.2016.
 */

public class JSONtoObjectConverter extends ArrayList<LostItem>{ private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String CATEGORY = "category";
    private static final String TOWN = "town";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String DESCRIPTION = "description";
    private static final String CONTACT = "contact";
    private static final String ID = "id";


    private String JSONResponse;

    public ArrayList<LostItem> items = new ArrayList<LostItem>();

    public JSONtoObjectConverter(String JSONResponse) {
        this.JSONResponse = JSONResponse;
    }

    public ArrayList<LostItem> convertJSONToItemList() {


        try {
            JSONArray jsonArray = new JSONArray(JSONResponse);
            for(int i =0; i<jsonArray.length();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                String name =  jsonObject.getString(NAME);
                String date= jsonObject.getString(DATE);
                String category= jsonObject.getString(CATEGORY);
                String town = jsonObject.getString(TOWN);
                double longitude = jsonObject.getDouble(LONGITUDE);
                double latitude = jsonObject.getDouble(LATITUDE);
                String description = jsonObject.getString(DESCRIPTION);
                String contact = jsonObject.getString(CONTACT);

                //int id = jsonObject.getInt(ID);


                LostItem item = new LostItem(name, date, latitude, longitude, category, description, contact);

                items.add(item);


            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("jsonFail","failed to convert jsonobjects");
        }

        return items;



    }
}
