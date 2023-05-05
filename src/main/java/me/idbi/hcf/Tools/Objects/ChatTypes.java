package me.idbi.hcf.Tools.Objects;

import java.util.ArrayList;
import java.util.Arrays;

public enum ChatTypes {

    PUBLIC(new ArrayList<>(Arrays.asList("pub", "p", "all"))),
    STAFF(new ArrayList<>(Arrays.asList("admin", "mod", "s", "m", "a"))),
    FACTION(new ArrayList<>(Arrays.asList("fac", "f"))),
    ALLY(new ArrayList<>(Arrays.asList("alliance"))),
    LEADER(new ArrayList<>(Arrays.asList("l", "leader")));

    private ArrayList<String> alias;

    ChatTypes(ArrayList<String> alias) {
        this.alias = alias;
    }

    public ArrayList<String> getAlias() {
        return alias;
    }

    public static ChatTypes getByName(String name) {
        for (ChatTypes value : values())
            if(value.name().equalsIgnoreCase(name) || value.getAlias().contains(name.toLowerCase()))
                return value;
        return null;
    }
}