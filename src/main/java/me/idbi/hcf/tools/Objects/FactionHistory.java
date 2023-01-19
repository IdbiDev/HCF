package me.idbi.hcf.tools.Objects;

import org.json.JSONObject;

import java.util.Date;

public class FactionHistory {
        public final Date joined;
        public Date left;
        public String cause;
        public String name;
        public String lastRole;
        public final int id;
        public FactionHistory(long joined,long left,String cause,String name,String lastRole,int id) {
            this.joined = new Date(joined);
            this.left = new Date(left);
            this.cause = cause;
            this.name = name;
            this.lastRole = lastRole;
            this.id = id;
        }
        public JSONObject toJSON(){
            JSONObject faction = new JSONObject();
            faction.put("name",name);
            faction.put("lastrole",lastRole);
            faction.put("joined",joined.getTime());
            faction.put("left",left.getTime());
            faction.put("cause",cause);
            faction.put("id",id);
            return faction;
        }
    
}
