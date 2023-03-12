package me.idbi.hcf.Commands.AllyCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.AllyTools;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AllyAcceptCommand extends SubCommand {
    @Override
    public String getName() {
        return "accept";
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
        return "factions.command.ally." + getName();
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
        Faction factionTarget = Playertools.getFactionByName(args[1]);
        Faction factionPlayer = Playertools.getPlayerFaction(p);
        if (factionPlayer != null) {
            if (factionTarget != null) {
                if (Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                    if (factionTarget.isFactionAllyInvited(factionPlayer)) {
                        AllyTools.addAlly(factionPlayer, factionTarget);
                        for (Player member : Playertools.getFactionOnlineMembers(factionPlayer)) {
                            member.sendMessage(Messages.joined_ally.language(member).setFaction(factionTarget).queue());
                        }
                        for (Player member : Playertools.getFactionOnlineMembers(factionTarget)) {
                            member.sendMessage(Messages.joined_ally.language(member).setFaction(factionPlayer).queue());
                            NameChanger.refresh(member);
                        }
                        addCooldown(p);
                        NameChanger.refreshTeams(p);
                    } else {
                        p.sendMessage(Messages.not_invited_ally.language(p).queue());
                    }
                } else {
                    p.sendMessage(Messages.no_permission.language(p).queue());
                }
            } else {
                //Nem vagy meghíva ebbe a facionbe
                p.sendMessage(Messages.not_found_faction.language(p).queue());
            }
        } else {
            // Már vagy egy factionbe
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
