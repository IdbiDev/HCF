package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class AdminVanishCommand extends SubCommand {
    @Override
    public String getName() {
        return "vanish";
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
        return "/admin " + getName();
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
        if (!Playertools.isInStaffDuty(p)) {
            p.sendMessage(Messages.not_in_duty.language(p).queue());
            return;
        }
        boolean inVanish = AdminTools.InvisibleManager.invisedAdmins.contains(p);
        if (inVanish) {
            AdminTools.InvisibleManager.showPlayer(p);
            AdminScoreboard.refresh(p);
            p.sendMessage(Messages.vanish_disable.language(p).queue());
        } else {
            AdminTools.InvisibleManager.hidePlayer(p);
            AdminScoreboard.refresh(p);
            p.sendMessage(Messages.vanish_enabled.language(p).queue());
        }
    }
}
