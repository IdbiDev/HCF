package me.idbi.hcf.Tools.Objects;

import java.util.HashMap;

public class PlayerObject {
    private final HashMap<String, Object> metadata = new HashMap<>();

    public void setData(String key, Object value) {
        metadata.put(key, value);
    }

    public String getData(String key) {
        if (hasData(key))
            return metadata.get(key).toString();
        //Bukkit.getLogger().severe(" Error while reading key:" + key);
        return "";
    }

    public Object getRealData(String key) {
        if (hasData(key))
            return metadata.get(key);
        //Bukkit.getLogger().severe(" Error while reading key:" + key);
        return null;
    }

    public boolean hasData(String key) {
        return metadata.containsKey(key);
    }
}
