package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.*;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
        return "Disbands the faction. This process is can't be undo.";
    }

    @Override
    public String getSyntax() {
        return "/faction disband <faction>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction selectedFaction = Playertools.getPlayerFaction(p);
        if (selectedFaction == null) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
            return;
        }
        if (Playertools.getFactionByName(args[1]) != selectedFaction) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
            return;
        }


        if (!Objects.equals(selectedFaction.getLeader(), p.getUniqueId().toString())) {
            p.sendMessage(Messages.not_leader.language(p).queue());
            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
            return;
        }
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        hcfPlayer.setChatType(ChatTypes.PUBLIC);

        if(!hcfPlayer.getFaction().getClaims().isEmpty()) {
            double backmoney = Claiming.calculateMoneyFromClaim(hcfPlayer.getFaction());
            hcfPlayer.addMoney(Math.toIntExact(Math.round(backmoney)));
            hcfPlayer.getFaction().clearClaims(); // done

        }
//        displayTeams.removePlayerFromTeam(p);
//        displayTeams.removeTeam(selectedFaction);
        for (HCFPlayer player : new ArrayList<>(selectedFaction.getMembers())) {
            Player bukkitPlayer = Bukkit.getPlayer(player.getUUID());
            //player.setFactionWithoutRank(null);
            final String rankName = player.getRank().getName();
            //player.setRank(null);

            player.removeFaction();


            if (bukkitPlayer != null) {
                Scoreboards.refresh(bukkitPlayer);
                NameChanger.refresh(bukkitPlayer);
            }


            PlayerStatistic stat = player.getPlayerStatistic();
            for (FactionHistory f : stat.factionHistory) {
                if (f.id == selectedFaction.getId()) {
                    f.left = new Date();
                    f.cause = "Disbanded";
                    f.lastRole = rankName;
                    f.name = selectedFaction.getName();
                }
            }
        }

        addCooldown(p);
        selectedFaction.clearMembers();


        // LogLibrary.sendFactionDisband(p, selectedFaction.name);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(Messages.faction_disband
                    .language(onlinePlayer)
                    .setPlayer(p)
                    .setFaction(selectedFaction.getName()).queue());
        }

        Scoreboards.refresh(p);
        GUISound.playSound(p, GUISound.HCFSounds.SUCCESS);
        NameChanger.refresh(p);
        NameChanger.refreshAll();

        selectedFaction.selfDestruct();
        //Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(p).setFaction(selectedFaction.factioname).queue());
    }
}
