package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.AdminSystem.AdminCommandManager;
import me.idbi.hcf.Commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminHelpCommand extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "This help window";
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName();
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
        p.sendMessage("--------------------------------");
        for (int i = 0; i < AdminCommandManager.getSubcommands().size(); i++) {
            p.sendMessage(AdminCommandManager.getSubcommands().get(i).getSyntax() + " - " + AdminCommandManager.getSubcommands().get(i).getDescription());
        }
        p.sendMessage("--------------------------------");
    }
}
