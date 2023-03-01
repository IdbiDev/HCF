package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class AdminSetFactionLeaderCommand extends SubCommand {
    private static final Connection con = Main.getConnection();

    @Override
    public String getName() {
        return "setfactionleader";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " <faction> <player>";
    }

    @Override
    public String getPermission() {
        return "factions.command.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        if (!Main.nameToFaction.containsKey(args[1])) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }
        if (Bukkit.getPlayer(args[2]) == null) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }
        Faction selectedFaction = Main.nameToFaction.get(args[1]);
        selectedFaction.leader = Bukkit.getPlayer(args[2]).getUniqueId().toString();
        SQL_Connection.dbExecute(con, "UPDATE factions SET leader='?' WHERE name='?'", selectedFaction.leader, args[1]);
        //Todo: Broadcast the change
        Faction f = Main.nameToFaction.get(args[1]);

        for (Player member : f.getMembers()) {
            member.sendMessage(Messages.set_faction_leader_by_admin.language(member).setPlayer(Bukkit.getPlayer(args[2])).setExecutor(p).queue());
        }

        p.sendMessage(Messages.admin_set_faction_name.language(p).setFaction(args[1]).queue());
    }
}
