package lostandfound.mi.ur.de.lostandfound;

/**
 * Created by Alexander on 28.08.2016.
 */
public class Item {
    private String name;
    private String date;
    private String location;
    private String category;
    private int id;

    public Item(String name, String date, String location, String category, int id){
        this.name=name;
        this.date=date;
        this.category=category;
        this.location=location;
        this.id=id;

    }

    public String getName() {return name;}
    public String getDate() {return date;}
    public String getCategory() {return category;}
    public String getLocation(){return location;}
    public int getId() {return id;}



}
