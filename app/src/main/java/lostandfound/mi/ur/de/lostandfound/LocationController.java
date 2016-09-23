package lostandfound.mi.ur.de.lostandfound;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import java.util.List;
import java.util.Locale;

public class LocationController implements LocationListener {

    private static final long UPDATE_TIME = 5000;
    private static final float UPDATE_DISTANCE = 5;

    private Context context;
    private LocationUpdateListener locationUpdateListener;
    private LocationManager locationManager;
    private boolean geocoding = false;

    public LocationController(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public void start(boolean geocoding) {
        this.geocoding = geocoding;
        startLocationRequest();
    }

    public void stop() {
        geocoding = false;
        stopLocationRequest();
    }

    public void startLocationRequest() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    UPDATE_TIME, UPDATE_DISTANCE, this);
        } catch (SecurityException e) {

        }
    }

    public void stopLocationRequest() {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {

        }
    }

    public void setLocationUpdateListener(LocationUpdateListener listener) {
        locationUpdateListener = listener;
    }


    public String geocodeLocation(Location location) {
        if (!Geocoder.isPresent()) {
            return context.getResources().getString(R.string.location_default_location);
        }
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0) {
            String gecode = buildAddressString(addresses.get(0));
            return gecode;
        } else {
            return null;
        }
    }

    private String buildAddressString(Address address) {
        String addressString = "";
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressString += address.getAddressLine(i) + "\n";
        }
        return addressString;
    }

    @Override
    public void onLocationChanged(Location location) {
        locationUpdateListener.onNewLocation(location.getLatitude(),
                location.getLongitude());
        if (geocoding) {
            String address = geocodeLocation(location);
            if (address != null) {
                locationUpdateListener.onNewGeocode(address);
            }
        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public interface LocationUpdateListener {
        public void onNewLocation(double latitude, double longitude);

        public void onNewGeocode(String geocode);
    }

}