package me.idbi.hcf.Commands;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.GUI.KOTHInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static me.idbi.hcf.Koth.Koth.*;
import static me.idbi.hcf.Tools.HCF_Claiming.ClaimTypes;
import static me.idbi.hcf.Tools.HCF_Claiming.KothPrepare;

public class KothCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length > 0) {
                if (p.hasPermission("factions.admin.admin")) {
                    HCFPlayer player = HCFPlayer.getPlayer(p);
                    switch (args[0]) {
                        case "setreward":
                            if (Main.kothRewardsGUI.contains(p.getUniqueId())) return false;
                            p.openInventory(KOTHInventory.kothInventory());
                            break;
                        case "create":
                            try {
                                createKoth(args[1]);
                                p.sendMessage(Messages.koth_created.language(p).setFaction(args[1]).queue());
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.missing_argument.language(p).queue());
                            } catch (Exception e) {
                                p.sendMessage(Messages.error_while_executing.language(p).queue());
                            }
                            break;
                        case "setcapturezone":
                            try {
                                if (getKothFromName(args[1]) != 0) {
                                    if (player.claimType != ClaimTypes.KOTH) {
                                        if (KothPrepare(p)) {
                                            player.setClaimType(ClaimTypes.KOTH);
                                            player.setKothId(getKothFromName(args[1]));
                                        }
                                    }
                                } else {
                                    p.sendMessage(Messages.koth_invalid_name.language(p).queue());
                                }
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.missing_argument.language(p).queue());
                            }
                            break;
                        case "setnatrualzone":
                            try {
                                if (getKothFromName(args[1]) != 0) {
                                    if (player.claimType != ClaimTypes.SPECIAL) {
                                        if (KothPrepare(p)) {
                                            player.setClaimType(ClaimTypes.SPECIAL);
                                            player.setKothId(getKothFromName(args[1]));
                                        }
                                    }
                                } else {
                                    p.sendMessage(Messages.koth_invalid_name.language(p).queue());
                                }
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.missing_argument.language(p).queue());
                            }
//                            try{
//                                if(getKothFromName(args[1]) != 0){
//                                    if(player.claimType != ClaimTypes.KOTH){
//                                        if(KothPrepare(p)){
//                                            player.setClaimType(ClaimTypes.KOTH);
//                                            player.setKothId(getKothFromName(args[1]));
//                                            Main.sendCmdMessage("MOEW setnatrualzone");
//                                        }
//                                    }else{
//                                        HCF_Claiming.ForceFinishClaim(getKothFromName(args[1]),p,ClaimAttributes.NORMAL);
//
//                                        player.setClaimType(ClaimTypes.NONE);
//                                        player.setKothId(0);
//
//                                        endpositions.remove(getKothFromName(args[1]));
//                                        startpositions.remove(getKothFromName(args[1]));
//                                        p.getInventory().remove(Material.IRON_HOE);
//                                    }
//                                }else{
//                                    p.sendMessage(Messages.koth_invalid_name.language(p).queue());
//                                }
//
//                                //p.sendMessage(Messages.KOTH_SUCESS_CREATE.setFaction(args[1]).queue());
//                            } catch (IndexOutOfBoundsException ignored) {
//                                p.sendMessage(Messages.missing_argument.language(p).queue());
//                            }
                            break;
                        case "help":
                            //System.out.println(ListMessages.KOTH_COMMAND_LIST.queue());
                            for (String lines : Messages.koth_commands.language(p).queueList()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
                            }

                            break;
                        case "start":
                            try {
                                if (getKothFromName(args[1]) != 0) {
                                    startKoth(args[1]);
                                    //Todo: koth started @everyone
                                } else {
                                    p.sendMessage(Messages.koth_invalid_name.language(p).queue());
                                }
                            } catch (IndexOutOfBoundsException ignored) {
                                p.sendMessage(Messages.missing_argument.language(p).queue());
                            }
                            break;
                    }
                }
            } else {
                for (String lines : Messages.koth_commands.language(p).queueList()) {
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
