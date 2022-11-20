package me.idbi.hcf.Enchantments.ETools;

import me.idbi.hcf.Enchantments.Enchants;
import me.idbi.hcf.tools.JsonUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EnchantJson {

    public static HashMap<Enchants, Integer> getEnch(JSONObject json) {
        Map<String, Object> map = JsonUtils.jsonToMap(json);
        HashMap<Enchants, Integer> enchants = new HashMap<>();

        for (Map.Entry<String, Object> hash : map.entrySet()) {
            enchants.put(Enchants.EnchantmentType.getByName(hash.getKey()).asEnchant(), (int) hash.getValue());
        }

        return enchants;
    }

    public static JSONObject getJSON(Map<String, Object> map) {
        JSONObject enchants = new JSONObject();

        for (Map.Entry<String, Object> hash : map.entrySet()) {
            enchants.put(hash.getKey(), hash.getValue());
        }

        return enchants;
    }
}
