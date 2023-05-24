package me.idbi.hcf.Tools.Objects;

public enum ClaimAttributes {
    /**
     *
     *  Block place and break is denied.
     *  The player can't PvP
     *  The player can't enter the zone, while having Combat tag
     *  Usages: Spawn
     * */
    PROTECTED,
    /**
     *  Block place and break is denied.
     *  The player can PvP
     *  The player can't enter the zone, while having PvP cool-down.
     *  Usages: KoTH outer zone
     * */
    NORMAL,
    /**
     *  Block place and break is denied.
     *  The player can PvP
     *  Capture zone
     *  Usages: KoTH capture zone
     * */
    KOTH,
    /**
     *  Block place and break is denied.
     *  The player can PvP
     *  The player cant enter the zone, while having PvP cool-down.
     *  Usages: Road
     */
    SPECIAL;
}
