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
        return "Demotes the selected to the next lower rank. Use the manage GUI if you want to manually select the rank.";
    }

    @Override
    public String getSyntax() {
        return "/faction " + getName() + " <Player>";
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        HCFPlayer hcfTarget = HCFPlayer.getPlayer(args[1]);
        if (!hcfPlayer.inFaction()) {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }

        if (!hcfPlayer.getRank().hasPermission(FactionRankManager.Permissions.MANAGE_RANKS)) {
            p.sendMessage(Messages.no_permission.language(p).queue());
            return;
        }

        if (hcfTarget == null) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }

        if (hcfPlayer.equals(hcfTarget)) {
            p.sendMessage(Messages.player_cant_self_demote.language(p).queue());
            return;
        }
        if (hcfTarget.getRank().isLeader() || hcfTarget.getRank().isDefault() ) {
            p.sendMessage(Messages.cant_demote.language(p).setPlayer(hcfTarget).queue());
            return;
        }
        if (!hcfPlayer.getFaction().equals(hcfTarget.getFaction())) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }

        if (hcfTarget.getRank().getPriority() >= hcfPlayer.getRank().getPriority()) {
            p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
            return;
        }

        Faction f = hcfTarget.getFaction();
        ArrayList<FactionRankManager.Rank> ranks = f.getRanks();
        FactionRankManager.Rank rank = hcfTarget.getRank();

        int index = ranks.indexOf(rank);
        if (index == -1) {
            p.sendMessage(Messages.not_found_player.language(p).queue());
            return;
        }

        FactionRankManager.Rank nextRank = null;
        try {
            nextRank = ranks.get(index - 1);
        } catch (IndexOutOfBoundsException ignored) {
            p.sendMessage(Messages.no_permission_in_faction.language(hcfPlayer).queue());
            return;
        }

        hcfTarget.setRank(nextRank);

        Player target = Bukkit.getPlayer(hcfTarget.getUUID());
        if (target != null) {
            target.sendMessage(Messages.demote_message.language(target).setExecutor(p).setRank(nextRank.getName()).queue());
        }

        p.sendMessage(Messages.executor_demote_message.language(target).setPlayer(hcfTarget).setRank(nextRank.getName()).queue());

        for (HCFPlayer _player : f.getMembers()) {
            Player _player_ = Bukkit.getPlayer(_player.getUUID());
            if (_player_ != null) {
                _player_.sendMessage(Messages.executor_demote_broadcast_message.language(_player)
                        .setExecutor(hcfPlayer).setPlayer(hcfTarget).setRank(nextRank.getName()).queue());
            }
        }
    }
}
