package me.idbi.hcf.commands;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.playertools;
import net.dv8tion.jda.api.entities.Invite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;

public class Command_test implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (p.isOp()) {
                if(command.getName().equalsIgnoreCase("")) {}
            }
        }
        return false;
    }
}

/*

                if(command.getName().equalsIgnoreCase("setuplogs")) {
                    Main.Faction faction = playertools.getPlayerFaction(p);

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
