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

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {return name;}
    public String getDate() {return date;}
    public String getCategory() {return category;}
    public Location getLocation(){return location;}
    public int getId() {return id;}



}
