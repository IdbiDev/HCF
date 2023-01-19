package me.idbi.hcf.tools.Objects;

import me.idbi.hcf.tools.SQL_Connection;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static me.idbi.hcf.commands.cmdFunctions.Faction_Create.con;

public class PlayerStatistic {
    public long TotalBardClassTime;
    public long TotalAssassinClassTime;
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
    public PlayerStatistic(JSONObject mainJSON){
        //FUCK ME
        //System.out.println(mainJSON.toString());
        JSONObject ClassTimes = mainJSON.getJSONObject("ClassTimes");
        TotalBardClassTime = Long.parseLong(ClassTimes.get("Bard").toString());
        TotalAssassinClassTime = Long.parseLong(ClassTimes.get("Assassin").toString());
        TotalArcherClassTime = Long.parseLong(ClassTimes.get("Archer").toString());
        TotalMinerClassTime = Long.parseLong(ClassTimes.get("Miner").toString());
        TotalClassTime = Long.parseLong(ClassTimes.get("Total").toString());
        JSONArray array = mainJSON.getJSONArray("FactionHistory");
        if(array.length() > 0){
            for(int x = 0;x<=array.length()-1;x++) {
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
        startDate = Long.parseLong(String.valueOf(mainJSON.get("startDate")));
        lastLogin = Long.parseLong(String.valueOf(mainJSON.get("lastLogin")));
        TimePlayed = Long.parseLong(String.valueOf(mainJSON.get("TimePlayed")));
    }

    public void Save(Player p){
        JSONObject jsonComp = new JSONObject();
        JSONArray factions = new JSONArray();
        //JSONArray ClassTimes = new JSONArray();
        JSONObject classTimes = new JSONObject();
        classTimes.put("Bard",TotalBardClassTime);
        classTimes.put("Assassin",TotalAssassinClassTime);
        classTimes.put("Archer",TotalArcherClassTime);
        classTimes.put("Miner",TotalMinerClassTime);
        classTimes.put("Total",TotalMinerClassTime+TotalArcherClassTime+TotalAssassinClassTime+TotalBardClassTime);

        jsonComp.put("totalFactions", factionHistory.size());
        jsonComp.put("MoneySpend", MoneySpend);
        jsonComp.put("MoneyEarned", MoneyEarned);
        jsonComp.put("TimePlayed", TimePlayed);
        jsonComp.put("startDate", startDate);
        jsonComp.put("lastLogin",new Date().getTime());
        for (FactionHistory f: factionHistory) {
            factions.put(f.toJSON());
        }
        jsonComp.put("FactionHistory", factions);
        jsonComp.put("ClassTimes", classTimes);
        SQL_Connection.dbExecute(con,"UPDATE members SET statistics='?' WHERE uuid='?'",jsonComp.toString(),p.getUniqueId().toString());
    }
}
