package me.idbi.hcf.API;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.CustomTimers;

import java.util.HashMap;

public class HCFCustomTimerApi {

    public static HashMap<String, CustomTimers> getCustomTimers() {
        return Main.customSBTimers;
    }

    public static boolean isCreated(String name) {
        return Main.customSBTimers.containsKey(name);
    }

    /**
     *
     * @param name
     * @return custom timer, otherwise null
     */
    public static CustomTimers getTimerByName(String name) {
        if(isCreated(name))
            return Main.customSBTimers.get(name);
        return null;
    }
}
