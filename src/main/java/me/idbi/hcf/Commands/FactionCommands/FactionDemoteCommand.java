package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class FactionDemoteCommand extends SubCommand {
    @Override
    public String getName() {
        return "demote";
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
                                ArrayList<FactionRankManager.Rank> ranks = f.getRanks();
                                FactionRankManager.Rank rank = hcfTarget.getRank();
                                FactionRankManager.Rank nextRank;
                                try {
                                    nextRank = ranks.get(ranks.indexOf(rank)-1);
                                }catch (IndexOutOfBoundsException ignored) {
                                    p.sendMessage(Messages.no_permission_in_faction.language(hcfPlayer).queue());
                                    return;
                                }
                                hcfTarget.setRank(nextRank);
                                Player target = Bukkit.getPlayer(hcfTarget.getUUID());
                                if(target != null)
                                    target.sendMessage(Messages.demote_message.language(target).setExecutor(p).setRank(nextRank.getName()).queue());
                                p.sendMessage(Messages.executor_demote_message.language(target).setPlayer(hcfTarget).setRank(nextRank.getName()).queue());
                                for(HCFPlayer _player : f.getMembers()) {
                                    Player _player_  = Bukkit.getPlayer(_player.getUUID());
                                    if(_player_ != null)
                                        _player_.sendMessage(Messages.executor_demote_broadcast_message.language(_player).setExecutor(hcfPlayer).setPlayer(hcfTarget).setRank(nextRank.getName()).queue());
                                }
                            } else {
                                p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
                            }
                        } else {
                            p.sendMessage(Messages.not_found_player.language(p).queue());
                        }
                    } else {
                        p.sendMessage(Messages.player_cant_self_demote.language(p).queue());
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
