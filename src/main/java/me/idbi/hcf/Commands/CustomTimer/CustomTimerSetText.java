package me.idbi.hcf.Commands.CustomTimer;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CustomTimerSetText extends SubCommand {

    @Override
    public String getName() {
        return "settext";
    }

    @Override
    public String getDescription() {
        return "Lists all the custom created factions.";
    }

    @Override
    public String getSyntax() {
        return "/customtimer " + getName() + " <name> <new text>";
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
        if (args.length == 3) {
            if (!CustomTimers.isCreated(args[1])) {
                p.sendMessage(Messages.customt_not_found.language(p).queue());
                return;
            }
            CustomTimers timer = Main.customSBTimers.get(args[1]);
            String mainText = ChatColor.translateAlternateColorCodes('&', args[2].replace("_", " "));
            timer.setText(mainText);
            p.sendMessage(Messages.customt_edited.language(p).queue());
        }
    }
}
