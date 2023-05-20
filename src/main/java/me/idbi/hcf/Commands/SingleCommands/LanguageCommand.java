package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "language",
        description = "Select language",
        permission = "factions.commands.language",
        syntax = "/language <language>")
public class LanguageCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {

        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(args.length == 1) {
            if (Main.availableLanguages.contains(args[0])) {
                hcfPlayer.setLanguage(args[0].toLowerCase());
                p.sendMessage(Messages.language_set.language(p).setLanguage(args[0].toLowerCase()).queue());
            } else {
                p.sendMessage(Messages.language_not_found.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.language_current.language(p).setLanguage(hcfPlayer.getLanguage()).queue());
        }
        addCooldown(p);
    }
}
