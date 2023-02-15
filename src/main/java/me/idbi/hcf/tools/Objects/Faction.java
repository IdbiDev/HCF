package me.idbi.hcf.tools.Objects;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.*;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.idbi.hcf.Main.max_allies_pro_faction;
import static me.idbi.hcf.Main.max_members_pro_faction;
import static me.idbi.hcf.commands.cmdFunctions.Faction_Create.con;

public class Faction {

    public int id;
    public String name;
    public String leader;
    public int balance;
    public ArrayList<HCF_Claiming.Faction_Claim> claims = new ArrayList<>();
    public double DTR = 0;
    public double DTR_MAX = 0;
    public long DTR_TIMEOUT = 0L;
    public inviteManager.factionInvite invites;
    public allyManager allyinvites;

    public Location homeLocation;

    public ArrayList<Faction_Rank_Manager.Rank> ranks = new ArrayList<>();
    public ArrayList<HCFPlayer> members = new ArrayList<>();
    //Statistics
    public ArrayList<HistoryEntrys.BalanceEntry> balanceHistory = new ArrayList<>();
    public ArrayList<HistoryEntrys.KickEntry> kickHistory = new ArrayList<>();
    public ArrayList<HistoryEntrys.JoinLeftEntry> joinLeftHistory = new ArrayList<>();
    public ArrayList<HistoryEntrys.FactionJoinLeftEntry> factionjoinLeftHistory = new ArrayList<>();
    public ArrayList<HistoryEntrys.InviteEntry> inviteHistory = new ArrayList<>();
    public ArrayList<HistoryEntrys.RankEntry> rankCreateHistory = new ArrayList<>();
    public HashMap<Integer,AllyFaction> Allies = new HashMap<>();


