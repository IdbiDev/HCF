package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import me.idbi.hcf.Tools.Playertools;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class PlayerStatistic {
    private static final Connection con = Playertools.con;
    public final static String defaultStats = "{\"totalFactions\":0,\"lastLogin\":0,\"TimePlayed\":0,\"MoneySpend\":0,\"ClassTimes\":{\"Miner\":0,\"Archer\":0,\"Rogue\":0,\"Total\":0,\"Bard\":0},\"MoneyEarned\":0,\"FactionHistory\":[],\"startDate\":0}";
    public long TotalBardClassTime;
    public long TotalRogueClassTime;
    public long TotalArcherClassTime;
    public long TotalMinerClassTime;
    public long TotalClassTime;
    public int MoneySpend;
    public int MoneyEarned;
    public long TimePlayed;
    public long startDate;
    public long lastLogin;
    public int kills;
    public int deaths;

    public ArrayList<FactionHistory> factionHistory = new ArrayList<>();

    public PlayerStatistic(JSONObject mainJSON) {
        //FUCK ME
        //System.out.println(mainJSON.toString());
        JSONObject ClassTimes = mainJSON.getJSONObject("ClassTimes");
        TotalBardClassTime = Long.parseLong(ClassTimes.get("Bard").toString());
        TotalRogueClassTime = Long.parseLong(ClassTimes.get("Rogue").toString());
        TotalArcherClassTime = Long.parseLong(ClassTimes.get("Archer").toString());
        TotalMinerClassTime = Long.parseLong(ClassTimes.get("Miner").toString());
        TotalClassTime = Long.parseLong(ClassTimes.get("Total").toString());
        JSONArray array = mainJSON.getJSONArray("FactionHistory");
        if (array.length() > 0) {
            for (int x = 0; x <= array.length() - 1; x++) {
                JSONObject obj = array.getJSONObject(x);
                factionHistory.add(new FactionHistory(
                        obj.getLong("joined"),
                        obj.getLong("left"),
                        obj.getString("cause"),
                        obj.getString("name"),
                        obj.getString("lastrole"),
                        obj.getInt("id")
                ));
            }
        }
        MoneySpend = Integer.parseInt(String.valueOf(mainJSON.get("MoneySpend")));
        MoneyEarned = Integer.parseInt(String.valueOf(mainJSON.get("MoneyEarned")));
        startDate = Long.parseLong(String.valueOf(mainJSON.get("startDate"))) == 0 ? new Date().getTime() : Long.parseLong(String.valueOf(mainJSON.get("startDate")));
        lastLogin = Long.parseLong(String.valueOf(mainJSON.get("lastLogin"))) == 0 ? new Date().getTime() : Long.parseLong(String.valueOf(mainJSON.get("lastLogin")));
        TimePlayed = Long.parseLong(String.valueOf(mainJSON.get("TimePlayed")));
    }

    public void save(UUID uuid) {
        JSONObject jsonComp = new JSONObject();
        JSONArray factions = new JSONArray();
        //JSONArray ClassTimes = new JSONArray();
        JSONObject classTimes = new JSONObject();
        classTimes.put("Bard", TotalBardClassTime);
        classTimes.put("Rogue", TotalRogueClassTime);
        classTimes.put("Archer", TotalArcherClassTime);
        classTimes.put("Miner", TotalMinerClassTime);
        classTimes.put("Total", TotalMinerClassTime + TotalArcherClassTime + TotalRogueClassTime + TotalBardClassTime);

        jsonComp.put("totalFactions", factionHistory.size());
        jsonComp.put("MoneySpend", MoneySpend);
        jsonComp.put("MoneyEarned", MoneyEarned);
        jsonComp.put("TimePlayed", TimePlayed);
        jsonComp.put("startDate", startDate);
        jsonComp.put("lastLogin", new Date().getTime());
        for (FactionHistory f : factionHistory) {
            factions.put(f.toJSON());
        }
        jsonComp.put("FactionHistory", factions);
        jsonComp.put("ClassTimes", classTimes);
        if(Main.isUsingMongoDB()) {
            Bson where = eq("uuid", uuid.toString());
            Bson updates = set("statistics",jsonComp.toString());
            MongoDBDriver.Update("members", where, updates);
        }else {
            SQL_Connection.dbExecute(con, "UPDATE members SET statistics='?' WHERE uuid='?'", jsonComp.toString(), uuid.toString());
        }
    }
    public void saveSync(UUID uuid) {
        JSONObject jsonComp = new JSONObject();
        JSONArray factions = new JSONArray();
        //JSONArray ClassTimes = new JSONArray();
        JSONObject classTimes = new JSONObject();
        classTimes.put("Bard", TotalBardClassTime);
        classTimes.put("Rogue", TotalRogueClassTime);
        classTimes.put("Archer", TotalArcherClassTime);
        classTimes.put("Miner", TotalMinerClassTime);
        classTimes.put("Total", TotalMinerClassTime + TotalArcherClassTime + TotalRogueClassTime + TotalBardClassTime);

        jsonComp.put("totalFactions", factionHistory.size());
        jsonComp.put("MoneySpend", MoneySpend);
        jsonComp.put("MoneyEarned", MoneyEarned);
        jsonComp.put("TimePlayed", TimePlayed);
        jsonComp.put("startDate", startDate);
        jsonComp.put("lastLogin", new Date().getTime());
        for (FactionHistory f : factionHistory) {
            factions.put(f.toJSON());
        }
        jsonComp.put("FactionHistory", factions);
        jsonComp.put("ClassTimes", classTimes);
        if(Main.isUsingMongoDB()) {
            Bson where = eq("uuid", uuid.toString());
            Bson updates = set("statistics",jsonComp.toString());
            MongoDBDriver.Update("members", where, updates);
        }else {
            SQL_Connection.dbSyncExec(con, "UPDATE members SET statistics='?' WHERE uuid='?'", jsonComp.toString(), uuid.toString());
        }
    }
}
