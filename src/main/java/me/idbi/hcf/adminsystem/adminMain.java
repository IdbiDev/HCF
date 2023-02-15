package me.idbi.hcf.adminsystem;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.AdminTools;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.FactionHistory;
import me.idbi.hcf.tools.Objects.HCFPlayer;
import me.idbi.hcf.tools.Objects.PlayerStatistic;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class adminMain {
    private static final Connection con = Main.getConnection("adminSystem");

    public static void setAdminDuty(Player player, boolean state) {
        if (state) {
            player.setGameMode(GameMode.CREATIVE);
            HCFPlayer hcf = HCFPlayer.getPlayer(player);
            hcf.setDuty(true);

            AdminTools.InvisibleManager.hidePlayer(player);

            AdminScoreboard.refresh(player);


            player.sendMessage(Messages.admin_duty_on.language(player).queue());
        } else {
            player.setGameMode(GameMode.SURVIVAL);

            HCFPlayer hcf = HCFPlayer.getPlayer(player);
            hcf.setDuty(false);
            AdminTools.InvisibleManager.showPlayer(player);

            Scoreboards.refresh(player);

            player.sendMessage(Messages.admin_duty_off.language(player).queue());
        }
        NameChanger.refresh(player);
    }

    public static void Deposit(Player admin, String faction, int amount) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.not_found_faction.language(admin).queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.not_a_number.language(admin).queue());
            return;
        }

        Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance += amount;
        Faction f = Main.nameToFaction.get(faction);

        for (Player member : f.getMembers()) {
            member.sendMessage(Messages.faction_admin_deposit_bc.language(member).setExecutor(admin).queue());
        }
    }

    public static void Withdraw(Player admin, String faction, int amount) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.not_found_faction.language(admin).queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.not_a_number.language(admin).queue());
            return;
        }
        Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.balance -= amount;
        Faction f = Main.nameToFaction.get(faction);

        for (Player member : f.getMembers()) {
            member.sendMessage(Messages.faction_admin_withdraw_bc.language(member).setExecutor(admin).queue());
        }
    }

    public static void SetFactionname(Player admin, String faction, String newName) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.not_found_faction.language(admin).queue());
            return;
        }
        if (newName.length() > 5) {
            admin.sendMessage(Messages.not_a_number.language(admin).queue());
            return;
        }
        Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.name = newName;
        //Todo: Broadcast the change
        Faction f = Main.nameToFaction.get(faction);

        for (Player member : f.getMembers()) {
            member.sendMessage(Messages.set_faction_name.language(member).setExecutor(admin).setFaction(newName).queue());
        }

        admin.sendMessage(Messages.admin_set_faction_name.setFaction(f.name).queue());

        Scoreboards.RefreshAll();
    }

    public static void GiveMoney(Player admin, String target, int amount) {
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.not_found_player.language(admin).queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.not_a_number.language(admin).queue());
            return;
        }
        Player player = Bukkit.getPlayer(target);
        HCFPlayer hcf = HCFPlayer.getPlayer(player);
        hcf.addMoney(amount);

        player.sendMessage(Messages.give_money.language(admin).setExecutor(admin).setAmount(String.valueOf(amount)).queue());
        Scoreboards.refresh(player);

        PlayerStatistic stat = hcf.playerStatistic;
        stat.MoneyEarned += amount;
    }

    public static void TakeMoney(Player admin, String target, int amount) {
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.not_found_player.language(admin).queue());
            return;
        }
        if (amount <= 0) {
            admin.sendMessage(Messages.not_a_number.language(admin).queue());
            return;
        }
        Player player = Bukkit.getPlayer(target);
        HCFPlayer hcf = HCFPlayer.getPlayer(player);
        hcf.takeMoney(amount);

        player.sendMessage(Messages.take_money.language(admin).setExecutor(admin).setAmount(String.valueOf(amount)).queue());
        Scoreboards.refresh(player);
        PlayerStatistic stat = hcf.playerStatistic;
        stat.MoneySpend += amount;
    }

    public static void DeleteFaction(Player admin, String faction) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.not_found_faction.language(admin).queue());
            return;
        }

        Faction selectedFaction = Main.nameToFaction.get(faction);

        Main.faction_cache.remove(selectedFaction.id);
        Main.nameToFaction.remove(selectedFaction.name);
        SQL_Connection.dbExecute(con, "DELETE FROM ranks WHERE faction='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "DELETE FROM factions WHERE ID='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "DELETE FROM claims WHERE factionid='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "UPDATE members SET rank='None',faction=0 WHERE faction='?'", String.valueOf(selectedFaction.id));
        for (Player player : playertools.getFactionOnlineMembers(selectedFaction)) {
            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);
            final String rank = hcfPlayer.rank.name;
            hcfPlayer.removeFaction();

            PlayerStatistic stat = hcfPlayer.playerStatistic;
            for(FactionHistory statF : stat.factionHistory){
                if(statF.id == selectedFaction.id){
                    statF.left = new Date();
                    statF.cause = "Deleted";
                    statF.lastRole = rank;
                    statF.name = selectedFaction.name;
                }
            }
            NameChanger.refresh(player);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Messages.delete_faction_by_admin.setExecutor(player).setFaction(selectedFaction.name).queue());
        }
    }

    public static void setFactionLeader(Player admin, String faction, String Leader) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.not_found_faction.language(admin).queue());
            return;
        }
        if (Bukkit.getPlayer(Leader) == null) {
            admin.sendMessage(Messages.not_found_player.language(admin).queue());
            return;
        }
        Faction selectedFaction = Main.nameToFaction.get(faction);
        selectedFaction.leader = Bukkit.getPlayer(Leader).getUniqueId().toString();
        SQL_Connection.dbExecute(con, "UPDATE factions SET leader='?' WHERE name='?'", selectedFaction.leader, faction);
        //Todo: Broadcast the change
        Faction f = Main.nameToFaction.get(faction);

        for (Player member : f.getMembers()) {
            member.sendMessage(Messages.set_faction_leader_by_admin.language(member).setPlayer(Bukkit.getPlayer(Leader)).setExecutor(admin).queue());
        }

        admin.sendMessage(Messages.admin_set_faction_name.language(admin).setFaction(faction).queue());
    }

    public static void setPlayerFaction(Player admin, String target, String faction) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.not_found_faction.language(admin).queue());
            return;
        }
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.not_found_player.language(admin).queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        Faction selectedFaction = Main.nameToFaction.get(faction);
        HCFPlayer player = HCFPlayer.getPlayer(p);

        player.setFaction(selectedFaction);

        SQL_Connection.dbExecute(con, "UPDATE members SET faction='?' WHERE uuid='?'", String.valueOf(selectedFaction.id), p.getUniqueId().toString());

        //Sikeres belépés
        p.sendMessage(Messages.join_message.language(p).queue());

        //Faction -> xy belépett
        Faction f = Main.nameToFaction.get(faction);

        for (Player member : f.getMembers()) {
            member.sendMessage(Messages.bc_join_message.language(member).setPlayer(p).queue());

        }

        Scoreboards.refresh(p);
        admin.sendMessage(Messages.admin_set_playerfaction.setFaction(faction).setPlayer(p).queue());
        NameChanger.refresh(p);
    }

    public static void kickPlayerFromFaction(Player admin, String target, String faction) {
        if (!Main.nameToFaction.containsKey(faction)) {
            admin.sendMessage(Messages.not_found_faction.language(admin).queue());
            return;
        }
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.not_found_player.language(admin).queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        HCFPlayer player = HCFPlayer.getPlayer(p);
        player.removeFaction();

        SQL_Connection.dbExecute(con, "UPDATE members SET faction=0 WHERE uuid='?'", p.getUniqueId().toString());
        Scoreboards.refresh(p);
        NameChanger.refresh(p);


    }

    public static void FreezePlayer(Player admin, String target) {
        if (Bukkit.getPlayer(target) == null) {
            admin.sendMessage(Messages.not_found_player.language(admin).queue());
            return;
        }
        Player p = Bukkit.getPlayer(target);
        HCFPlayer player = HCFPlayer.getPlayer(p);
        boolean state = player.freezeStatus;
        // Freeze
        if (!state) {
            admin.sendMessage(Messages.freeze_executor_on.language(admin).setPlayer(p).queue());
            p.sendMessage(Messages.freeze_player_off.language(p).setExecutor(admin).queue());
        } else {
            //UnFreeze
            admin.sendMessage(Messages.freeze_executor_off.language(admin).setPlayer(p).queue());
            p.sendMessage(Messages.freeze_player_off.language(p).setExecutor(admin).queue());
        }
        player.setFreezeStatus(!player.freezeStatus);
    }

    public static void SpawnPlace(Player admin, String state) {
        HCFPlayer player = HCFPlayer.getPlayer(admin);
        if (state.equalsIgnoreCase("start")) {
            for (String lines : Messages.claim_info_admin.queueList()) {

                admin.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
            }

            player.setClaimType(HCF_Claiming.ClaimTypes.SPAWN);
            HCF_Claiming.SpawnPrepare(admin);
        } else if (state.equalsIgnoreCase("claim")) {
            if (HCF_Claiming.ForceFinishClaim(1,admin, HCF_Claiming.ClaimAttributes.PROTECTED)) {
                //Todo: Kurvva sikerült
                player.setClaimType(HCF_Claiming.ClaimTypes.NONE);
                admin.sendMessage(Messages.spawn_claim_success.language(admin).queue());
                admin.getInventory().remove(HCF_Claiming.spawnWand());
            } else {
                //Todo: nem sikerült, balfasz vagy és nem raktad le
                player.setClaimType(HCF_Claiming.ClaimTypes.NONE);
                admin.sendMessage(Messages.faction_claim_invalid_zone.language(admin).queue());
                admin.getInventory().remove(HCF_Claiming.spawnWand());
            }
        } else if (state.equalsIgnoreCase("stop")) {
            player.setClaimType(HCF_Claiming.ClaimTypes.NONE);
            admin.sendMessage(Messages.faction_claim_decline.language(admin).queue());
            admin.getInventory().remove(HCF_Claiming.spawnWand());
        }
    }

    public static void toggleStaffChat(Player admin, String[] args) {
        if(!playertools.isInStaffDuty(admin)) {
            admin.sendMessage(Messages.not_in_duty.language(admin).queue());
            return;
        }
        HCFPlayer player = HCFPlayer.getPlayer(admin);
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("chat")) {
                if(!player.staffChat) {
                    player.setStaffChat(true);
                    admin.sendMessage(Messages.staff_chat.language(admin).queue());
                } else {
                    player.setStaffChat(false);
                    admin.sendMessage(Messages.staff_chat_off.language(admin).queue());
                }
            }
            AdminScoreboard.refresh(admin);
        }
        else if(args.length == 2) {
            if (args[0].equalsIgnoreCase("chat")) {
                HCFPlayer hcf = HCFPlayer.getPlayer(admin);
                if(args[1].equalsIgnoreCase("on")) {
                    hcf.setStaffChat(true);
                    admin.sendMessage(Messages.staff_chat_on.language(admin).queue());
                } else if(args[1].equalsIgnoreCase("off")) {
                    hcf.setStaffChat(false);
                    admin.sendMessage(Messages.staff_chat_off.language(admin).queue());
                } else {
                    List<String> argsList = Arrays.stream(args).toList();
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if(onlinePlayer.hasPermission("factions.admin")) {
                            onlinePlayer.sendMessage(
                                    Messages.staff_chat.language(onlinePlayer)
                                            .setPlayer(admin)
                                            .setMessage(getMessage(argsList)
                                                    .replaceFirst(args[0] + " ", ""))
                                            .queue());
                        }
                    }
                }
            }
            AdminScoreboard.refresh(admin);
        } else {
            List<String> argsList = Arrays.stream(args).toList();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.hasPermission("factions.admin")) {
                    onlinePlayer.sendMessage(Messages.staff_chat.language(onlinePlayer).setPlayer(admin).setMessage(getMessage(argsList)).queue());
                }
            }
        }
    }

    public static String getMessage(List<String> args) {
        String output = "";

        for (String s : args) {
            output += s;
            output += " ";
        }

        return output;
    }

    public static void toggleVanish(Player p, String[] args) {
        if(!playertools.isInStaffDuty(p)) {
            p.sendMessage(Messages.not_in_duty.language(p).queue());
            return;
        }
        boolean inVanish = AdminTools.InvisibleManager.invisedAdmins.contains(p);
        if(inVanish) {
            AdminTools.InvisibleManager.showPlayer(p);
            AdminScoreboard.refresh(p);
            p.sendMessage(Messages.vanish_disable.language(p).queue());
        } else {
            AdminTools.InvisibleManager.hidePlayer(p);
            AdminScoreboard.refresh(p);
            p.sendMessage(Messages.vanish_enabled.language(p).queue());
        }
    }

    public static void setDTR(Player p, String[] args) {
        // /a setdtr CicaMC 2.0
        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("setdtr")) {
                Faction f = playertools.getFactionByName(args[1]);
                if(f == null) {
                    p.sendMessage(Messages.not_found_player.language(p).queue());
                    return;
                }
                try {
                    double dtr = Double.parseDouble(args[2]);
                    if(f.DTR_MAX >= dtr) {
                        f.DTR = Math.min(f.DTR_MAX, dtr);
                        // ToDo: Successfully set dtr
                        p.sendMessage(Messages.admin_set_dtr.language(p).setFaction(f.name).queue()
                                .replace("%new_dtr%", f.DTR + ""));
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(Messages.not_a_number.language(p).queue());
                }
            }
        }
    }
}
