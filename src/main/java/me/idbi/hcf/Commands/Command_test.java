package me.idbi.hcf.Commands;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class Command_test implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.setOp(true);
            // setuplogs factions.*
            /*p.sendMessage(p.hasPermission(args[0]) + "");
            if(args[0].equalsIgnoreCase("op"))
                p.setOp(true);*/
            if (p.isOp()) {
                if (command.getName().equalsIgnoreCase("deleteconfig")) {
                    //new File(Main.getInstance().getDataFolder(), "config.yml").delete();
                    new File(Main.getInstance().getDataFolder(), "scoreboard.yml").delete();
                } else if (command.getName().equalsIgnoreCase("balance")) {
                    p.sendMessage("§7[§2§o$§7] §aBalance: §c" + HCFPlayer.getPlayer(p).getMoney());
                }
            }
        }
        return false;
    }
}

/*

                if(command.getName().equalsIgnoreCase("setuplogs")) {
                    Faction faction = playertools.getPlayerFaction(p);

                    for (int i = 0; i < 55; i++) {
                        faction.rankCreateHistory.add(0, new HistoryEntrys.RankEntry("rank" + i, p.getName(), new Date().getTime(), "type"));
                        faction.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(
                                p.getName(),
                                "type",
                                new Date().getTime()
                        ));

                        faction.inviteHistory.add(0, new HistoryEntrys.InviteEntry(
                                p.getName(),
                                p.getName(),
                                new Date().getTime(),
                                true
                        ));

                        faction.kickHistory.add(0, new HistoryEntrys.KickEntry(
                                p.getName(),
                                p.getName(),
                                new Date().getTime(),
                                "this is the reason"+i //Használj I-t
                        ));
                    }
                }
            }
 */
