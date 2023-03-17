package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class FactionHomeCommand extends SubCommand implements Listener {
    @Override
    public String getName() {
        return "home";
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
        return "/faction home";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getName());
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
        if(HCF_Timer.getHomeTime(p) != 0) {
            p.sendMessage(Messages.already_home_teleporting.language(p).queue());
            return;
        }
        teleportToHome(p);
        addCooldown(p);
    }

    public static void teleportToHome(Player p) {
        Faction faction = Playertools.getPlayerFaction(p);
        if(faction == null){
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }

        Location loc = faction.getHomeLocation();
        if(loc != null) {
            p.sendMessage(Messages.teleport_to_home.language(p).setTime(Config.TeleportHome.asStr()).queue());
            //p.sendMessage(Messages.teleport_cancel.language(p).queue().replace("%time%", Config.TeleportHome.asStr()));
            HCF_Timer.addHomeTimer(p);
        } else {
            p.sendMessage(Messages.doesnt_home.language(p).queue());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            if (HCF_Timer.getHomeTime(e.getPlayer()) != 0) {
                HCF_Timer.removeHomeTimer(e.getPlayer());
                e.getPlayer().sendMessage(Messages.teleport_cancel.language(e.getPlayer()).queue());
            }
        }
    }
}
