package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Timers;
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
        return "Teleports the player to a safe location";
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
        if (!Timers.STUCK.has(p)) {
            //Todo: somethingwrite
            Timers.STUCK.add(p);
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
        if(hcfPlayer.getStuckLocation() == null) return;
        Location originalLocation = hcfPlayer.getStuckLocation();
        if(originalLocation.distance(e.getPlayer().getLocation()) > 4) {
            if (Timers.STUCK.has(e.getPlayer())) {
                Timers.STUCK.remove(e.getPlayer());
                e.getPlayer().sendMessage(Messages.stuck_interrupted.language(e.getPlayer()).queue());
            }
        }
    }
}
