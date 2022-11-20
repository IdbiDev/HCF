package me.idbi.hcf.commands;

import me.idbi.hcf.Enchantments.EnchantTools;
import me.idbi.hcf.Enchantments.Enchantments.PersistentTool;
import me.idbi.hcf.Enchantments.Enchants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchantmentCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("enchantment")) {
            if(sender instanceof Player p) {
                if(p.getItemInHand() == null) return false;
                if(args.length == 0) {
                    Bukkit.broadcastMessage(PersistentTool.getNBT(p.getItemInHand()));
                }

                else if(args.length == 3) {
                    if(args[0].equalsIgnoreCase("add")) {
                        if(args[1].matches("^[0-9]+$")) {
                            p.sendMessage("§cCsak számot!");
                            return false;
                        }
                        PersistentTool.addEnchant(p.getItemInHand(), Enchants.EnchantmentType.getByName(args[1]), Integer.parseInt(args[2]));
                        p.sendMessage("§a" + Enchants.EnchantmentType.getByName(args[1]).getName() + " added level: " +args[2]);
                    }
                }
            }
        }
        return false;
    }
}
