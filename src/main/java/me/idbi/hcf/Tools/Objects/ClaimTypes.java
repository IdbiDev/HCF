package me.idbi.hcf.Tools.Objects;

public enum ClaimTypes {
    NORMAL,
    PROTECTED,
    KOTH,
    SPECIAL,
    NONE;

    public static ClaimTypes getByName(String name) {
        for (ClaimTypes value : values()) {
            if(value.name().equalsIgnoreCase(name))
                return value;
        }
        return null;
    }
}
