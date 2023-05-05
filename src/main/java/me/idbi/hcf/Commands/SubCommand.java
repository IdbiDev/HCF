package me.idbi.hcf.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class SubCommand {

    public static HashMap<SubCommand, HashMap<Player, Long>> commandCooldowns;
    
    public abstract String getName();

    //name of the subcommand ex. /prank <subcommand> <-- that
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    //ex. "This is a subcommand that let's a shark eat someone"
    public abstract String getDescription();

    //How to use command ex. /prank freeze <player>
    public abstract String getSyntax();

    public String getPermission() {
        return "factions.commands." + getName();
    }

    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    public boolean hasCooldown(Player p) {
        if (!commandCooldowns.get(this).containsKey(p)) return false;
        return commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        commandCooldowns.put(this, hashMap);
    }

    public abstract int getCooldown();

    //code for the subcommand
    public abstract void perform(Player p, String[] args);

    public void perform(CommandSender cs, String[] args) {

    }
}