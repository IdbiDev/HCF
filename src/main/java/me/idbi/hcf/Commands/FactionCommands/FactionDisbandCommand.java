package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.FactionHistory;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Objects;

public class FactionDisbandCommand extends SubCommand {
    @Override
    public String getName() {
        return "disband";
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
        return "/faction disband <faction>";
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction selectedFaction = Playertools.getPlayerFaction(p);
        if (selectedFaction == null) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
            return;
        }
        if (Playertools.getFactionByName(args[1]) != selectedFaction) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
            return;
        }


        if (!Objects.equals(selectedFaction.leader, p.getUniqueId().toString())) {
            //Todo: Nope factionLeader
            p.sendMessage(Messages.not_leader.language(p).queue());
            GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
            return;
        }
//        displayTeams.removePlayerFromTeam(p);
//        displayTeams.removeTeam(selectedFaction);
        for (HCFPlayer player : selectedFaction.members) {
            Player bukkitPlayer = Bukkit.getPlayer(player.uuid);

            final String rankName = player.rank.name;

            //player.removeFaction();
            player.setFactionWithoutRank(null);
            player.setRank(null);

            if (bukkitPlayer != null) {
                Scoreboards.refresh(bukkitPlayer);
                NameChanger.refresh(bukkitPlayer);
            }


            PlayerStatistic stat = player.playerStatistic;
            for (FactionHistory f : stat.factionHistory) {
                if (f.id == selectedFaction.id) {
                    f.left = new Date();
                    f.cause = "Disbanded";
                    f.lastRole = rankName;
                    f.name = selectedFaction.name;
                }
            }
        }

        selectedFaction.members.clear();

        // LogLibrary.sendFactionDisband(p, selectedFaction.name);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(Messages.faction_disband
                    .language(onlinePlayer)
                    .setPlayer(p)
                    .setFaction(selectedFaction.name).queue());
        }

        Scoreboards.refresh(p);
        GUI_Sound.playSound(p, GUI_Sound.HCFSounds.SUCCESS);
        NameChanger.refresh(p);
        NameChanger.refreshAll();
        selectedFaction.selfDestruct();
        //Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(p).setFaction(selectedFaction.factioname).queue());
    }
}
