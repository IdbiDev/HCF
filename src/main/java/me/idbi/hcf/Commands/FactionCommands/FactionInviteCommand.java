package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.ClickableMessages.Clickable_Join;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

public class FactionInviteCommand extends SubCommand {
    @Override
    public String getName() {
        return "invite";
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
        return "/faction invite <player>";
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
    public void perform(Player p, String[] args) {
        if (Playertools.getPlayerFaction(p) != null) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_INVITE)) {
                //Todo nincs jog
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                if (Playertools.getPlayerFaction(target) == null) {
                    Faction faction = Playertools.getPlayerFaction(p);

                    if (!faction.isPlayerInvited(target)) {
                        faction.invitePlayer(target);
                        // Broadcast both player the invite successfully

                        p.sendMessage(Messages.invited_player.language(p).setPlayer(target).queue());

                        Clickable_Join.sendMessage(target,
                                "/f join " + faction.name,
                                Messages.invited_by.language(p).setPlayer(p).queue(),
                                Messages.hover_join.language(p).queue());

                        //target.sendMessage(Messages.INVITED_BY.repExecutor(p).setFaction(faction.factioname).queue());
                        //Invite kiírása a faction számára
                        //Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));

                        for (Player member : faction.getMembers()) {
                            member.sendMessage(Messages.faction_invite_broadcast.language(member).setExecutor(p).setPlayer(target).queue());
                        }

                        faction.inviteHistory.add(0, new HistoryEntrys.InviteEntry(
                                p.getName(),
                                target.getName(),
                                new Date().getTime(),
                                true
                        ));
                    } else {
                        // This player is already invited
                        p.sendMessage(Messages.already_invited.language(p).queue());
                    }
                } else {
                    p.sendMessage(Messages.player_in_faction.queue());
                }
            } else {
                p.sendMessage(Messages.not_found_player.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_invited.language(p).queue());
        }
    }
}
