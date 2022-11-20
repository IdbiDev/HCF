package me.idbi.hcf.tools;

import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.*;


public class JsonUtils {
    //Lol lopott pls dont kill me

    public static Map<String, Object> jsonToMap(org.json.JSONObject json) {
        Map<String, Object> retMap = new HashMap<>();

        if (json != null) {
            retMap = toMap(json);
        }
        return retMap;
    }
    public static Map<String, Object> toMap(org.json.JSONObject object) {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keysItr = object.keySet().iterator();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof org.json.JSONObject) {
                value = toMap((org.json.JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((org.json.JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

}