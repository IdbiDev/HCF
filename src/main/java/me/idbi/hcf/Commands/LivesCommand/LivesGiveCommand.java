package me.idbi.hcf.Commands.LivesCommand;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesGiveCommand extends SubCommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Gives live to the selected player";
    }

    @Override
    public String getSyntax() {
        return "/lives give <name> <lives>";
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
            if(Playertools.isInt(args[2])) {
                int lives = Integer.parseInt(args[2]);
                hcfPlayer.addLives(lives);
                if(cs instanceof Player) {
                    Player p = (Player) cs;
                    p.sendMessage(Messages.lives_give_executor.language(p).setPlayer(target).setLives(lives).setLeftLives(hcfPlayer.getLives()).queue());
                    target.sendMessage(Messages.lives_give_player.language(target).setExecutor(p).setLives(lives).setLeftLives(hcfPlayer.getLives()).queue());
                } else {
                    cs.sendMessage(Messages.lives_give_executor.setPlayer(target).setLives(lives).setLeftLives(hcfPlayer.getLives()).queue());
                    target.sendMessage(Messages.lives_give_player.language(target).setExecutor("Console").setLives(lives).setLeftLives(hcfPlayer.getLives()).queue());
                }
            }
        } else {
            if(cs instanceof Player) {
                Player p = (Player) cs;
                p.sendMessage(Messages.not_found_player.language(p).queue());
            } else
                cs.sendMessage(Messages.not_found_player.queue());
        }
    }
}
