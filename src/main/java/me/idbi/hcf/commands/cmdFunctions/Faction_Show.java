package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
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
        if(factionName.equals("Nincs")) {
            p.sendMessage(Messages.NO_FACTION_EXISTS.queue());
            return null;
        }
        HashMap<Player, Integer> returnPlayers = new HashMap<>();

        Map<String, Main.Faction> asd = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        asd.putAll(Main.nameToFaction);

        Main.Faction faction = asd.get(factionName);

        String factionStatus = (playertools.isFactionOnline(faction.factioname)
                ? Messages.STATUS_DESIGN_ONLINE.queue()
                : Messages.STATUS_DESIGN_OFFLINE.queue());

        String leaderName = ((Bukkit.getPlayer(faction.leader)) != null
                ? Bukkit.getPlayer(UUID.fromString(faction.leader)).getName()
                : Bukkit.getOfflinePlayer(UUID.fromString(faction.leader)).getName());

        Location homeLoc;
        if(faction.homeLocation == null)
            homeLoc = new Location(Bukkit.getWorld(ConfigLibrary.World_name.getValue()), 0, 0, 0, 0, 0);
        else homeLoc = faction.homeLocation;

        for (String line : ListMessages.FACTION_SHOW.setupShow(
                        faction.factioname, factionStatus, leaderName, String.valueOf(faction.balance),
                        "2",
                        "3",
                        homeLoc.getBlockX() + ", " + homeLoc.getBlockZ(),
                        String.valueOf(faction.DTR)
                )
                .setMembers(playertools.getRankPlayers(faction.factioname),
                        playertools.getPlayersKills()).queue()) {
            p.sendMessage(line);
        }

        return returnPlayers;
    }
}
