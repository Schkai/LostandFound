package lostandfound.mi.ur.de.lostandfound.helpers;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateJsonTypeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
    private static final String[] isoFormats = new String[]{"yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'", "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'", "yyyy-MM-dd\'T\'HH:mm\'Z\'", "yyyy-MM-dd", "yyyyMMdd", "yy-MM-dd", "yyMMdd"};

    public DateJsonTypeAdapter() {
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String jsonDate = json.getAsString();
        String[] arr$ = isoFormats;
        int len$ = arr$.length;
        int i$ = 0;

        while (i$ < len$) {
            String format = arr$[i$];

            try {
                SimpleDateFormat e = new SimpleDateFormat(format);
                e.setTimeZone(TimeZone.getTimeZone("UTC"));
                return e.parse(jsonDate);
            } catch (Exception var10) {
                Log.d("ParseError", var10.getMessage());
                ++i$;
            }
        }

        return null;
    }

    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new JsonPrimitive(sdf.format(src));
    }
}