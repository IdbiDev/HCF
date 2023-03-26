package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class FactionPromoteCommand extends SubCommand {
    @Override
    public String getName() {
        return "promote";
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
        return "/f " + getName() + " <Player>";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        return false;
    }

    @Override
    public void addCooldown(Player p) {

    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        HCFPlayer hcfTarget = HCFPlayer.getPlayer(args[1]);
        if(hcfPlayer.inFaction()) {
            if(hcfPlayer.getRank().hasPermission(FactionRankManager.Permissions.MANAGE_RANKS)) {
                if(hcfTarget != null) {
                    if(!hcfPlayer.equals(hcfTarget)) {
                        if(hcfPlayer.getFaction().equals(hcfTarget.getFaction())){
                            if(hcfTarget.getRank().getPriority() < hcfPlayer.getRank().getPriority()) {
                                Faction f = hcfTarget.getFaction();
                                int priority = 9999;
                                int activeRankPriority = hcfTarget.getRank().getPriority();
                                FactionRankManager.Rank nextRank = null;

                                for(FactionRankManager.Rank rank : f.getRanks()) {
                                    if(rank.isLeader() ||hcfPlayer.getRank().getPriority() > rank.getPriority()) continue;
                                    if(rank.getPriority() < priority && rank.getPriority() > activeRankPriority) {

                                        nextRank= rank;
                                    }
                                }
                                if(nextRank == null) {
                                    p.sendMessage(Messages.);
                                    return;
                                }
                                hcfTarget.setRank(nextRank);
                            } else {
                                p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
                            }
                        } else {
                            p.sendMessage(Messages.not_found_player.language(p).queue());
                        }
                    } else {
                       p.sendMessage(Messages.player_cant_self_promote.language(p).queue());
                    }
                } else {
                    p.sendMessage(Messages.not_found_player.language(p).queue());
                }
            } else {
                p.sendMessage(Messages.no_permission.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
