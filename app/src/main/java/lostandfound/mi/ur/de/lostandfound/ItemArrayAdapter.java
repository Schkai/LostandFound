package lostandfound.mi.ur.de.lostandfound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alexander on 28.08.2016.
 */
public class ItemArrayAdapter  extends ArrayAdapter<LostItem> {private ArrayList<LostItem> items;
    private Context context;
    private LostItem item;


    public ItemArrayAdapter(Context context, ArrayList<LostItem> items) {
        super(context, R.layout.item_view, items);
        this.context= context;
        this.items= items;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        View v = convertView;
        if(v==null){
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_view, null);

        }

        item = items.get(position);

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView category = (TextView) v.findViewById(R.id.category);
        //TextView location = (TextView) v.findViewById(R.id.location);
        TextView date = (TextView) v.findViewById(R.id.date);


        name.setText(item.getName());
        category.setText(item.getCategory());

        //location.setText(item.getLocation().toString());
        date.setText(item.getDate());


        return v;
    }




}
