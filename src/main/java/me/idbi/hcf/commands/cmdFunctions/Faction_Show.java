package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class Faction_Show {
    public static HashMap<Player, Integer> show(Player p, Faction faction) {
        if (faction == null) {
            p.sendMessage(Messages.no_faction_exists.language(p).queue());
            return null;
        }
        HashMap<Player, Integer> returnPlayers = new HashMap<>();

        String factionStatus = (playertools.isFactionOnline(faction)
                ? Messages.status_design_online.language(p).queue()
                : Messages.status_design_offline.language(p).queue());
        String leaderName = "";
        try {
            leaderName = ((Bukkit.getPlayer(faction.leader)) != null
                    ? Bukkit.getPlayer(UUID.fromString(faction.leader)).getName()
                    : Bukkit.getOfflinePlayer(UUID.fromString(faction.leader)).getName());
        } catch (IllegalArgumentException ignore) {
        }
        Location homeLoc;
        if (faction.homeLocation == null)
            homeLoc = new Location(Bukkit.getWorld(Config.world_name.asStr()), 0, 0, 0, 0, 0);
        else homeLoc = faction.homeLocation;

        for (String line : Messages.faction_show.language(p).setupShow(
                        faction.name, factionStatus, leaderName, String.valueOf(faction.balance),
                        String.valueOf(faction.getKills()),
                        String.valueOf(faction.getDeaths()),
                        homeLoc.getBlockX() + ", " + homeLoc.getBlockZ(),
                        String.valueOf(faction.DTR),
                        ((faction.DTR == faction.DTR_MAX) ? "-" : playertools.convertLongToTime(faction.DTR_TIMEOUT)),
                        String.valueOf(faction.DTR_MAX),
                        String.valueOf(playertools.getOnlineSize(faction)),
                        String.valueOf(faction.getMemberCount()),
                        (faction.DTR <= 0 ? "true" : "false")

                )
                .setMembers(playertools.getRankPlayers(faction),
                        playertools.getFactionKills(faction)).queueList()) {
            p.sendMessage(line);
        }

        return returnPlayers;
    }
}
