package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
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
    public static HashMap<Player, Integer> show(Player p, String factionName) {
        if (factionName.equalsIgnoreCase("none")) {
            p.sendMessage(Messages.NO_FACTION_EXISTS.queue());
            return null;
        }
        HashMap<Player, Integer> returnPlayers = new HashMap<>();

        Map<String, Faction> asd = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        asd.putAll(Main.nameToFaction);

        // System.out.println("Faction Name: " + factionName);
        Faction faction = asd.get(factionName);

        String factionStatus = (playertools.isFactionOnline(faction)
                ? Messages.STATUS_DESIGN_ONLINE.queue()
                : Messages.STATUS_DESIGN_OFFLINE.queue());
        String leaderName = "";
        try {
            leaderName = ((Bukkit.getPlayer(faction.leader)) != null
                    ? Bukkit.getPlayer(UUID.fromString(faction.leader)).getName()
                    : Bukkit.getOfflinePlayer(UUID.fromString(faction.leader)).getName());
        } catch (IllegalArgumentException ignore) {
        }
        Location homeLoc;
        if (faction.homeLocation == null)
            homeLoc = new Location(Bukkit.getWorld(ConfigLibrary.World_name.getValue()), 0, 0, 0, 0, 0);
        else homeLoc = faction.homeLocation;

        for (String line : ListMessages.FACTION_SHOW.setupShow(
                        faction.name, factionStatus, leaderName, String.valueOf(faction.balance),
                        String.valueOf(faction.getKills()),
                        String.valueOf(faction.getDeaths()),
                        homeLoc.getBlockX() + ", " + homeLoc.getBlockZ(),
                        String.valueOf(faction.DTR),
                        ((faction.DTR == faction.DTR_MAX) ? "-" : playertools.convertLongToTime(faction.DTR_TIMEOUT)),
                        String.valueOf(faction.DTR_MAX),
                        String.valueOf(playertools.getOnlineSize(faction)),
                        String.valueOf(faction.memberCount),
                        (faction.DTR <= 0 ? "true" : "false")

                )
                .setMembers(playertools.getRankPlayers(faction.name),
                        playertools.getPlayersKills()).queueShow()) {
            p.sendMessage(line);
        }

        return returnPlayers;
    }
}
