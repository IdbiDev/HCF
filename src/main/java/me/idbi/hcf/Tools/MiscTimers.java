package me.idbi.hcf.Tools;


import me.idbi.hcf.Classes.ClassSelector;
import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Bard;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.MountainEvent;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MiscTimers {
    private final HashMap<HCFPlayer, BukkitTask> warmup_tasks = new HashMap<>();
    public final ArrayList<BukkitTask> tasks = new ArrayList<>();
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
        BukkitTask t =  new BukkitRunnable() {
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

                HashMap<LivingEntity, Long> savedPlayers = new HashMap<>(Main.savedPlayers);
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
                    if (clss == Classes.ROGUE) {
                        stats.TotalRogueClassTime += 1000L;
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
        tasks.add(t);
    }

    public void mainRefresher() {
        BukkitTask t = new BukkitRunnable() {
            @Override
            public void run() {
                if(MountainEvent.getInstance().isReset()) {
                    MountainEvent.getInstance().reset();
                }

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);

                    runNowExpires(player);
                    executeClassWarmup(player);
                    refreshScoreboard(player);

                    NameChanger.refresh(player);

                    if (hcfPlayer.getPlayerClass() != Classes.BARD) continue;
                    if (!(hcfPlayer.getBardEnergy() >= bard.maxBardEnergy)) {
                        hcfPlayer.addBardEnergy(0.1 * bard.bardEnergyMultiplier);
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
        tasks.add(t);
    }

    public void KOTHCountdown() {

      BukkitTask t = new BukkitRunnable() {
            @Override
            public void run() {
                if (Koth.GLOBAL_AREA != null && Koth.GLOBAL_PLAYER != null) {
                    Koth.GLOBAL_TIME--;
                    if (Koth.GLOBAL_TIME % 30 == 0)
                        for(Player _p : Bukkit.getOnlinePlayers())
                            _p.sendMessage(Messages.koth_capture_timer
                                    .language(_p)
                                    .setFaction(Koth.GLOBAL_AREA.getFaction())
                                    .setFormattedTime(Koth.GLOBAL_TIME)
                                    .queue()
                            );
                    else if (Koth.GLOBAL_TIME % 15 == 0) {
                        Koth.GLOBAL_PLAYER.sendMessage(Messages.koth_holding.language(Koth.GLOBAL_PLAYER).setFaction(Koth.GLOBAL_AREA.getFaction()).queue());
                    }
                    if (Koth.GLOBAL_TIME <= 0) {
                        Koth.reward(Koth.GLOBAL_PLAYER);
                        Koth.stopKoth();
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
        tasks.add(t);
    }

    public void createFakeWalls() {
        BukkitTask t = new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    SpawnShield.CalcWall(player);
                    if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) continue;
                    ArrayList<Location> it = new ArrayList<>(Main.playerBlockChanges.get(player.getUniqueId()));
                    ArrayList<Location> it2 = new ArrayList<>(Main.playerBlockChanges.get(player.getUniqueId()));
                    //ListIterator<Location> it = Main.playerBlockChanges.get(player.getUniqueId()).listIterator();
                    for (Location loc : it) {
                        if (loc.distance(player.getLocation()) > 12) {
                            player.sendBlockChange(loc, Material.AIR, (byte) 0);
                            it2.remove(loc);
                        }
                    }
                    Main.playerBlockChanges.put(player.getUniqueId(), it2);
                }

            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 1);
        tasks.add(t);
    }
    public void removeFakeWalls(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) return;
                ArrayList<Location> it = new ArrayList<>(Main.playerBlockChanges.get(player.getUniqueId()));
                try {
                    for (Location location : it) {
                        player.sendBlockChange(location, Material.AIR, (byte) 0);
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }

                Main.playerBlockChanges.put(player.getUniqueId(), new ArrayList<Location>());
            }
        }.runTaskAsynchronously(Main.getPlugin(Main.class));
    }

    public void autoSave() {
        BukkitTask t = new BukkitRunnable() {
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
        tasks.add(t);
    }

    private void executeClassWarmup(Player player) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);
        if (hcfPlayer.getPlayerClass() == ClassSelector.getPlayerWearingClass(player)) {
            if (ClassSelector.getPlayerWearingClass(player).equals(Classes.NONE) && hcfPlayer.isClassWarmup()) {
                hcfPlayer.setClassWarmup(false);
                Timers.CLASS_WARMUP.remove(hcfPlayer);
                if (warmup_tasks.containsKey(hcfPlayer))
                    warmup_tasks.get(hcfPlayer).cancel();
            }
            return;
        }

        boolean armorClass = ClassSelector.getPlayerWearingClass(player) == Classes.NONE;
        boolean hasClass = hcfPlayer.getPlayerClass() == Classes.NONE;

        if (hasClass && !armorClass && !hcfPlayer.isClassWarmup()) {
            //Add
            addClassToPlayer(player);
            hcfPlayer.setClassWarmup(true);
            Timers.CLASS_WARMUP.add(hcfPlayer);
        } else if (armorClass && !hasClass) {
            //Remove
            ClassSelector.addClassToPlayer(player);
            removeWarmup(hcfPlayer);
        }

        if (armorClass && hcfPlayer.isClassWarmup()) {
            removeWarmup(hcfPlayer);
        }
    }

    private void removeWarmup(HCFPlayer hcfPlayer) {
        hcfPlayer.setClassWarmup(false);
        Timers.CLASS_WARMUP.remove(hcfPlayer);
        if (warmup_tasks.containsKey(hcfPlayer))
            warmup_tasks.get(hcfPlayer).cancel();
    }

    private void refreshScoreboard(Player player) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);
        if(hcfPlayer.isInDuty()) AdminScoreboard.refresh(player);
        boolean hasTimer = Arrays.stream(Timers.values()).anyMatch(timer -> timer.has(player));
        if (
                hasTimer
                || hcfPlayer.getBardEnergy() < bard.maxBardEnergy
        ) {
            Scoreboards.refresh(player);
        }
    }

    private void runNowExpires(Player p) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (Timers.STUCK.nowExpire(hcfPlayer)) {
            Location loc = Claiming.ReturnSafeSpot(p.getLocation());
            if (loc != null) {
                p.teleport(loc);
                p.sendMessage(Messages.stuck_finished.queue());
            }
        }
        //Stuck Time
        if (Timers.HOME.nowExpire(hcfPlayer)) {
            Faction faction = Playertools.getPlayerFaction(p);
            if (faction != null) {
                if (faction.getHomeLocation() != null) {
                    p.teleport(faction.getHomeLocation());
                    p.sendMessage(Messages.successfully_home_teleport.queue());
                }
            }
        }
        //Stuck Time
        if (Timers.LOGOUT.nowExpire(hcfPlayer)) {
            if(Config.BungeeCord.asBoolean()) {
                BungeeChanneling.getInstance().sendToLobby(p);
                p.sendMessage(Messages.logout_kick_message.language(p).queue());
            } else {
                p.kickPlayer(Messages.logout_kick_message.language(p).queue());
            }
        }
        if(Timers.PVP_TIMER.nowExpire(hcfPlayer)) {
            removeFakeWalls(p);
        }
        if(Timers.COMBAT_TAG.nowExpire(hcfPlayer)) {
            removeFakeWalls(p);
        }
    }
}

