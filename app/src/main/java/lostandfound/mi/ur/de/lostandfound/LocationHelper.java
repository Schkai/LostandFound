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


    public LocationHelper(Context context) {
        this.context = context;
    }

    public String getAddressString(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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

    public void updatePostalCodeForItem(LostItem item) {
        String postalCode = "unknown";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = gcd.getFromLocation(item.getLatitude(), item.getLongitude(), 1);


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {

            postalCode = addresses.get(0).getPostalCode();

        }
        item.setPostalCode(postalCode);
    }

}
