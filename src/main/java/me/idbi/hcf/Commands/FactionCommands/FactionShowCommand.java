package me.idbi.hcf.Commands.FactionCommands;

import com.comphenix.protocol.PacketType;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FactionShowCommand extends SubCommand {
    @Override
    public String getName() {
        return "show";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("info") || argument.equalsIgnoreCase("who");
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
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction faction = Playertools.getPlayerFaction(p);
        if(args.length == 2) {
            OfflinePlayer offline = Bukkit.getOfflinePlayer(args[1]);
            if (offline.hasPlayedBefore()) {
                if (HCFPlayer.getPlayer(offline.getUniqueId()).inFaction()) {
                    faction = HCFPlayer.getPlayer(offline.getUniqueId()).getFaction();
                }
            } else {
                faction = Playertools.getFactionByName(args[1]);
            }
        }

        if (faction == null) {
            p.sendMessage(Messages.no_faction_exists.language(p).queue());
            return;
        }
        if(faction.getLeader() == null) {
            p.sendMessage(Messages.no_faction_exists.language(p).queue());
            return;
        }
        if(faction.getLeader().equals("null")) {
            p.sendMessage(Messages.no_faction_exists.language(p).queue());
            return;
        }
        if(faction.getLeader().equals("")) {
            p.sendMessage(Messages.no_faction_exists.language(p).queue());
            return;
        }

        String factionStatus = (Playertools.isFactionOnline(faction)
                ? Messages.status_design_online.language(p).queue()
                : Messages.status_design_offline.language(p).queue());

        String leaderName = Bukkit.getOfflinePlayer(UUID.fromString(faction.getLeader())).getName() == null
                ? "-" : Bukkit.getOfflinePlayer(UUID.fromString(faction.getLeader())).getName();
        Location homeLoc;
        if (faction.getHomeLocation() == null)
            homeLoc = new Location(Bukkit.getWorld(Config.WorldName.asStr()), 0, 0, 0, 0, 0);
        else homeLoc = faction.getHomeLocation();

        addCooldown(p);
        for (String line : Messages.faction_show.language(p).setupShow(
                        faction.getName(), factionStatus, leaderName, String.valueOf(faction.getBalance()),
                        faction.getKills() + "",
                        faction.getDeaths() + "",
                        homeLoc.getBlockX() + ", " + homeLoc.getBlockZ(),
                        Formatter.formatDtr(faction),
                        faction.isDTRRegenEnabled()
                                ? faction.getDTR() == faction.getDTR_MAX()
                                    ? "-"
                                    : Playertools.convertLongToTime(faction.getDTR_TIMEOUT() - System.currentTimeMillis())
                                : "-",
                        Formatter.formatDtr(faction.getDTR_MAX()),
                        Playertools.getOnlineSize(faction) + "",
                        faction.getMemberCount() + "",
                        (faction.getDTR() <= 0 ? "true" : "false"),
                String.valueOf(faction.getPoints())

                )
                .setMembers(Playertools.getRankPlayers(faction),
                        Playertools.getFactionKills(faction)).queueList()) {
            p.sendMessage(line);
        }
    }
}
