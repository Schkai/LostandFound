package lostandfound.mi.ur.de.lostandfound.REST;

import android.os.Parcel;
import android.os.Parcelable;

import lostandfound.mi.ur.de.lostandfound.helpers.GeoPoint;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantin on 11.09.2016.
 */

public class FoundItemItem implements Parcelable {

    public String name;
    public String content;
    public String latitude;
    public String longitude;
    public String category;
    public String ort;
    @SerializedName("_id")
    public String id;
    public GeoPoint location;
    // Parcelable interface

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(content);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(category);
        dest.writeString(ort);
        dest.writeString(id);
        dest.writeDoubleArray(location != null && location.coordinates.length != 0 ? location.coordinates : null);
    }

    public static final Creator<FoundItemItem> CREATOR = new Creator<FoundItemItem>() {
        @Override
        public FoundItemItem createFromParcel(Parcel in) {
            FoundItemItem item = new FoundItemItem();

            item.name = in.readString();
            item.content = in.readString();
            item.latitude = in.readString();
            item.longitude = in.readString();
            item.category = in.readString();
            item.ort = in.readString();
            item.id = in.readString();
            double[] location_coords = in.createDoubleArray();
            if (location_coords != null)
                item.location = new GeoPoint(location_coords);
            return item;
        }

        @Override
        public FoundItemItem[] newArray(int size) {
            return new FoundItemItem[size];
        }
    };

    public static class List extends ArrayList<FoundItemItem> {
    }

}

