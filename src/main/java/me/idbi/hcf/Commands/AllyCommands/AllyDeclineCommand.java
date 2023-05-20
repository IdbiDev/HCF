package me.idbi.hcf.Commands.AllyCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AllyDeclineCommand extends SubCommand {
    @Override
    public String getName() {
        return "decline";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Declines an ally request.";
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
        if (!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
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
        addCooldown(p);
        Faction target = Playertools.getFactionByName(args[1]);
        Faction playerFaction = Playertools.getPlayerFaction(p);
        if (playerFaction == null) {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
            return;
        }
        if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
            p.sendMessage(Messages.no_permission.language(p).queue());
            return;
        }
        if (target == null) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }
        if (playerFaction.isFactionAllyInvited(target) && !playerFaction.isAlly(target)) {
            playerFaction.unInviteAlly(target);
            target.unInviteAlly(playerFaction);
            p.sendMessage(Messages.faction_decline_ally_success.language(p).setFaction(target).queue());
            for (Player member : Playertools.getFactionOnlineMembers(target)) {
                member.sendMessage(Messages.faction_decline_ally_target.language(member).setFaction(playerFaction).queue());
            }
        } else {
            p.sendMessage(Messages.already_invited_ally.language(p).queue());
        }
    }
}
