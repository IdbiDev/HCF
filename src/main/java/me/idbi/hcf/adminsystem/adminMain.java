package me.idbi.hcf.adminsystem;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;

public class adminMain {
    private static final Connection con = Main.getConnection("adminSystem");

    public static void setAdminDuty(Player player, boolean state) {
        if (state) {
            player.setGameMode(GameMode.CREATIVE);
            playertools.setMetadata(player, "adminDuty", true);

            player.sendMessage(Messages.ADMIN_DUTY_ON.queue());
        } else {
            player.setGameMode(GameMode.SURVIVAL);

            playertools.setMetadata(player, "adminDuty", false);
            player.sendMessage(Messages.ADMIN_DUTY_OFF.queue());
        }
    }

    public static void Deposit(Player admin, String faction, int amount) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance += amount;
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.FACTION_ADMIN_DEPOSIT_BC.repExecutor(admin).queue());
    }

    public static void Withdraw(Player admin, String faction, int amount) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance -= amount;
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.FACTION_ADMIN_WITHDRAW_BC.repExecutor(admin).queue());
    }

    public static void SetFactionname(Player admin, String faction, String newName) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if (newName.length() > 5) {
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.name = newName;
        //Todo: Broadcast the change
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.SET_FACTION_NAME.repExecutor(admin).setFaction(newName).queue());
        Scoreboards.RefreshAll();
    }

    public static void GiveMoney(Player admin, String target, int amount) {
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Player player = Bukkit.getPlayer(target);
        playertools.setMetadata(player, "money", Integer.parseInt(playertools.getMetadata(player, "money")) + amount);

        player.sendMessage(Messages.GIVE_MONEY.repExecutor(admin).setAmount(String.valueOf(amount)).queue());
        Scoreboards.refresh(player);
        Main.PlayerStatistic stat = Main.playerStatistics.get(player);
        stat.MoneyEarned += amount; 
        Main.playerStatistics.put(player,stat);
    }

    public static void TakeMoney(Player admin, String target, int amount) {
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.NOT_A_NUMBER.queue());
            return;
        }
        Player player = Bukkit.getPlayer(target);
        playertools.setMetadata(player, "money", Integer.parseInt(playertools.getMetadata(player, "money")) - amount);

        player.sendMessage(Messages.TAKE_MONEY.repExecutor(admin).setAmount(String.valueOf(amount)).queue());
        Scoreboards.refresh(player);
        Main.PlayerStatistic stat = Main.playerStatistics.get(player);
        stat.MoneySpend += amount;
        Main.playerStatistics.put(player,stat);
    }

    public static void DeleteFaction(Player admin, String faction) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }

        Main.Faction selectedFaction = Main.nameToFaction.get(faction);

        Main.faction_cache.remove(selectedFaction.id);
        Main.nameToFaction.remove(selectedFaction.name);
        Main.factionToname.remove(selectedFaction.id);
        SQL_Connection.dbExecute(con, "DELETE FROM ranks WHERE faction='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "DELETE FROM factions WHERE ID='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "DELETE FROM claims WHERE factionid='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "UPDATE members SET rank='None',faction=0,factionname='None' WHERE faction='?'", String.valueOf(selectedFaction.id));
        for (Player player : playertools.getFactionOnlineMembers(faction)) {
            playertools.setMetadata(player, "faction", "None");
            playertools.setMetadata(player, "factionid", "0");
            playertools.setMetadata(player, "rank", "none");
            Main.PlayerStatistic stat = Main.playerStatistics.get(player);
            for(Main.FactionHistory statF : stat.factionHistory){
                if(statF.id == selectedFaction.id){
                    statF.left = new Date();
                    statF.cause = "Deleted";
                    statF.lastRole = selectedFaction.player_ranks.get(player).name;
                    statF.name = selectedFaction.name;
                }
            }
            Main.playerStatistics.put(player,stat);
        }

        Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(admin).setFaction(selectedFaction.name).queue());
    }

    public static void setFactionLeader(Player admin, String faction, String Leader) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if (Bukkit.getPlayer(Leader) == null) {
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.leader = Bukkit.getPlayer(Leader).getUniqueId().toString();
        SQL_Connection.dbExecute(con, "UPDATE factions SET leader='?' WHERE name='?'", selectedFaction.leader, faction);
        //Todo: Broadcast the change
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.SET_FACTION_LEADER_BY_ADMIN.repPlayer(Bukkit.getPlayer(Leader)).repExecutor(admin).queue());
        admin.sendMessage(Messages.ADMIN_SET_FACTION_NAME.setFaction(faction).queue());
    }

    public static void setPlayerFaction(Player admin, String target, String faction) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        Main.Faction selectedFaction = Main.nameToFaction.get(faction);
        playertools.setMetadata(p, "factionid", selectedFaction.id);
        playertools.setMetadata(p, "faction", selectedFaction.name);
        SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',factionname='?' WHERE uuid='?'", String.valueOf(selectedFaction.id), selectedFaction.name, p.getUniqueId().toString());

        //Sikeres belépés
        p.sendMessage(Messages.JOIN_MESSAGE.queue());

        //Faction -> xy belépett
        Main.Faction f = Main.nameToFaction.get(faction);
        f.BroadcastFaction(Messages.BC_JOIN_MESSAGE.repPlayer(p).queue());
        Scoreboards.refresh(p);
        admin.sendMessage(Messages.ADMIN_SET_PLAYERFACTION.setFaction(faction).repPlayer(p).queue());
    }

    public static void kickPlayerFromFaction(Player admin, String target, String faction) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        playertools.setMetadata(p, "factionid", "0");
        playertools.setMetadata(p, "faction", "None");
        SQL_Connection.dbExecute(con, "UPDATE members SET faction=0,factionname='None' WHERE uuid='?'", p.getUniqueId().toString());
        Scoreboards.refresh(p);


    }

    public static void FreezePlayer(Player admin, String target) {
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        boolean state = Boolean.parseBoolean(playertools.getMetadata(p, "freeze"));
        // Freeze
        if (!state) {
            admin.sendMessage(Messages.FREEZE_EXECUTOR_ON.repPlayer(p).queue());
            p.sendMessage(Messages.FREEZE_PLAYER_ON.repExecutor(admin).queue());
        } else {
            //UnFreeze
            admin.sendMessage(Messages.FREEZE_EXECUTOR_OFF.repPlayer(p).queue());
            p.sendMessage(Messages.FREEZE_PLAYER_OFF.repExecutor(admin).queue());
        }
        playertools.setMetadata(p, "freeze", !state);
    }

    public static void SpawnPlace(Player admin, String state) {
        if (state.equalsIgnoreCase("start")) {
            for (String lines : ListMessages.CLAIM_INFO_ADMIN.getMessageList()) {

                admin.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
            }

            playertools.setMetadata(admin, "spawnclaiming", true);
            HCF_Claiming.SpawnPrepare(admin);
        } else if (state.equalsIgnoreCase("claim")) {
            if (HCF_Claiming.ForceFinishClaim(1,admin,"protected")) {
                //Todo: Kurvva sikerült
                playertools.setMetadata(admin, "spawnclaiming", false);
                admin.sendMessage(Messages.SPAWN_CLAIM_SUCCESS.queue());
                admin.getInventory().remove(Material.GOLD_HOE);
            } else {
                //Todo: nem sikerült, balfasz vagy és nem raktad le
                playertools.setMetadata(admin, "spawnclaiming", false);
                admin.sendMessage(Messages.FACTION_CLAIM_INVALID_ZONE.queue());
                admin.getInventory().remove(Material.GOLD_HOE);
            }
        } else if (state.equalsIgnoreCase("stop")) {
            playertools.setMetadata(admin, "spawnclaiming", false);
            admin.sendMessage(Messages.FACTION_CLAIM_DECLINE.queue());
            admin.getInventory().remove(Material.GOLD_HOE);
        }
    }

}
