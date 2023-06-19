package me.idbi.hcf.Tools.Database.MongoDB;

public class MongoFields {

    public enum FactionsFields {
        ID("ID"),
        ALLIES("allies"),
        HOME("home"),
        BALANCE("balance"),
        NAME("name"),
        POINTS("points"),
        LEADER("leader"),
        STATISTICS("statistics");

        public String value;

        FactionsFields(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }
    }

    public enum MembersFields {
        ID("ID"),
        NAME("name"),
        FACTION("faction"),
        RANK("rank"),
        KILLS("kills"),
        DEATHS("deaths"),
        MONEY("money"),
        LIVES("lives"),
        UUID("uuid"),
        LANGUAGE("language"),
        STATISTICS("statistics");

        public String value;

        MembersFields(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }
    }

    public enum ClaimsFields {
        ID("ID"),
        FACTIONID("factionid"),
        STARTX("startX"),
        STARTZ("startZ"),
        ENDX("endX"),
        ENDZ("endZ"),
        TYPE("type"),
        WORLD("world");

        public String value;

        ClaimsFields(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }
    }

    public enum DeathbansFields {
        ID("ID"),
        UUID("uuid"),
        TIME("time");

        public String value;

        DeathbansFields(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }
    }

    public enum RanksFields {
        ID("ID"),
        FACTION("faction"),
        NAME("name"),
        ISDEFAULT("isDefault"),
        ISLEADER("isLeader"),
        @Deprecated PERMISSION_ALL("ALL_Permission"),
        @Deprecated PERMISSION_MONEY("MONEY_Permission"),
        @Deprecated PERMISSION_INVITE("INVITE_Permission"),
        @Deprecated PERMISSION_RANK("RANK_Permission"),
        @Deprecated PERMISSION_KICK("KICK_Permission"),
        PERMISSIONS("permissions"),
        PRIORITY("priority");

        public String value;

        RanksFields(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }
    }
}
