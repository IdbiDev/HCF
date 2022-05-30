package me.idbi.hcf.adminsystem;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class adminMain {
    private static final Connection con = Main.getConnection("adminSystem");
    public static void setAdminDuty(Player player, boolean state){
        if(state){
            player.setGameMode(GameMode.CREATIVE);
            playertools.setMetadata(player,"adminDuty",true);

            player.sendMessage(Messages.ADMIN_DUTY_ON.queue());
        }else{
            player.setGameMode(GameMode.SURVIVAL);

            playertools.setMetadata(player,"adminDuty",false);
            player.sendMessage(Messages.ADMIN_DUTY_OFF.queue());
        }
    }

    public static void Deposit(Player admin,String faction,int amount){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(amount <= 0){
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance += amount;
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.FACTION_ADMIN_DEPOSIT_BC.repExecutor(admin).queue());
    }

    public static void Withdraw(Player admin,String faction,int amount){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(amount <= 0){
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance -= amount;
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.FACTION_ADMIN_WITHDRAW_BC.repExecutor(admin).queue());
    }

    public static void SetFactionname(Player admin,String faction,String newName){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(newName.length() > 5){
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.factioname = newName;
        //Todo: Broadcast the change
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.SET_FACTION_NAME.repExecutor(admin).setFaction(newName).queue());
        Scoreboards.RefreshAll();
    }
    public static void GiveMoney(Player admin,String target,int amount){
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        if(amount <= 0){
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Player player = Bukkit.getPlayer(target);
        playertools.setMetadata(player,"money",Integer.parseInt(playertools.getMetadata(player,"money")) + amount);

        player.sendMessage(Messages.GIVE_MONEY.repExecutor(admin).setAmount(String.valueOf(amount)).queue());
        Scoreboards.refresh(player);
    }

    public static void TakeMoney(Player admin,String target,int amount){
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        if(amount <= 0){
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Player player = Bukkit.getPlayer(target);
        playertools.setMetadata(player,"money",Integer.parseInt(playertools.getMetadata(player,"money")) - amount);

        player.sendMessage(Messages.TAKE_MONEY.repExecutor(admin).setAmount(String.valueOf(amount)).queue());
        Scoreboards.refresh(player);
    }

    public static void DeleteFaction(Player admin,String faction){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }

        Main.Faction selectedFaction = Main.nameToFaction.get(faction);

        Main.faction_cache.remove(selectedFaction.factionid);
        Main.nameToFaction.remove(selectedFaction.factioname);
        Main.factionToname.remove(selectedFaction.factionid);
        SQL_Connection.dbExecute(con,"DELETE FROM ranks WHERE faction='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con,"DELETE FROM factions WHERE ID='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con,"DELETE FROM claims WHERE factionid='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con,"UPDATE members SET rank='Nincs',faction=0,factionname='Nincs' WHERE faction='?'", String.valueOf(selectedFaction.factionid));
        for(Player player :playertools.getFactionOnlineMembers(faction)){
            playertools.setMetadata(player,"faction","Nincs");
            playertools.setMetadata(player,"factionid","0");
            playertools.setMetadata(player,"rank","none");

        }

        Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(admin).setFaction(selectedFaction.factioname).queue());
    }
    public static void setFactionLeader(Player admin,String faction,String Leader){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(Bukkit.getPlayer(Leader) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.leader = Bukkit.getPlayer(Leader).getUniqueId().toString();
        SQL_Connection.dbExecute(con,"UPDATE factions SET leader='?' WHERE name='?'", selectedFaction.leader,faction);
        //Todo: Broadcast the change
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.SET_FACTION_LEADER_BY_ADMIN.repPlayer(Bukkit.getPlayer(Leader)).repExecutor(admin).queue());
    }
    public static void setPlayerFaction(Player admin,String target,String faction){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        playertools.setMetadata(p,"factionid",selectedFaction.factionid);
        playertools.setMetadata(p,"faction",selectedFaction.factioname);
        SQL_Connection.dbExecute(con,"UPDATE members SET faction='?',factionname='?' WHERE uuid='?'", String.valueOf(selectedFaction.factionid),selectedFaction.factioname,p.getUniqueId().toString());

        //Sikeres belépés
        p.sendMessage(Messages.JOIN_MESSAGE.queue());

        //Faction -> xy belépett
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.BC_JOIN_MESSAGE.repPlayer(p).queue());
        Scoreboards.refresh(p);
    }
    public static void kickPlayerFromFaction(Player admin,String target,String faction){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        playertools.setMetadata(p,"factionid","0");
        playertools.setMetadata(p,"faction","Nincs");
        SQL_Connection.dbExecute(con,"UPDATE members SET faction=0,factionname='Nincs' WHERE uuid='?'",p.getUniqueId().toString());
        Scoreboards.refresh(p);

    }

    public static void FreezePlayer(Player admin,String target){
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        boolean state = Boolean.parseBoolean(playertools.getMetadata(p,"freeze"));
        // Freeze
        if(!state){
            admin.sendMessage(Messages.FREEZE_EXECUTOR_ON.repPlayer(p).queue());
            p.sendMessage(Messages.FREEZE_PLAYER_ON.repExecutor(admin).queue());
        }else{
            //UnFreeze
            admin.sendMessage(Messages.FREEZE_EXECUTOR_OFF.repPlayer(p).queue());
            p.sendMessage(Messages.FREEZE_PLAYER_OFF.repExecutor(admin).queue());
        }
        playertools.setMetadata(p,"freeze",!state);
    }

}
