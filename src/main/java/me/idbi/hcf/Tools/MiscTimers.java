package me.idbi.hcf.Tools;


import me.idbi.hcf.Bossbar.BossbarTools;
import me.idbi.hcf.Classes.ClassSelector;
import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Bard;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static me.idbi.hcf.Koth.Koth.GLOBAL_TIME;
import static me.idbi.hcf.Koth.Koth.stopKoth;

public class MiscTimers {
    private final HashMap<HCFPlayer, BukkitTask> warmup_tasks = new HashMap<>();
    Bard bard = new Bard();
    public void addClassToPlayer(Player player) {
        HCFPlayer p = HCFPlayer.getPlayer(player);
        bard.useSimpleBardEffect(player);
        if (p.isClassWarmup()) return;
        BukkitTask task;
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!ClassSelector.isPlayerWearingValidClass(player))
                    cancel();

                else
                    ClassSelector.addClassToPlayer(player);
                p.setClassWarmup(false);
                Timers.CLASS_WARMUP.remove(p);
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60);
        warmup_tasks.put(p, task);
    }
    public void DTRTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Faction f : Main.factionCache.values()) {
                    if(f.isDTRRegenEnabled()) {
                        if(f.getDTR() != f.getDTR_MAX()) {
                            if (f.getDTR_TIMEOUT() <= System.currentTimeMillis()){
                                f.setDTR_TIMEOUT(0L);
                                f.setDTR(f.getDTR_MAX());
                            }
                        }
                    }
                }

                HashMap<LivingEntity, Long> savedPlayers = Main.savedPlayers;
                for (Map.Entry<LivingEntity, Long> entry : savedPlayers.entrySet()) {
                    LivingEntity key = entry.getKey();
                    long val = entry.getValue();
                    if (val <= 0) {
                        key.remove();
                        Main.savedPlayers.remove(key);
                        Main.savedItems.remove(key);
                    } else {
                        Main.savedPlayers.put(key, val - 1000);
                    }
                }

                for (Player onlines : Bukkit.getOnlinePlayers()) {
                    HCFPlayer hcf = HCFPlayer.getPlayer(onlines);
                    PlayerStatistic stats = hcf.getPlayerStatistic();
                    stats.TimePlayed += 1000L;
                    Classes clss = hcf.getPlayerClass();
                    if (clss == Classes.ASSASSIN) {
                        stats.TotalAssassinClassTime += 1000L;
                    }
                    if (clss == Classes.ARCHER) {
                        stats.TotalArcherClassTime += 1000L;
                    }
                    if (clss == Classes.BARD) {
                        stats.TotalBardClassTime += 1000L;
                    }
                    if (clss == Classes.MINER) {
                        stats.TotalMinerClassTime += 1000L;
                    }

                }

            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 20);
    }

    public void mainRefresher() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);

                    runNowExpires(player);
                    executeClassWarmup(player);
                    refreshScoreboard(player);

                    if (hcfPlayer.getPlayerClass() != Classes.BARD) continue;
                    if (!(hcfPlayer.getBardEnergy() >= bard.maxBardEnergy)) {
                        hcfPlayer.addBardEnergy(0.1 * bard.bardEnergyMultiplier);
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
    }

    public void KOTHCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Koth.GLOBAL_AREA != null && Koth.GLOBAL_PLAYER != null) {
                    //TODO: Scoreboard format MIN:SS
                    GLOBAL_TIME--;
                    if (GLOBAL_TIME % 30 == 0)
                        Bukkit.broadcastMessage(Messages.koth_capture_timer
                                .setFaction(Koth.GLOBAL_AREA.getFaction()).setFormattedTime(GLOBAL_TIME).queue());
                    //  Bukkit.broadcastMessage(Messages.KOTH_CAPTURE_TIMER.setFaction(KOTH.GLOBAL_AREA.faction.factioname).repTime_formatted(GLOBAL_TIME).queue());
                    if (GLOBAL_TIME <= 0) {
                        stopKoth();
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            BossbarTools.remove(onlinePlayer);
                        }
                    }
                }

//                for (Player online : Bukkit.getOnlinePlayers()) {
//                    BarUtil.setBar(online, "Igen nem §cTe köcsög §cigen " + GLOBAL_TIME, 50);
//                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
    }

    public void createFakeWalls() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    SpawnShield.CalcWall(player);
                    if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) return;
                    ListIterator<Location> it = Main.playerBlockChanges.get(player.getUniqueId()).listIterator();
                    try {
                        while (it.hasNext()) {
                            Location loc = it.next();
                            if (loc.distance(player.getLocation()) > 12) {
                                player.sendBlockChange(loc, Material.AIR, (byte) 0);
                                it.remove();
                            }
                        }
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }

            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 1);
    }
    public void removeFakeWalls(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) return;
                ListIterator<Location> it = Main.playerBlockChanges.get(player.getUniqueId()).listIterator();
                try {
                    while (it.hasNext())
                        player.sendBlockChange(it.next(), Material.AIR, (byte) 0);
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }

            }
        }.runTaskAsynchronously(Main.getPlugin(Main.class));
    }

    public void autoSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Main.saveAll();
                } catch (Exception asd){
                    asd.printStackTrace();
                }
                Runtime runtime = Runtime.getRuntime();
                long totalMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();


            }
        }.runTaskTimer(Main.getPlugin(Main.class), 6000, 6000);
    }
}

