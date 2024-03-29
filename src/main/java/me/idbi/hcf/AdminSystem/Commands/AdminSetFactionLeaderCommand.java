package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;

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
        return "Gives the faction leader to another player.";
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " <faction> <player>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
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
        if (Playertools.getFactionByName(args[1]) == null) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }
        if (Bukkit.getPlayer(args[2]) == null) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }
        HCFPlayer hcf = HCFPlayer.getPlayer(Bukkit.getPlayer(args[2]));
        Faction selectedFaction = Playertools.getFactionByName(args[1]);

        if(hcf.getFaction() != selectedFaction) {
            p.sendMessage(Messages.player_in_faction.queue());
            return;
        }

        assert selectedFaction != null;
        selectedFaction.updateLeader(hcf);
        SQL_Connection.dbExecute(con, "UPDATE factions SET leader='?' WHERE name='?'", selectedFaction.getLeader(), args[1]);

        for (Player member : selectedFaction.getOnlineMembers()) {
            member.sendMessage(Messages.set_faction_leader_by_admin.language(member).setPlayer(Bukkit.getPlayer(args[2])).setExecutor(p).queue());
        }

        p.sendMessage(Messages.admin_set_faction_name.language(p).setFaction(args[1]).queue());
    }
}
