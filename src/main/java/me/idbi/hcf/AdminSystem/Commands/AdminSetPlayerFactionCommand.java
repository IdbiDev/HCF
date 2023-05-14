package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;

public class AdminSetPlayerFactionCommand extends SubCommand {
    private static final Connection con = Main.getConnection();

    @Override
    public String getName() {
        return "setplayerfaction";
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
        return "/admin " + getName() + " <player> <faction>";
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
        if (!Main.nameToFaction.containsKey(args[2])) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }
        if (Bukkit.getPlayer(args[1]) == null) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        Faction selectedFaction = Main.nameToFaction.get(args[2]);
        HCFPlayer player = HCFPlayer.getPlayer(p);

        player.setFaction(selectedFaction);

        SQL_Connection.dbExecute(con, "UPDATE members SET faction='?' WHERE uuid='?'", String.valueOf(selectedFaction.getId()), target.getUniqueId().toString());

        //Sikeres belépés
        target.sendMessage(Messages.player_joined_faction_message.language(target).queue());

        //Faction -> xy belépett
        Faction f = Main.nameToFaction.get(args[2]);

        for (Player member : f.getOnlineMembers()) {
            member.sendMessage(Messages.new_member_join_faction.language(member).setPlayer(target).queue());

        }

        Scoreboards.refresh(target);
        p.sendMessage(Messages.admin_set_playerfaction.setFaction(f).setPlayer(target).queue());
        NameChanger.refresh(target);
    }
}
