package me.idbi.hcf.Commands.CustomTimer;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import org.bukkit.entity.Player;

import java.util.Map;

public class CustomTimerList extends SubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Lists all the custom created factions.";
    }

    @Override
    public String getSyntax() {
        return "/customtimer " + getName();
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public String getPermission() {
        return "factions.commands.customtimer." + getName();
    }
    @Override
    public void perform(Player p, String[] args) {
        if (Main.customSBTimers.isEmpty()) {
            p.sendMessage(Messages.customt_no_active_timer.language(p).queue());
            return;
        }
        p.sendMessage(" ");
        int counter = 1;
        for (
                Map.Entry<String, CustomTimers> cus : Main.customSBTimers.entrySet()) {
            p.sendMessage("§3#" + counter + " §b" + cus.getKey() + " §7(" + (cus.getValue().isActive() ? "§aActive§7" : "§cExpired§7")
                    + ")§7: §r" + cus.getValue().text);
            p.sendMessage("§7§o(( Expire in §b" + Formatter.formatMMSS((cus.getValue().getTime())) + "§7§o ))");
            p.sendMessage(" ");
            counter++;
        }
    }
}
