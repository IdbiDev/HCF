package me.idbi.hcf.Commands.FactionCommands;


import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class FactionUninviteCommand extends SubCommand {
    @Override
    public String getName() {
        return "uninvite"; // Uwwwu
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Uninvite an invited member.";
    }

    @Override
    public String getSyntax() {
        return "/faction " + getName() + " <player>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(player.inFaction()) {
            if(player.getRank().hasPermission(FactionRankManager.Permissions.MANAGE_INVITE)) {
                HCFPlayer hcfPlayer = HCFPlayer.getPlayer(args[1]);
                if(hcfPlayer != null) {
                    if(player.getFaction().isPlayerInvited(hcfPlayer)) {
                        OfflinePlayer targetPlayer = Bukkit.getPlayer(args[1]);
                        player.getFaction().unInvitePlayer(hcfPlayer);
                        p.sendMessage(Messages.uninvite_executor.language(p).setPlayer(hcfPlayer).queue());
                        if(targetPlayer.isOnline())
                            ((Player)targetPlayer).sendMessage(Messages.uninvite_target.language(p).setPlayer(player).queue());
                        addCooldown(p);
                    }else {
                        p.sendMessage(Messages.not_invited.language(p).queue());
                    }
                }else {
                    p.sendMessage(Messages.not_found_player.language(p).queue());
                }
            }else {
                p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
            }
        }else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
