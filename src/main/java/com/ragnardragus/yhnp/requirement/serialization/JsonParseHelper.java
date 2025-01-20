package com.ragnardragus.yhnp.requirement.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public class JsonParseHelper {

    public static String getString(JsonObject obj, String key) {
        Optional<JsonElement> jsonElement = Optional.of(obj.get(key));
        return jsonElement.orElseThrow(NullPointerException::new).getAsString();
    }

    public static int getInt(JsonObject obj, String key) {
        Optional<JsonElement> jsonElement = Optional.of(obj.get(key));
        return jsonElement.orElseThrow(NullPointerException::new).getAsInt();
    }

    public static double getDouble(JsonObject obj, String key) {
        Optional<JsonElement> jsonElement = Optional.of(obj.get(key));
        return jsonElement.orElseThrow(NullPointerException::new).getAsDouble();
    }
}
