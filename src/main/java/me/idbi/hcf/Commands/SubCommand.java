package me.idbi.hcf.Commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();

    //name of the subcommand ex. /prank <subcommand> <-- that
    public abstract boolean isCommand(String argument);

    //ex. "This is a subcommand that let's a shark eat someone"
    public abstract String getDescription();

    //How to use command ex. /prank freeze <player>
    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract boolean hasPermission(Player p);

    //code for the subcommand
    public abstract void perform(Player p, String[] args);
}