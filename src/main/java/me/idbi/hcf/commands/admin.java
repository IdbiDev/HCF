package me.idbi.hcf.commands;

import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.WorldModes.SOTW;
import me.idbi.hcf.adminsystem.adminMain;
import me.idbi.hcf.commands.cmdFunctions.Faction_EOTW;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class admin implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length > 0) {
                if (p.hasPermission("factions.admin.admin")) {
                    switch (args[0]) {
                        case "duty":
                            adminMain.setAdminDuty(p, !Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty")));
                            break;
                        case "deposit":
                            try {
                                adminMain.Deposit(p, args[1], Integer.parseInt(args[2]));
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "withdraw":
                            try {
                                adminMain.Withdraw(p, args[1], Integer.parseInt(args[2]));
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "freeze":
                            try {
                                adminMain.FreezePlayer(p, args[1]);
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "kick":
                            break;
                        case "setfaction":
                            try {
                                adminMain.setPlayerFaction(p, args[1], args[2]);
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "removefaction":
                            try {
                                adminMain.kickPlayerFromFaction(p, args[1], args[2]);
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "eotw":
                            try {
                                Faction_EOTW.EOTW(p);
                                //Kiíratás hogy balfaszul használta a commandot
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                                e.printStackTrace();
                            }
                            break;
                        case "sotw":
                            try {
                                SOTW.EnableSOTW();
                                //Kiíratás hogy balfaszul használta a commandot
                            } catch (Exception e) {
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                                e.printStackTrace();
                            }
                            break;
                        case "deletefaction":
                            try {
                                adminMain.DeleteFaction(p, args[1]);
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "setleader":
                            try {
                                adminMain.setFactionLeader(p, args[1], args[2]);
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "givemoney":
                            try {
                                adminMain.GiveMoney(p, args[1], Integer.parseInt(args[2]));
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "takemoney":
                            try {
                                adminMain.TakeMoney(p, args[1], Integer.parseInt(args[2]));
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "setfactionname":
                            try {
                                adminMain.SetFactionname(p, args[1], String.valueOf(args[2]));
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "spawnclaim":
                            try {
                                adminMain.SpawnPlace(p, args[1]);
                                //ListMessages.CLAIM_INFO.queue().forEach(p::sendMessage);
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "help":
                            for (String lines : ListMessages.ADMIN_COMMAND_LIST.queue()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                            }
                            break;
                    }
                } else {
                    p.sendMessage(Messages.NO_PERMISSION.queue());
                }
            }else{
                for (String lines : ListMessages.ADMIN_COMMAND_LIST.queue()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("admin")) {
            if (args.length == 1) {
                return Arrays.asList(
                        "duty",
                        "deposit",
                        "withdraw",
                        "freeze",
                        "kick",
                        "setfaction",
                        "removefaction",
                        "deletefaction",
                        "setleader",
                        "givemoney",
                        "takemoney",
                        "setfactionname",
                        "spawnclaim",
                        "eotw",
                        "help"
                );
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("spawnclaim")) {
                    return Arrays.asList("start", "stop","claim");
                }
            }
        }
        return null;
    }
}
