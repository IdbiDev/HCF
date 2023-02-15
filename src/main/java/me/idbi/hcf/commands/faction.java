package me.idbi.hcf.commands;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.commands.cmdFunctions.Bank.Faction_DepositBank;
import me.idbi.hcf.commands.cmdFunctions.Bank.Faction_WithdrawBank;
import me.idbi.hcf.commands.cmdFunctions.*;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class faction implements CommandExecutor, TabCompleter {
    private final Main m = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //if (cmd.getName().equalsIgnoreCase("faction")) {
        if (sender instanceof Player p) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "create":
                        //CreateFaction
                        try {
                            Faction_Create.CreateFaction(p, args[1]);
                        } catch (IndexOutOfBoundsException ignored) {
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    case "show":
                        //Show faction
                        if (args.length == 1) {
                            Faction_Show.show(p, playertools.getMetadata(p, "faction"));
                            return false;
                        } else if (args.length == 2) {
                            Faction arg1Faction = playertools.getFactionByName(args[1]);
                            if (Bukkit.getPlayer(args[1]) != null) {
                                if (args[1] != null) {
                                    Faction_Show.show(p, args[1]);
                                    playertools.getPlayerFaction(p).DTR--;
                                    return false;
                                }
                            }
                            for (Map.Entry<Integer, Faction> faction : Main.faction_cache.entrySet()) {
                                if (faction.getValue().name.equalsIgnoreCase(args[1])) {
                                    Faction_Show.show(p, args[1]);
                                    return false;
                                }
                            }
                            p.sendMessage(Messages.no_faction_exists.queue());
                            return false;
                        }
                        p.sendMessage(Messages.unknown_command.language(p).queue());
                        break;
                    case "claim":
                        //Claim
                        Faction_Claim.PrepareClaiming(p);
                        break;
                    case "leave":
                        Faction_Leave.leave_faction(p);
                        break;
                    case "stats":
                        Faction_Stats.PlayerStats(p, args);
                    case "withdraw":
                        //WithdrawFaction
                        if (args.length != 2) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            return false;
                        }
                        Faction_WithdrawBank.asd(args, p);
                        break;
                    case "deposit":
                        if (args.length != 2) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            return false;
                        }
                        Faction_DepositBank.asd(args, p);
                        break;
                    case "invite":
                        //Invite
                        try {
                            Faction_Invite.InvitePlayerToFaction(p, args[1]);
                        } catch (Exception e) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }
                        break;
                    case "join":
                        try {
                            Faction_Join.JoinToFaction(p, args[1]);
                        } catch (IndexOutOfBoundsException ignored) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    case "help":
                        for (String lines : Messages.commands.language(p).queueList()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                        }
                        break;
                    case "chat":
                        Faction_Chat.chat(p, args);
                        break;
                    case "sethome":
                        Faction_Home.setHome(p);
                        break;
                    case "home":
                        Faction_Home.teleportToHome(p);
                        break;
                    case "reload":
                        if (sender.hasPermission("factions.reload")) {
                        /*    switch (args[1]) {
                                case "config" -> m.reloadConfig();
                                case "messages" -> MessagesFile.reloadMessages();
                                case "discord" -> DiscordFile.reloadDiscord();
                                case "all" -> {
                                    MessagesFile.reloadMessages();
                                    m.reloadConfig();
                                    DiscordFile.reloadDiscord();
                                    Main.blacklistedRankNames = m.getConfig().getStringList("Blacklisted-rankNames");
                                }
                                default -> {
                                    sender.sendMessage(Messages.NOT_FILE.getMessage().queue());
                                    return false;
                                }
                            }*/
                            p.sendMessage(Messages.reload.language(p).queue());
                        } else {
                            sender.sendMessage(Messages.no_permission.language(p).queue());
                        }
                        break;
                    /*case "addrank":
                        try {

                            //Faction_Rank_Manager.addRank(p, args[1]);
                        } catch (IndexOutOfBoundsException ignored) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            e.printStackTrace();
                        }
                        break;
                    case "setrankperm":
                        try {
                            //Faction_Ranks.setPermissionToRank(p, args[1], args[2]);
                        } catch (IndexOutOfBoundsException asd) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            e.printStackTrace();
                        }
                        break;
                    case "removerankperm":
                        try {
                            //Faction_Ranks.removePermissionFromRank(p, args[1], args[2]);
                        } catch (IndexOutOfBoundsException asd) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            e.printStackTrace();
                        }
                        break;
                    case "setrank":
                        try {
                            //Faction_Ranks.setPlayerForRank(p, args[1], args[2]);
                        } catch (IndexOutOfBoundsException asd) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            e.printStackTrace();
                        }
                        break;*/
                    case "disband":
                        try {
                            Faction_Disband.disband(p, args[1]);
                        } catch (IndexOutOfBoundsException asd) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    case "stuck":
                        try {
                            Faction_Stuck.processStuck(p);
                        } catch (IndexOutOfBoundsException asd) {
                            p.sendMessage(Messages.unknown_command.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        }catch (Exception e){
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    case "manage":
                        try{
                            if(playertools.getPlayerFaction(p) == null) {
                                p.sendMessage(Messages.not_in_faction.language(p).queue());
                                return false;
                            }
                            p.openInventory(MainInventory.mainInv(p));
                            GUI_Sound.playSound(p,"success");
                        }catch (Exception e){
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        p.sendMessage(Messages.unknown_command.language(p).queue());
                        break;
                }
            } else {
                for (String lines : Messages.commands.language(p).queueList()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                }
            }
            // }
        }
        return true;
    }

    public ArrayList<String> commandList() {
        ArrayList<String> commands = (ArrayList<String>) Arrays.asList(
                "§9/f create §7- Create your faction",
                "§9/f show [Faction] §7- Show faction",
                "§9/f claim §7- ",
                "§9/f invite <Player> §7- Invite player to your faction",
                "§9/f join §7- Join to a faction",
                "§9/f leave §7- Leave from your faction",
                "§9/f deposit <Amount> §7- Deposit money to your faction bank!",
                "§9/f withdraw <Amount> §7- Withdraw money from faction bank!",
                "§9/f sethome §7- Sets a home to your faction!",
                "§9/f home §7- Teleport to faction's home!",
                "§9/f home §7- Teleport to faction's home!",
                "§9/f reload [file] §7- Reload files!"
        );

        ArrayList<String> returnList = new ArrayList();

        for (String cmds : commands) {
            returnList.add(cmds);
        }
        return returnList;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("faction")) {
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("reload"))
                    return Arrays.asList(
                            "all", "config", "messages"
                    );
            } else if (args.length == 1) {
                return Arrays.asList(
                        "create",
                        "claim",
                        "show",
                        "join",
                        "leave",
                        "withdraw",
                        "deposit",
                        "invite",
                        "addrank",
                        "sethome",
                        "manage",
                        "home"
                );
            }
        }
        return null;
    }
}
