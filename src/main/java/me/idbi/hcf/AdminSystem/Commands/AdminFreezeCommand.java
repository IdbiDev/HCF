package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminFreezeCommand extends SubCommand {
    @Override
    public String getName() {
        return "freeze";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Freeze the selected player";
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " <player>";
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
        if (Bukkit.getPlayer(args[1]) == null) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        HCFPlayer player = HCFPlayer.getPlayer(p);
        boolean state = player.isFreezed();
        // Freeze
        if (!state) {
            p.sendMessage(Messages.freeze_executor_on.language(p).setPlayer(p).queue());
            target.sendMessage(Messages.freeze_player_on.language(p).setExecutor(p).queue());
        } else {
            //UnFreeze
            p.sendMessage(Messages.freeze_executor_off.language(p).setPlayer(p).queue());
            target.sendMessage(Messages.freeze_player_off.language(p).setExecutor(p).queue());
        }
        player.setFreezed(!player.isFreezed());
    }
}
