package lostandfound.mi.ur.de.lostandfound.helpers;

/**
 * Created by Konstantin on 11.09.2016.
 */

import android.support.annotation.Nullable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StringUtils {
    public StringUtils() {
    }

    public static String firstNChars(String theString, int nChars, boolean ellipsize) {
        if (theString == null) {
            return null;
        } else {
            int endIndex = theString.length() > nChars ? nChars : theString.length() - 1;
            return theString.substring(0, endIndex) + (ellipsize ? " ..." : "");
        }
    }

    public static String removeImgTag(String input) {
        return input.replaceAll("<img.+?>", "");
    }

    public static Number StringToNumber(Object num) {
        if (num == null) {
            return null;
        } else if (num instanceof Number) {
            return (Number) num;
        } else {
            Float res = null;

            try {
                if (num instanceof String) {
                    res = Float.valueOf(Float.parseFloat((String) num));
                }
            } catch (NumberFormatException var3) {
                Log.d("Parsing Error", "Error parsing string to number " + var3.getMessage());
            }

            return res;
        }
    }

    public static Date parseDate(String date) {
        Date res = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss\'Z\'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            res = df.parse(date);
        } catch (ParseException var4) {
            Log.e("ParseError", var4.getMessage());
        }

        return res;
    }

    public static String monthName(int month) {
        Calendar cal = Calendar.getInstance();

        try {
            cal.set(month, 2);
            SimpleDateFormat e = new SimpleDateFormat("MMM");
            return e.format(cal.getTime());
        } catch (Exception var3) {
            return null;
        }
    }

    public static String doubleToString(Double val, boolean removeTrailingZeroes) {
        if (val == null) {
            return null;
        } else {
            String res = val.toString();
            return removeTrailingZeroes ? res.replaceAll("\\.0*$", "") : res;
        }
    }

    public static Long parseLong(String value) {
        Long res;
        try {
            res = value != null && value.length() != 0 ? Long.valueOf(Long.parseLong(value.toString())) : null;
        } catch (NumberFormatException var3) {
            res = null;
        }

        return res;
    }

    public static Double parseDouble(String value) {
        Double res;
        try {
            res = value != null && value.length() != 0 ? Double.valueOf(Double.parseDouble(value.toString())) : null;
        } catch (NumberFormatException var3) {
            res = null;
        }

        return res;
    }

    public static String booleanToString(Boolean value) {
        return value == null ? "" : (value.booleanValue() ? "Yes" : "No");
    }

    @Nullable
    public static URL parseUrl(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException var2) {
            return null;
        }
    }

    @Nullable
    public static URL parseUrl(String baseUrl, String url, String qs) {
        if (url == null) {
            return null;
        } else if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (baseUrl != null) {
                String postfix = qs != null ? (url.indexOf(63) >= 0 ? "&" : "?") + qs : "";
                if (baseUrl.endsWith("/")) {
                    baseUrl = baseUrl.substring(0, baseUrl.length());
                }

                return parseUrl(baseUrl + url + postfix);
            } else {
                return null;
            }
        } else {
            return parseUrl(url);
        }
    }

    public static URL parseUrl(URL url) {
        return url;
    }
}
