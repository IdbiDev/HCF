package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FactionShowCommand extends SubCommand {
    @Override
    public String getName() {
        return "show";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Shows the selected Faction.";
    }

    @Override
    public String getSyntax() {
        return "/faction show [faction | player]";
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction faction = Playertools.getPlayerFaction(p);
        if (faction == null) {
            p.sendMessage(Messages.no_faction_exists.language(p).queue());
            return;
        }

        String factionStatus = (Playertools.isFactionOnline(faction)
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
            homeLoc = new Location(Bukkit.getWorld(Config.WorldName.asStr()), 0, 0, 0, 0, 0);
        else homeLoc = faction.homeLocation;

        for (String line : Messages.faction_show.language(p).setupShow(
                        faction.name, factionStatus, leaderName, String.valueOf(faction.balance),
                        String.valueOf(faction.getKills()),
                        String.valueOf(faction.getDeaths()),
                        homeLoc.getBlockX() + ", " + homeLoc.getBlockZ(),
                        String.valueOf(faction.DTR),
                        ((faction.DTR == faction.DTR_MAX) ? "-" : Playertools.convertLongToTime(faction.DTR_TIMEOUT)),
                        String.valueOf(faction.DTR_MAX),
                        String.valueOf(Playertools.getOnlineSize(faction)),
                        String.valueOf(faction.getMemberCount()),
                        (faction.DTR <= 0 ? "true" : "false")

                )
                .setMembers(Playertools.getRankPlayers(faction),
                        Playertools.getFactionKills(faction)).queueList()) {
            p.sendMessage(line);
        }
    }
}
