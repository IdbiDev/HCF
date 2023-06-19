package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
@CommandInfo(
        name = "factioncall",
        description = "Gives your position to your faction!",
        permission = "factions.commands.factioncall",
        syntax = "/factioncall")

public class FactionPositionCommand extends HCFCommand {


    public static String convertLocation(Player p, Location loc) {
        return Messages.faction_player_position.language(p).setPlayer(p).setCoords(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).queue();
    }
    @Override
    public void execute(Player p, String[] args) {
        if (Playertools.getPlayerFaction(p) == null) {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }
        Faction faction = Playertools.getPlayerFaction(p);
        //faction.BroadcastFaction("&2"+p.getName()+" >> &o" +  convertLocation(p,p.getLocation()));

        for (Player member : faction.getOnlineMembers()) {
            member.sendMessage(convertLocation(p, p.getLocation()));
        }
        addCooldown(p);
    }


    @Override
    public int getCooldown() {
        return 0;
    }
    // xd xDDDDDDD
}
