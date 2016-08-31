package lostandfound.mi.ur.de.lostandfound;

import android.location.Location;

/**
 * Created by Alexander on 28.08.2016.
 */
public class LostItem {
    private String name;
    private String date;
    private Location location;
    private String category;
    private int id;

    public LostItem(String name, String date, Location location, String category, int id){
        this.name=name;
        this.date=date;
        this.category=category;
        this.location=location;
        this.id=id;

    }

    public String getName() {return name;}
    public String getDate() {return date;}
    public String getCategory() {return category;}
    public Location getLocation(){return location;}
    public int getId() {return id;}



}
