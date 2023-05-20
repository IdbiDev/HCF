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
import java.util.HashMap;

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
        return "Invites a player to the faction.";
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
        if (Playertools.getPlayerFaction(p) != null) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_INVITE)) {
                p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
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

                        addCooldown(p);

                        Clickable_Join.sendMessage(target,
                                "/f join " + faction.getName(),
                                Messages.invited_by.language(p).setFaction(faction).setExecutor(p).queue(),
                                Messages.hover_join.language(p).queue());

                        //target.sendMessage(Messages.INVITED_BY.repExecutor(p).setFaction(faction.factioname).queue());
                        //Invite kiírása a faction számára
                        //Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));

                        for (Player member : faction.getOnlineMembers()) {
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
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