    public Faction(int id, String name, String leader, int balance) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.invites = new inviteManager.factionInvite();
        this.allyinvites = new allyManager();
        this.balance = balance;

    }
    public boolean isPlayerInFaction(Player p){
        return members.contains(HCFPlayer.getPlayer(p));
    }
    public void loadFactionHistory(JSONObject mainJSON){
        try{
            JSONArray balance_array = mainJSON.getJSONArray("balanceHistory");
            JSONArray kick_array = mainJSON.getJSONArray("kickHistory");
            JSONArray join_array = mainJSON.getJSONArray("joinLeftHistory");
            JSONArray factionjoin_array = mainJSON.getJSONArray("factionjoinLeftHistory");
            JSONArray invite_array = mainJSON.getJSONArray("inviteHistory");
            JSONArray rank_array = mainJSON.getJSONArray("rankCreateHistory");
            if(balance_array.length() > 0){
                for(int x = 0;x<=balance_array.length()-1;x++) {
                    //System.out.println("Balance on load: " + balance_array.getJSONObject(x));
                    if(x >= 50){
                        balanceHistory.remove(balanceHistory.size() - 1);
                    }

                    JSONObject obj = balance_array.getJSONObject(x);
                    balanceHistory.add(new HistoryEntrys.BalanceEntry(
                            obj.getInt("amount"),
                            obj.getString("player"),
                            obj.getLong("time")
                    ));
                }
            }
            if(kick_array.length() > 0){
                for(int x = 0;x<=kick_array.length()-1;x++) {
                    if(x >= 50){
                        kick_array.remove(kickHistory.size() - 1);
                    }
                    JSONObject obj = kick_array.getJSONObject(x);
                    kickHistory.add(new HistoryEntrys.KickEntry(
                            obj.getString("player"),
                            obj.getString("executor"),
                            obj.getLong("time"),
                            obj.getString("reason")
                    ));
                }
            }
            if(factionjoin_array.length() > 0){
                for(int x = 0;x<=factionjoin_array.length()-1;x++) {
                    if(x >= 50){
                        factionjoinLeftHistory.remove(factionjoinLeftHistory.size() - 1);
                    }
                    JSONObject obj = factionjoin_array.getJSONObject(x);
                    factionjoinLeftHistory.add( new HistoryEntrys.FactionJoinLeftEntry(
                            obj.getString("player"),
                            obj.getString("type"),
                            obj.getLong("time")
                    ));
                }
            }
            if(join_array.length() > 0){
                for(int x = 0;x<=join_array.length()-1;x++) {
                    if(x >= 50){
                        joinLeftHistory.remove(joinLeftHistory.size() - 1);
                    }
                    JSONObject obj = join_array.getJSONObject(x);
                    joinLeftHistory.add(new HistoryEntrys.JoinLeftEntry(
                            obj.getString("player"),
                            obj.getBoolean("isjoined"),
                            obj.getLong("time")
                    ));
                }
            }
            if(invite_array.length() > 0){
                for(int x = 0;x<=invite_array.length()-1;x++) {
                    if(x >= 50){
                        inviteHistory.remove(inviteHistory.size() - 1);
                    }
                    JSONObject obj = invite_array.getJSONObject(x);
                    inviteHistory.add(new HistoryEntrys.InviteEntry(
                            obj.getString("executor"),
                            obj.getString("player"),
                            obj.getLong("time"),
                            obj.getBoolean("isinvited")
                    ));
                }
            }
            if(rank_array.length() > 0){
                for(int x = 0;x<=rank_array.length()-1;x++) {
                    if(x >= 50){
                        rankCreateHistory.remove(rankCreateHistory.size() - 1);
                    }
                    JSONObject obj = rank_array.getJSONObject(x);
                    rankCreateHistory.add(new HistoryEntrys.RankEntry(
                            obj.getString("rank"),
                            obj.getString("player"),
                            obj.getLong("time"),
                            obj.getString("type")
                    ));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            loadFactionHistory(assembleFactionHistory());
        }

    }

    public JSONObject assembleFactionHistory(){
        JSONObject main = new JSONObject();
        JSONArray balanceHistory = new JSONArray();
        JSONArray kickHistory = new JSONArray();
        JSONArray joinleftHistory = new JSONArray();
        JSONArray factionJoinLeftHistory = new JSONArray();
        JSONArray inviteHistory = new JSONArray();
        JSONArray rankModifyHistory = new JSONArray();

        for (HistoryEntrys.BalanceEntry balanceEntry : this.balanceHistory) {
            JSONObject balance = new JSONObject();
            balance.put("amount",balanceEntry.amount);
            balance.put("player",balanceEntry.player);
            balance.put("time",balanceEntry.time);
            balanceHistory.put(balance);
            //System.out.println("Balance: " + balanceEntry.amount);
        }
        main.put("balanceHistory",balanceHistory);
        for (HistoryEntrys.InviteEntry InviteEntry : this.inviteHistory) {
            JSONObject invite = new JSONObject();
            invite.put("executor",InviteEntry.executor);
            invite.put("player",InviteEntry.player);
            invite.put("time",InviteEntry.time);
            invite.put("isinvited",InviteEntry.isInvited);
            inviteHistory.put(invite);
        }
        main.put("inviteHistory",inviteHistory);
        for (HistoryEntrys.KickEntry KickEntry : this.kickHistory) {
            JSONObject kick = new JSONObject();
            kick.put("executor",KickEntry.executor);
            kick.put("player",KickEntry.player);
            kick.put("time",KickEntry.time);
            kick.put("reason",KickEntry.reason);
            kickHistory.put(kick);
        }
        main.put("kickHistory",kickHistory);
        for (HistoryEntrys.JoinLeftEntry JoinEntry : this.joinLeftHistory) {
            JSONObject join = new JSONObject();
            join.put("player",JoinEntry.player);
            join.put("isjoined",JoinEntry.isJoined);
            join.put("time",JoinEntry.time);
            joinleftHistory.put(join);
        }
        main.put("joinLeftHistory",joinleftHistory);
        for (HistoryEntrys.FactionJoinLeftEntry FactionJoinEntry : this.factionjoinLeftHistory) {
            JSONObject fjoin = new JSONObject();
            fjoin.put("player",FactionJoinEntry.player);
            fjoin.put("type",FactionJoinEntry.type);
            fjoin.put("time",FactionJoinEntry.time);
            factionJoinLeftHistory.put(fjoin);
        }
        main.put("factionjoinLeftHistory",factionJoinLeftHistory);
        for (HistoryEntrys.RankEntry rankEntry : this.rankCreateHistory) {
            JSONObject rank = new JSONObject();
            rank.put("player",rankEntry.player);
            rank.put("type",rankEntry.type);
            rank.put("time",rankEntry.time);
            rank.put("rank",rankEntry.rank);
            rankModifyHistory.put(rank);
        }
        main.put("rankCreateHistory",rankModifyHistory);
        return main;
    }
    public void saveFactionData(){
        String AlliesEntry = AllyTools.getAlliesJson(this);
        SQL_Connection.dbExecute(con,"UPDATE factions SET name='?',money='?',leader='?',`statistics`='?',Allies='?' WHERE ID='?'", name, String.valueOf(balance),leader,assembleFactionHistory().toString(),AlliesEntry,String.valueOf(id));
    }
    public void invitePlayer(Player p) {
        if(getMemberCount() + 1 > max_members_pro_faction || invites.getInvitedPlayers().size()+1 > HCF_Rules.maxInvitesPerFaction ) {
            p.sendMessage(Messages.max_members_reached.language(p).queue());
            return;
        }
        invites.invitePlayerToFaction(p);
    }

    public Faction_Rank_Manager.Rank getPlayerRank(Player p) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);

        return hcfPlayer.rank;
    }

    public int getMemberCount() {
        return this.members.size();
    }

    public void unInvitePlayer(Player p) {
        invites.removePlayerFromInvite(p);
    }

    public boolean isPlayerInvited(Player p) {
        return invites.isPlayerInvited(p);
    }

    public void unInviteAlly(Faction faction) {
        allyinvites.removePlayerFromInvite(faction);
    }

    public boolean isFactionAllyInvited(Faction faction) {
        return allyinvites.isFactionInvited(faction);
    }
    public void inviteFactionAlly(Faction ally) {
        if(Allies.size() + 1 > max_allies_pro_faction || allyinvites.getInvitedAllies().size()+1 > HCF_Rules.maxAlliesPerFaction ) {
            return;
        }
        allyinvites.inviteFactionToAlly(ally);
        Main.sendCmdMessage("Invite requested! Requester: " + this.name + " Receiver: " + ally.name);
    }
    public void resolveFactionAlly(Faction ally) {
        Allies.remove(ally.id);
        ally.Allies.remove(this.id);

        // Allies.removeIf(allyFaction -> allyFaction.getAllyFaction().id == ally.id);
    }

    public void addClaim(HCF_Claiming.Faction_Claim claimid) {
        try {
            claims.add(claimid);
        } catch (NullPointerException ex) {
            claims = new ArrayList<>();
            claims.add(claimid);
            ex.printStackTrace();
        }
    }
    public HCF_Claiming.Faction_Claim getFactionClaim(Integer id) {
        return claims.get(id);
    }

    public Faction_Rank_Manager.Rank getRank(String name) {
        for (Faction_Rank_Manager.Rank rank : ranks) {
            if (name.equalsIgnoreCase(rank.name)) {
                return rank;
            }
        }
        //NameChanger.refresh(p);
        return getDefaultRank();
    }

    public void ApplyPlayerRank(OfflinePlayer p, String name) {
        for (Faction_Rank_Manager.Rank rank : ranks) {
            if (Objects.equals(rank.name, name)) {
                SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE uuid='?'", rank.name, p.getUniqueId().toString());
                break;
            }
        }
    }

    public Faction_Rank_Manager.Rank FindRankByName(String name) {
        // Bukkit.broadcastMessage(name);
        //Bukkit.broadcastMessage("Meow " + name.toCharArray().length);
        for (Faction_Rank_Manager.Rank rank : ranks) {
            if (rank.name.equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }


    public Faction_Rank_Manager.Rank getDefaultRank() {
        for (Faction_Rank_Manager.Rank rank : ranks) {
            if (rank.isDefault)
                return rank;
        }
        return null;
    }

    public Faction_Rank_Manager.Rank getLeaderRank() {
        for (Faction_Rank_Manager.Rank rank : ranks) {
            if (rank.isLeader)
                return rank;
        }
        return null;
    }

    public void setHomeLocation(Location loc) {
        homeLocation = loc;
    }

    public void BroadcastFaction(Messages message) {
        ArrayList<Player> members = playertools.getFactionOnlineMembers(this);
        for (Player member : members) {
            member.sendMessage(message.queue());
        }
    }

    public void BroadcastFaction(String message) {
        ArrayList<Player> members = playertools.getFactionOnlineMembers(this);
        for (Player member : members) {
            member.sendMessage(message);
        }
    }

/*    public int getOnlineSize() {
        int counts = 0;
        for (Player member : this.getMembers()) {
            if(member == null) continue;
            if(!member.isOnline()) continue;

            counts++;
        }

        return counts;
    }*/

    public ArrayList<Player> getMembers() {
        return playertools.getFactionOnlineMembers(this);
    }

    public void PlayerBroadcast(String message) {
        ArrayList<Player> members = playertools.getFactionOnlineMembers(this);
        for (Player member : members) {
            member.sendMessage();
        }
    }

    public int getKills(){
        int total = 0;
        for(HCFPlayer members : this.members){
            total += members.getKills();
        }
        return total;
    }
    public int getDeaths() {
        int total = 0;
        for(HCFPlayer members : this.members){
            total += members.getDeaths();
        }
        return total;
    }
    public void refreshDTR(){
        this.DTR_MAX = Double.parseDouble(playertools.CalculateDTR(this));
    }

    public void setupAllies() {
        HashMap<String, Object> permissionmap = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE ID='?'", String.valueOf(this.id));
        //        //(String) permissionmap.get("Allies")
        JSONObject obj = new JSONObject((String) permissionmap.get("Allies"));
        System.out.println(obj);
        // ID       PermissionMap
        Map<String, Object> map = JsonUtils.jsonToMap(obj);

        for (Map.Entry<String, Object> hash : map.entrySet()) {
            if(!hash.getKey().matches("^[0-9]+$")) continue;

            Map<String, Object> jsonPerms = JsonUtils.jsonToMap(new JSONObject(hash.getValue()));
            Map<Permissions, Boolean> perms = new HashMap<>();

            for (Map.Entry<String, Object> permsJson : jsonPerms.entrySet()) {
                if(Permissions.getByName(permsJson.getKey()) == null) continue;
                perms.put(Permissions.getByName(permsJson.getKey()), Boolean.parseBoolean(permsJson.getValue() + ""));
            }

            int id = Integer.parseInt(hash.getKey());
            AllyFaction allyFaction = new AllyFaction(id, Main.faction_cache.get(id));

            for (Map.Entry<Permissions, Boolean> permsHash : perms.entrySet()) {
                allyFaction.setPermission(permsHash.getKey(), permsHash.getValue());
            }
            this.Allies.put(id,allyFaction);
        }
    }

    public boolean HaveAllyPermission(Faction faction, Permissions perm){

//        //if(faction == null) return true;
//        if(faction.claims.isEmpty()) return true;
//        if(this.Allies.size() == 0) return false;
//        if(faction.DTR <= 0) return true;
        if(faction == null) return true;
       for(AllyFaction allyFaction : this.Allies.values()){
            return (allyFaction.getAllyFaction().id == faction.id) && allyFaction.hasPermission(perm);
       }
       return true;
    }
    public boolean isAlly(Faction faction){
        for(AllyFaction allyFaction : this.Allies.values()) {
            if(allyFaction.getFactionId() == faction.id){
                return true;
            }
        }
        return false;
    }

    public void selfDestruct() {
        Main.faction_cache.remove(this.id);
        Main.nameToFaction.remove(this.name);
        SQL_Connection.dbExecute(con, "DELETE FROM ranks WHERE faction='?'", String.valueOf(this.id));
        SQL_Connection.dbExecute(con, "DELETE FROM factions WHERE ID='?'", String.valueOf(this.id));
        SQL_Connection.dbExecute(con, "DELETE FROM claims WHERE factionid='?'", String.valueOf(this.id));
        SQL_Connection.dbExecute(con, "UPDATE members SET rank='None',faction=0 WHERE faction='?'", String.valueOf(this.id));
    }
}
