package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminVanishCommand extends SubCommand {
    @Override
    public String getName() {
        return "vanish";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("v");
    }

    @Override
    public String getDescription() {
        return "This will make you invisible. Staffs can still see you.";
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
        if (!Playertools.isInStaffDuty(p)) {
            p.sendMessage(Messages.not_in_duty.language(p).queue());
            return;
        }
        boolean inVanish = AdminTools.InvisibleManager.invisedAdmins.contains(p.getUniqueId());
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
