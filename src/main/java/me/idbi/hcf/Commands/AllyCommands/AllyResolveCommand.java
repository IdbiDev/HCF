package me.idbi.hcf.Commands.AllyCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class AllyResolveCommand extends SubCommand {
    @Override
    public String getName() {
        return "resolve";
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
        Faction allyFaction = Playertools.getFactionByName(args[1]);
        Faction playerFaction = Playertools.getPlayerFaction(p);
        if (playerFaction != null) {
            if (allyFaction != null) {
                if (Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                    if (playerFaction.isAlly(allyFaction)) {
                        playerFaction.resolveFactionAlly(allyFaction);
                        NameChanger.refreshTeams(p);
                        for (Player member : Playertools.getFactionOnlineMembers(allyFaction)) {
                            NameChanger.refresh(member);
                        }
                    } else {
                        p.sendMessage("no alli(Hard code uwu)");
                        //TOdo: Message: no ally yet
                    }
                } else {
                    p.sendMessage(Messages.no_permission.language(p).queue());
                }
            } else {
                p.sendMessage(Messages.not_found_faction.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
