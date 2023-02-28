package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import org.bukkit.entity.Player;

public class AdminDeleteFactionCommand extends SubCommand {
    @Override
    public String getName() {
        return "deletefaction";
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
        return "/admin " + getName() + " <faction>";
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

    }
}
