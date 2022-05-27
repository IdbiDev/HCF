package me.idbi.hcf.adminsystem;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;

public class adminMain {
    private static final Connection con = Main.getConnection("adminSystem");
    public static void setAdminDuty(Player player, boolean state){
        if(state){
            player.setGameMode(GameMode.CREATIVE);
            playertools.setMetadata(player,"adminDuty",true);
            //Todo: Dutyba léptél
            player.sendMessage("Beléptél");
        }else{
            player.setGameMode(GameMode.SURVIVAL);
            //Todo: Kiléptél dutyból
            playertools.setMetadata(player,"adminDuty",false);
            player.sendMessage("Kiléptél");
        }
    }

    public static void Deposit(Player admin,String faction,int amount){
        if(!Main.nameToFaction.containsKey(faction)){
            //Todo: Nem található faction
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(amount <= 0){
            //Todo: Valid number
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance -= amount;
        //Todo: Broadcast the loss
        playertools.BroadcastFaction(faction,"RETARD");
    }

    public static void Withdraw(Player admin,String faction,int amount){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            //Todo: Nem található faction
            return;
        }
        if(amount <= 0){
            //Todo: Valid number
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance += amount;
        //Todo: Broadcast the money
        playertools.BroadcastFaction(faction,"RETARD");
    }

    public static void SetFactionname(Player admin,String faction,String newName){
        if(!Main.nameToFaction.containsKey(faction)){
            //Todo: Nem található faction
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(newName.length() > 5){
            //Todo: Valid number
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.factioname = newName;
        //Todo: Broadcast the change
        playertools.BroadcastFaction(faction,"RETARD");
    }
    public static void GiveMoney(Player admin,String target,int amount){
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            //Todo: Nem található player
            return;
        }
        if(amount <= 0){
            //Todo: Valid number
            return;
        }
        Player player = Bukkit.getPlayer(target);
        playertools.setMetadata(player,"money",Integer.parseInt(playertools.getMetadata(player,"money")) + amount);
        //Todo: Broadcast the change
        player.sendMessage("Cica");
    }

    public static void TakeMoney(Player admin,String target,int amount){
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            //Todo: Nem található player
            return;
        }
        if(amount <= 0){
            //Todo: Valid number
            return;
        }
        Player player = Bukkit.getPlayer(target);
        playertools.setMetadata(player,"money",Integer.parseInt(playertools.getMetadata(player,"money")) - amount);
        //Todo: Broadcast the change
        player.sendMessage("Cica");
    }

    public static void DeleteFaction(Player admin,String faction){
        if(!Main.nameToFaction.containsKey(faction)){
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        //Todo: Broadcast the change
        playertools.BroadcastFaction(faction,"RETARD");

        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        Main.faction_cache.remove(selectedFaction.factionid);
        Main.nameToFaction.remove(selectedFaction.factioname);
        Main.factionToname.remove(selectedFaction.factionid);
        SQL_Connection.dbExecute(con,"DELETE FROM ranks WHERE faction='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con,"DELETE FROM factions WHERE ID='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con,"DELETE FROM claims WHERE factionid='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con,"UPDATE members SET rank='Nincs',faction=0,factionname='Nincs' WHERE faction='?'", String.valueOf(selectedFaction.factionid));


    }
    public static void setFactionLeader(Player admin,String faction,String Leader){
        if(!Main.nameToFaction.containsKey(faction)){
            //Todo: Nem található faction
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(Bukkit.getPlayer(Leader) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            //Todo: Nem található player
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.leader = Bukkit.getPlayer(Leader).getUniqueId().toString();
        SQL_Connection.dbExecute(con,"UPDATE factions SET leader='?' WHERE name='?'", selectedFaction.leader,faction);
        //Todo: Broadcast the change
        playertools.BroadcastFaction(faction,"RETARD");
    }
    public static void setPlayerFaction(Player admin,String target,String faction){
        if(!Main.nameToFaction.containsKey(faction)){
            //Todo: Nem található faction
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            //Todo: Nem található player
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
        playertools.BroadcastFaction(selectedFaction.factioname, Messages.BC_JOIN_MESSAGE.repPlayer(p).queue());
    }
    public static void kickPlayerFromFaction(Player admin,String target,String faction){
        if(!Main.nameToFaction.containsKey(faction)){
            //Todo: Nem található faction
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            //Todo: Nem található player
            return;
        }
        Player p = Bukkit.getPlayer(target);
        playertools.setMetadata(p,"factionid","0");
        playertools.setMetadata(p,"faction","Nincs");
        SQL_Connection.dbExecute(con,"UPDATE members SET faction=0,factionname='Nincs' WHERE uuid='?'",p.getUniqueId().toString());

    }

    public static void FreezePlayer(Player admin,String target){
        if(Bukkit.getPlayer(target) == null){
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            //Todo: Nem található player
            return;
        }
        Player p = Bukkit.getPlayer(target);
        boolean state = Boolean.parseBoolean(playertools.getMetadata(p,"freeze"));
        // Freeze
        if(!state){
            p.setWalkSpeed(0);
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 255),true);
            //Todo: You are frozen IDK
            p.sendMessage();
        }else{
            //UnFreeze
            p.removePotionEffect(PotionEffectType.JUMP);
            p.setWalkSpeed(0.2f);
            //Todo: You are unfrozen IDK
            p.sendMessage();
        }
    }

}
