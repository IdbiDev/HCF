package me.idbi.hcf.Commands.AllyCommands;

import me.idbi.hcf.ClickableMessages.Clickable_Join;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class AllyInviteCommand extends SubCommand {
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public boolean isCommand(String argument) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/ally " + getName() + " <faction>";
    }

    @Override
    public String getPermission() {
        return "factions.command.ally." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        invite(p, args[1]);
    }

    public static void invite(Player p, String name) {
        Faction target = Playertools.getFactionByName(name);
        Faction playerFaction = Playertools.getPlayerFaction(p);
        if (playerFaction != null) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                //Todo nincs jog
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            if (target != null && target != playerFaction) {
                if (Playertools.isFactionOnline(target)) {
                    //Faction faction = Main.faction_cache.get(Integer.valueOf(playertools.getMetadata(p, "factionid")));

                    if (!playerFaction.isFactionAllyInvited(target) && !playerFaction.isAlly(target)) {
                        playerFaction.inviteFactionAlly(target);
                        // Broadcast both player the invite successfully

                        // p.sendMessage(Messages.invited_player.language(p).setPlayer(target).queue());

                        /*Clickable_Join.sendMessage(target,
                                "/f join " + faction.name,
                                Messages.invited_by.language(p).setPlayer(p).queue(),
                                Messages.hover_join.language(p).queue());


                        for (Player member : faction.getMembers()) {
                            member.sendMessage(Messages.faction_invite_broadcast.language(member).setExecutor(p).setPlayer(target).queue());
                        }*/
                        for (Player member : Playertools.getFactionOnlineMembers(target)) {

                            Clickable_Join.sendMessage(member,
                                    "/ally accept " + playerFaction.name,
                                    Messages.faction_invited_ally.language(member).setFaction(playerFaction).setPlayer(p).queue(),
                                    Messages.hover_accept.language(member).queue());
                        }
                        for (Player member : Playertools.getFactionOnlineMembers(playerFaction)) {
                            member.sendMessage(Messages.faction_invited_ally.language(member).setFaction(target).queue());
                        }
                    } else {
                        p.sendMessage(Messages.already_invited_ally.language(p).queue());
                    }
                } else {
                    p.sendMessage(Messages.not_found_faction.language(p).queue());
                }
            } else {
                p.sendMessage(Messages.not_found_faction.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
