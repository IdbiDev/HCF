package me.idbi.hcf.Commands.KothCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.entity.Player;

import static me.idbi.hcf.Koth.Koth.createKoth;

public class KothCreateCommand extends SubCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) ;
    }

    @Override
    public String getDescription() {
        return "Creates a koth faction";
    }

    @Override
    public String getSyntax() {
        return "/koth " + getName() + " <Koth name>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.koth." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        return false;
    }

    @Override
    public void addCooldown(Player p) {

    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        createKoth(args[1]);
        p.sendMessage(Messages.koth_created.language(p).setFaction(args[1]).queue());
    }
}
