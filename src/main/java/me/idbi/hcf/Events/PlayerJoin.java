package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.TabManager.TabManager;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class PlayerJoin implements Listener {
    private final Main m = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        //Main.scoreboardManager.start();
       //
        //
        // p.setScoreboard(ScoreboardBuilder.getOrCreate(p).build());

        //TabManager.test(e.getPlayer());
        //TabManager.getManager().refresh(e.getPlayer());

        /*FastBoard board = new FastBoard(e.getPlayer());
        board.updateTitle(Config.DefaultScoreboardTitle.asStr());
        Main.boards.put(e.getPlayer().getUniqueId(), board);*/

        Playertools.loadOnlinePlayer(p);
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        //hcf.createScoreboard(p);

        if (Main.deathWaitClear.contains(p.getUniqueId())) {
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setHealth(e.getPlayer().getMaxHealth());
            p.setFoodLevel(20);
            p.setFallDistance(0);


            Main.deathWaitClear.remove(e.getPlayer().getUniqueId());
            /*String str = Config.SpawnLocation.asStr();

            Location spawn = new Location(
                    Bukkit.getWorld(Config.WorldName.asStr()),
                    Integer.parseInt(str.split(" ")[0]),
                    Integer.parseInt(str.split(" ")[1]),
                    Integer.parseInt(str.split(" ")[2]),
                    Integer.parseInt(str.split(" ")[3]),
                    Integer.parseInt(str.split(" ")[4])
            );*/
            e.getPlayer().teleport(Main.getSpawnLocation());
            //pvpTimer
            if(!Main.SOTWEnabled)
                Timers.PVP_TIMER.add(e.getPlayer());
        }
        if(Main.SOTWEnabled) {
            Timers.SOTW.add(p, Main.SOTWStarted + Config.SOTWDuration.asInt() * 1000L - System.currentTimeMillis());
        }
        e.setJoinMessage("");
        List<UUID> list = new ArrayList<>(AdminTools.InvisibleManager.getInvisedAdmins());
        for (UUID admins : list) {
            Player player = Bukkit.getPlayer(admins);
            if(player == null) continue;
            AdminTools.InvisibleManager.hidePlayer(player);
        }


        if (hcf.inFaction()) { // írjad csöves
            Faction f = Playertools.getPlayerFaction(e.getPlayer());
            if (f != null) {
                for (Player member : f.getOnlineMembers()) {
                    member.sendMessage(Messages.join_online_player_in_faction.language(member).setPlayer(e.getPlayer()).queue());
                }
                //f.addPrefixPlayer(p);
                //Bukkit.getScoreboardManager().getMainScoreboard().getTeam(f.name).addEntry(p.getName());
                f.joinLeftHistory.add(0, new HistoryEntrys.JoinLeftEntry(p.getName(), true, new Date().getTime()));
            }
        }

        // displayTeams.setupPlayer(e.getPlayer());
        // displayTeams.addPlayerToTeam(e.getPlayer());

        NameChanger.refreshAll();

        for (Map.Entry<LivingEntity, Long> entity : Main.savedPlayers.entrySet()) {
            if (Bukkit.getPlayer(entity.getKey().getCustomName()) != null) {
                if (Bukkit.getPlayer(entity.getKey().getCustomName()).isOnline()) {
                    entity.getKey().remove();

                    Main.savedPlayers.remove(entity.getKey());
                    Main.savedItems.remove(entity.getKey());
                }
            }
        }

        PlayerStatistic statistic = hcf.getPlayerStatistic();
        p.sendMessage("Started:" + new Date(statistic.startDate));
        p.sendMessage("Last Login:" + new Date(statistic.lastLogin));
        p.sendMessage("Time Played" + (statistic.TimePlayed / 1000) / 60);
        p.sendMessage("Total classes" + (statistic.TotalClassTime / 1000) / 60);
    }
}
