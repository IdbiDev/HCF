package me.idbi.hcf.Commands.LivesCommand;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesGetCommand extends SubCommand {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/lives get <name>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.lives." + getName();
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {

    }

    @Override
    public void perform(CommandSender cs, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if(target != null) {
            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(target.getUniqueId());
            int currentLives = hcfPlayer.getLives();
            if(cs instanceof Player p) {
                p.sendMessage(Messages.lives_get.language(p).setPlayer(target).setLives(currentLives).queue());
            } else {
                cs.sendMessage(Messages.lives_get.setPlayer(target).setLives(currentLives).queue());
            }

        } else {
            if(cs instanceof Player p) {
                p.sendMessage(Messages.not_found_player.language(p).queue());
            } else {
                cs.sendMessage(Messages.not_found_player.queue());
            }
        }
    }
}
