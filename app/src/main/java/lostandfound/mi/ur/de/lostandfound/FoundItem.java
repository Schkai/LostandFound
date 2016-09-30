package lostandfound.mi.ur.de.lostandfound;

/**
 * Created by Konstantin on 21.09.2016.
 */

public class  FoundItem {

    private String name;
    private String date;
    private double longitude;
    private double latitude;
    private String category;
    private int id;
    private String description;
    private String town;
    private String contact;

    public FoundItem(String name, String date, double longitude, double latitude, String category, String description, String town, String contact){
        this.name=name;
        this.date=date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category=category;
        this.description = description;
        this.town = town;
        this.contact = contact;
    }

    public FoundItem(){
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setDescription(String description){this.description = description;}
    public String getDescription(){return description;}

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {return name;}
    public String getDate() {return date;}
    public double getLongitude() {return longitude;}
    public double getLatitude() {return longitude;}
    public String getCategory() {return category;}
    public String getContact() {return contact;}
    public String getTown() {return town;}
    public int getId() {return id;}



}

