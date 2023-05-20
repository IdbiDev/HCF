package me.idbi.hcf.Commands.KothCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;
import static me.idbi.hcf.Koth.Koth.stopKoth;

public class KothStopCommand  extends SubCommand {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) ;
    }

    @Override
    public String getDescription() {
        return "Stops the currently going KoTH!";
    }

    @Override
    public String getSyntax() {
        return "/koth " + getName();
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
        Faction f = Playertools.getFactionByName(args[1].replaceAll("_", " "));
        if (f != null) {
            stopKoth();
        } else {
            p.sendMessage(Messages.koth_invalid_name.language(p).queue());
        }
    }
}
