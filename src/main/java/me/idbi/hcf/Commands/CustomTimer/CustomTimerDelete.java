package me.idbi.hcf.Commands.CustomTimer;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import org.bukkit.entity.Player;

public class CustomTimerDelete extends SubCommand {

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Lists all the custom created factions.";
    }

    @Override
    public String getSyntax() {
        return "/customtimer " + getName() + " <name>";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
    @Override
    public String getPermission() {
        return "factions.commands.customtimer." + getName();
    }
    @Override
    public void perform(Player p, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                if (CustomTimers.isCreated(args[1])) {
                    Main.customSBTimers.get(args[1]).delete();
                    p.sendMessage(Messages.customt_deleted.language(p).queue());
                } else {
                    p.sendMessage(Messages.customt_not_found.language(p).queue());
                }
            }
        }
    }
}
