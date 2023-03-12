package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class FactionStuckCommand extends SubCommand implements Listener {
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
        return "factions.commands." + getName();
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
        if (!HCF_Timer.checkStuckTimer(p)) {
            //Todo: somethingwrite
            HCF_Timer.addStuckTimer(p);
            p.sendMessage(Messages.stuck_started.language(p).setAmount(String.valueOf(Main.stuckDuration)).queue());
            HCFPlayer player = HCFPlayer.getPlayer(p);
            addCooldown(p);
            player.setStuckLocation(new Location(
                    p.getWorld(),
                    p.getLocation().getBlockX(),
                    p.getLocation().getBlockY(),
                    p.getLocation().getBlockZ()));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(e.getPlayer());
        if(hcfPlayer.stuckLocation == null) return;
        Location originalLocation = hcfPlayer.stuckLocation;
        if(originalLocation.distance(e.getPlayer().getLocation()) > 4) {
            if (HCF_Timer.getStuckTime(e.getPlayer()) != 0) {
                HCF_Timer.removeStuckTimer(e.getPlayer());
                e.getPlayer().sendMessage(Messages.stuck_interrupted.language(e.getPlayer()).queue());
            }
        }
    }
}
