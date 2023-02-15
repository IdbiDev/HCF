package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.FactionHistory;
import me.idbi.hcf.tools.Objects.HCFPlayer;
import me.idbi.hcf.tools.Objects.PlayerStatistic;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

public class Faction_Disband {
    private static final Connection con = Main.getConnection("faction");

    public static void disband(Player p, String faction) {
        Faction selectedFaction = playertools.getPlayerFaction(p);
        if (playertools.getFactionByName(faction) != selectedFaction) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            GUI_Sound.playSound(p,"error");
            return;
        }


        if (!Objects.equals(selectedFaction.leader, p.getUniqueId().toString())) {
            //Todo: Nope factionLeader
            p.sendMessage(Messages.not_leader.language(p).queue());
            GUI_Sound.playSound(p,"error");
            return;
        }
//        displayTeams.removePlayerFromTeam(p);
//        displayTeams.removeTeam(selectedFaction);
        selectedFaction.selfDestruct();
        for (HCFPlayer player : selectedFaction.members) {
            Player bukkitPlayer = Bukkit.getPlayer(player.uuid);

            final String rankName = player.rank.name;

            player.removeFaction();
            if(bukkitPlayer != null) {
                Scoreboards.refresh(bukkitPlayer);
                NameChanger.refresh(bukkitPlayer);
            }


            PlayerStatistic stat = player.playerStatistic;
            for(FactionHistory f : stat.factionHistory){
                if(f.id == selectedFaction.id){
                    f.left = new Date();
                    f.cause = "Disbanded";
                    f.lastRole = rankName;
                    f.name = selectedFaction.name;
                }
            }
        }
        // LogLibrary.sendFactionDisband(p, selectedFaction.name);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(Messages.faction_disband
                    .language(onlinePlayer)
                    .setPlayer(p)
                    .setFaction(selectedFaction.name).queue());
        }

        Scoreboards.refresh(p);
        GUI_Sound.playSound(p,"success");
        NameChanger.refresh(p);
        NameChanger.refreshAll();
        //Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(p).setFaction(selectedFaction.factioname).queue());
    }
}
