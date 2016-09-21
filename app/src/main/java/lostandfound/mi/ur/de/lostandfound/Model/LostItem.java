package lostandfound.mi.ur.de.lostandfound.Model;

/**
 * Created by Konstantin on 21.09.2016.
 */
public class LostItem {

    String name;
    String content;
    String description;
    String latitude;
    String longitude;
    String category;
    String contact;
    String location;

    //Empty Constructor
    public LostItem(){
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCategory() {
        return category;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
