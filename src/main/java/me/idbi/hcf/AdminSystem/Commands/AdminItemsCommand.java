package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.Tools.Objects.Crowbar;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class AdminItemsCommand extends SubCommand {

    @Override
    public String getName() {
        return "items";
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
        return "/admin " + getName() + " help";
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

    }

    public void perform(CommandSender cs, String[] args) {
        if(args[1].equalsIgnoreCase("help")) {
            cs.sendMessage("§cUsage: /admin items <player> <item>");
            cs.sendMessage("§cItems: §7'§ccrowbar§7'");
        } else {
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null) return;
            if(args[2].equalsIgnoreCase("crowbar")) {
                ItemStack is = Crowbar.is();
                player.getInventory().addItem(is);
                //ToDo: Message
                cs.sendMessage("§aSuccessfully gived!");
            }
        }
    }
}
