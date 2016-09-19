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

public class DecimalJsonTypeAdapter implements JsonDeserializer<Double>, JsonSerializer<Double> {
    public DecimalJsonTypeAdapter() {
    }

    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String jsonDouble = json.getAsString();
        Double res = Double.valueOf(0.0D);

        try {
            res = Double.valueOf(Double.parseDouble(jsonDouble));
        } catch (Exception var7) {
            Log.d("ParseError", var7.getMessage());
        }

        return res;
    }

    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
