package me.idbi.hcf.commands;

import me.idbi.hcf.CustomFiles.MessagesFile;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.commands.cmdFunctions.*;
import me.idbi.hcf.commands.cmdFunctions.Bank.Faction_DepositBank;
import me.idbi.hcf.commands.cmdFunctions.Bank.Faction_WithdrawBank;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
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
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //if (cmd.getName().equalsIgnoreCase("faction")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length > 0) {
                    switch (args[0]) {
                        case "create":
                            //CreateFaction
                            try{
                                Faction_Create.CreateFaction(p,args[1]);
                            }catch (Exception e){
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                                //Kiíratás hogy balfaszul használta a commandot
                            }
                            break;
                        case "show":
                            //Show faction
                            if(args.length == 1) {
                                Faction_Show.show(p, playertools.getMetadata(p, "faction"));
                                return false;
                            } else if(args.length == 2) {
                                for(Map.Entry<Integer, Main.Faction> faction : Main.faction_cache.entrySet()) {
                                    if (faction.getValue().factioname.equalsIgnoreCase(args[1])) {
                                        Faction_Show.show(p, args[1]);
                                        return false;
                                    }
                                }
                                p.sendMessage(Messages.NO_FACTION_EXISTS.queue());
                                return false;
                            }
                            p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            break;
                        case "claim":
                            //Claim
                            Faction_Claim.PrepareClaiming(p);
                            break;
                        case "leave":
                            Faction_Leave.leave_faction(p);
                            break;
                        case "withdraw":
                            //WithdrawFaction
                            if(args.length != 2) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                                return false;
                            }
                            Faction_WithdrawBank.asd(args, p);
                            break;
                        case "deposit":
                            if(args.length != 2) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                                return false;
                            }
                            Faction_DepositBank.asd(args, p);
                            break;
                        case "invite":
                            //Invite
                            try {
                                Faction_Invite.InvitePlayerToFaction(p, args[1]);
                            } catch (Exception e){
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                                //Kiíratás hogy balfaszul használta a commandot
                            }
                            break;
                        case "help":
                            for(String lines : ListMessages.COMMAND_LIST.queue()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                            }
                            break;
                        case "sethome":
                            Faction_Home.setHome(p);
                            break;
                        case "home":
                            Faction_Home.teleportToHome(p);
                            break;
                        case "reload":
                            if (sender.hasPermission("factions.reload")) {
                                switch (args[1]) {
                                    case "config":
                                        m.reloadConfig();
                                        break;
                                    case "messages":
                                        MessagesFile.reloadMessages();
                                        break;
                                    case "all":
                                        MessagesFile.reloadMessages();
                                        m.reloadConfig();
                                        break;
                                    default:
                                        sender.sendMessage(Messages.NOT_FILE.getMessage().queue());
                                        return false;
                                }
                                p.sendMessage(Messages.RELOAD.queue());
                            } else {
                                sender.sendMessage(Messages.NO_PERMISSION.getMessage().queue());
                            }
                            break;
                        case "addrank":
                            //Invite
                            try {
                                Faction_Ranks.addRank(p, args[1]);
                            } catch (Exception e){
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                                //Kiíratás hogy balfaszul használta a commandot
                            }
                            break;
                    }
                } else {
                    for(String lines : ListMessages.COMMAND_LIST.queue()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                    }
                }
           // }
        }
        return true;
    }

    private Main m = Main.getPlugin(Main.class);

    @Deprecated
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
        if(cmd.getName().equalsIgnoreCase("faction")) {
            if(args.length == 2) {
                return Arrays.asList(
                        "all", "config", "messages"
                );
            } else if(args.length == 1) {
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
                        "home"
                );
            }
        }
        return null;
    }
}
