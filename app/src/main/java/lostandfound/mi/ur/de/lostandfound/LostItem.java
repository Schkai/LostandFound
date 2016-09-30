package lostandfound.mi.ur.de.lostandfound;

/**
 * Created by Alexander on 28.08.2016.
 */
public  class LostItem {
    private String name;
    private String date;
    private double longitude;
    private double latitude;
    private String category;
    private int id;
    private String description;
    private String postalCode;
    private String contact;
    private int spamMarked;


    public LostItem(String name, String date, double latitude, double longitude, String category, String description, String contact){
        this.name=name;
        this.date=date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category=category;
        this.description = description;
        this.contact = contact;
        postalCode ="";
        spamMarked=0;




    }

    public LostItem(){
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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
    public void markAsSpam(){spamMarked++;}

    public String getName() {return name;}
    public String getDate() {return date;}
    public double getLongitude() {return longitude;}
    public double getLatitude() {return latitude;}
    public String getCategory() {return category;}
    public String getContact() {return contact;}
    public String getPostalCode() {return postalCode;}
    public int getId() {return id;}
    public int getSpamMarked(){return spamMarked;}



}
