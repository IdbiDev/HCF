package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "slots",
        description = "Set server's slot",
        permission = "factions.commands.admin.slots",
        syntax = "/slots set <new slot>")
public class SlotsCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        if(args.length == 0) {
            p.sendMessage(Messages.server_slot_set.language(p).setNumber(Main.serverSlot).queue());
            return;
        }
        //
        if(args[0].equalsIgnoreCase("reset")) {
            Main.serverSlot = Bukkit.getMaxPlayers();
            p.sendMessage(Messages.server_slot_set.language(p).setNumber(Main.serverSlot).queue());
            addCooldown(p);
            return;
        }
        if(!args[0].equalsIgnoreCase("set")) {
            printUsage(p);
            return;
        }
        if(!Playertools.isInt(args[1])) return;
        if(args.length == 2) {
            long argSlot = Integer.parseInt(args[1]);
            if(argSlot >= Integer.MAX_VALUE) return;
            Main.serverSlot = Math.toIntExact(argSlot);
            p.sendMessage(Messages.server_slot_set.language(p).setNumber(Main.serverSlot).queue());
            addCooldown(p);
        }
    }
}
