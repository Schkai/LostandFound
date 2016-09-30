package lostandfound.mi.ur.de.lostandfound;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexander on 23.09.2016.
 */

public class LocationHelper {

    private Context context;
    private Geocoder geocoder;

    public LocationHelper(Context context) {
        this.context = context;
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public String getAddressString(double lat, double lng) {

        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0) {
                address += addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return address;
    }
    public String getPostalCodeFromLatLng(double lat, double lng){
        String postalCode = "unknown";

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {

            postalCode = addresses.get(0).getPostalCode();

        }
        return postalCode;
    }
    public String getCityNameFromLatLng(double lat, double lng){
        String name = "";

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {

            name = addresses.get(0).getLocality();

        }
        return name;
    }

}
