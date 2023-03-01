package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionStuckCommand extends SubCommand {
    @Override
    public String getName() {
        return "stuck";
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public boolean isCommand(String name) {
        return name.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Stucks player to a safe location";
    }

    @Override
    public String getSyntax() {
        return "/faction stuck";
    }

    @Override
    public void perform(Player p, String[] args) {
        if (!HCF_Timer.checkStuckTimer(p)) {
            //Todo: somethingwrite
            HCF_Timer.addStuckTimer(p);
            p.sendMessage(Messages.stuck_started.language(p).setAmount(String.valueOf(Main.stuckDuration)).queue());
            HCFPlayer player = HCFPlayer.getPlayer(p);
            player.setStuckLocation(new Location(
                    p.getWorld(),
                    p.getLocation().getBlockX(),
                    p.getLocation().getBlockY(),
                    p.getLocation().getBlockZ()));
        }
    }
}
