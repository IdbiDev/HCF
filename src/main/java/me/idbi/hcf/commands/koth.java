package me.idbi.hcf.commands;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.koth.GUI.KOTHInventory;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static me.idbi.hcf.koth.KOTH.createKoth;
import static me.idbi.hcf.koth.KOTH.getKothFromName;
import static me.idbi.hcf.tools.HCF_Claiming.endpositions;
import static me.idbi.hcf.tools.HCF_Claiming.startpositions;

public class koth implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p){
            if (args.length > 0) {
                if (p.hasPermission("factions.admin.admin")) {
                    switch (args[0]) {
                        case "setreward":
                            if(Main.kothRewardsGUI.contains(p.getUniqueId())) return false;
                            p.openInventory(KOTHInventory.kothInventory());
                            break;
                        case "create":
                            try{
                                createKoth(args[1]);
                                p.sendMessage(Messages.KOTH_CREATED.setFaction(args[1]).queue());
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }catch (Exception e){
                                p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
                            }
                            break;
                        case "setcapturezone":
                            try{
                                if(getKothFromName(args[1]) != 0){
                                    if(!Boolean.parseBoolean(playertools.getMetadata(p, "kothclaim"))){
                                        playertools.setMetadata(p, "kothclaim", true);
                                        playertools.setMetadata(p, "kothid", getKothFromName(args[1]));
                                    }else{
                                        HCF_Claiming.ForceFinishClaim(getKothFromName(args[1]),p,"koth");
                                        playertools.setMetadata(p, "kothclaim", false);
                                        playertools.setMetadata(p, "kothid", 0);
                                        endpositions.remove(getKothFromName(args[1]));
                                        startpositions.remove(getKothFromName(args[1]));
                                    }
                                }else{
                                    p.sendMessage(Messages.KOTH_INVALID_NAME.queue());
                                }

                                p.sendMessage(Messages.KOTH_CREATED.setFaction(args[1]).queue());
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }
                            break;
                        case "setnatrualzone":
                            try{
                                if(getKothFromName(args[1]) != 0){
                                    if(!Boolean.parseBoolean(playertools.getMetadata(p, "kothclaim"))){
                                        playertools.setMetadata(p, "kothclaim", true);
                                        playertools.setMetadata(p, "kothid", getKothFromName(args[1]));
                                    }else{
                                        HCF_Claiming.ForceFinishClaim(getKothFromName(args[1]),p,"normal");
                                        playertools.setMetadata(p, "kothclaim", false);
                                        playertools.setMetadata(p, "kothid", 0);
                                        endpositions.remove(getKothFromName(args[1]));
                                        startpositions.remove(getKothFromName(args[1]));
                                    }
                                }else{
                                    p.sendMessage(Messages.KOTH_INVALID_NAME.queue());
                                }

                                p.sendMessage(Messages.KOTH_SUCESS_CREATE.setFaction(args[1]).queue());
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.UNKNOWN_COMMAND.queue());
                            }
                            break;
                        case "help":
                            //System.out.println(ListMessages.KOTH_COMMAND_LIST.queue());
                            for (String lines : ListMessages.KOTH_COMMAND_LIST.queue()) {
                                System.out.println(lines);
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                            }
                            break;
                    }
                }
            }else{
                for (String lines : ListMessages.KOTH_COMMAND_LIST.queue()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("koth")) {
            if (args.length == 1) {
                return Arrays.asList(
                        "setreward",
                        "create",
                        "setcapturezone",
                        "setnatrualzone",
                        "help"
                );
            }
        }
        return null;
    }

}
