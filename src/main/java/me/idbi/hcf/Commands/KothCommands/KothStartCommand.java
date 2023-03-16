package me.idbi.hcf.Commands.KothCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.entity.Player;

import static me.idbi.hcf.Koth.Koth.getKothFromName;
import static me.idbi.hcf.Koth.Koth.startKoth;

public class KothStartCommand extends SubCommand {
    @Override
    public String getName() {
        return "start";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) ;
    }

    @Override
    public String getDescription() {
        return "Starts the selected koth";
    }

    @Override
    public String getSyntax() {
        return "/koth " + getName() + "<Koth name>";
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
        if (getKothFromName(args[1]) != 0) {
            startKoth(p,args[1]);
            //Todo: koth started @everyone
        } else {
            p.sendMessage(Messages.koth_invalid_name.language(p).queue());
        }
    }
}
