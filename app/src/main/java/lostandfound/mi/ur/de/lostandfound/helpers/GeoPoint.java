package lostandfound.mi.ur.de.lostandfound.helpers;

import java.util.Locale;

/**
 * Created by Konstantin on 11.09.2016.
 */
public class GeoPoint {
    public static final int LONGITUDE_INDEX = 0;
    public static final int LATITUDE_INDEX = 1;
    public String type = "Point";
    public double[] coordinates = new double[]{0.0D, 0.0D};

    public GeoPoint() {
    }

    public GeoPoint(double lon, double lat) {
        this.coordinates[0] = lon;
        this.coordinates[1] = lat;
    }

    public GeoPoint(double[] coords) {
        this.coordinates = coords;
    }

    public String toString() {
        return String.format(Locale.US, "%.8f, %.8f", new Object[]{Double.valueOf(this.coordinates[1]), Double.valueOf(this.coordinates[0])});
    }
}
