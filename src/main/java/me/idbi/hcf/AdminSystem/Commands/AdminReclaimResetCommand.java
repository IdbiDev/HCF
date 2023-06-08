package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.CustomFiles.ReclaimFile;
import me.idbi.hcf.Reclaim.ReclaimConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminReclaimResetCommand extends SubCommand {

    @Override
    public String getName() {
        return "resetreclaim";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Resets a player reclaim status. This will let the player reclaim again.";
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + "<all | <player>";
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
        if (!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
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
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) {
                ReclaimFile.get().set("ClaimedReclaims", null);
                ReclaimFile.save();
                p.sendMessage(Messages.reclaim_reset_all.language(p).queue());
            } else if (Bukkit.getPlayer(args[1]) != null) {
                Player target = Bukkit.getPlayer(args[1]);
                ReclaimConfig.resetClaim(target);
                p.sendMessage(Messages.reclaim_reset_player.language(p).setPlayer(target).queue());
            } else {
                p.sendMessage(Messages.not_found_player.language(p).queue());
            }
        } else {
            p.sendMessage("§cUsage: " + getSyntax());
        }
    }
}
