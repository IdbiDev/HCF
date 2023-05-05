package me.idbi.hcf.Commands.AllyCommands;

import me.idbi.hcf.ClickableMessages.Clickable_Join;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AllyInviteCommand extends SubCommand {
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
        return "/ally " + getName() + " <faction>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.ally." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        invite(p, args[1]);
        addCooldown(p);
    }

    public static void invite(Player p, String name) {
        Faction target = Playertools.getFactionByName(name);
        Faction playerFaction = Playertools.getPlayerFaction(p);
        if (playerFaction != null) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            if (target != null && target != playerFaction) {
                if (Playertools.isFactionOnline(target)) {
                    if (!playerFaction.isFactionAllyInvited(target) && !playerFaction.isAlly(target)) {
                        playerFaction.inviteFactionAlly(target);
                        for (Player member : Playertools.getFactionOnlineMembers(target)) {
                            Clickable_Join.sendMessage(member,
                                    "/ally accept " + playerFaction.getName(),
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
