package me.idbi.hcf.Tools;


import me.idbi.hcf.Bossbar.BossbarTools;
import me.idbi.hcf.Classes.ClassSelector;
import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Bard;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCF_Rules;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static me.idbi.hcf.Koth.Koth.GLOBAL_TIME;
import static me.idbi.hcf.Koth.Koth.stopKoth;

public class MiscTimers {
    Bard bard = new Bard();
    public void checkArmors() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    ClassSelector.addClassToPlayer(player);
                    HCFPlayer hcf = HCFPlayer.getPlayer(player);
                    if (hcf.getPlayerClass() == Classes.BARD) {
                        bard.useSimpleBardEffect(player);
                    }
                    //Shapes.Crossing(new Location(player.getWorld(),10,64,16),new Location(player.getWorld(),-21,64,-5),Effect.HEART,10);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 60);
    }

    /*public static void addBardEffects() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (!playertools.HasMetaData(player, "class")) continue;
                    if (playertools.getMetadata(player, "class").equalsIgnoreCase("bard")){
                        Bard.ApplyBardEffectOnActionBar(player);

                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 90);
    }*/
    /*public static void PvpTag() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<LivingEntity, Long> entry : Main.saved_players.entrySet()) {
                    LivingEntity key = entry.getKey();
                    long val = entry.getValue();
                    if (val <= 0) {
                        key.remove();
                        Main.saved_players.remove(key);
                        Main.saved_items.remove(key);
                    } else {
                        Main.saved_players.put(key, val - 1000);
                    }
                }

            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
    }*/
    public void DTRTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<Integer, Long> dtrRegen = Main.DTRREGEN;
                for (Map.Entry<Integer, Long> entry : dtrRegen.entrySet()) {
                    int key = entry.getKey();
                    long val = entry.getValue();
                    Faction f = Main.factionCache.get(key);
                    // 4000+10000 <= 4000
                    if (val <= System.currentTimeMillis()) {
                        Main.DTRREGEN.remove(key);
                        if (Main.debug)
                            Main.sendCmdMessage("DTR REGEN finished: ");
                        f.setDTR(f.getDTR_MAX());
                    } //DTR regen: 0 Minutes 10 Seconds
                    f.setDTR_TIMEOUT(val - System.currentTimeMillis());
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

    //LEc see
    //2Tick
    public void bardEnergy() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    boolean shouldRefresh = false;
                    //Stuck Time
                    if (Timers.STUCK.nowExpire(player)) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                        Location loc = HCF_Claiming.ReturnSafeSpot(player.getLocation());
                        if (loc != null) {
                            player.teleport(loc);
                            player.sendMessage(me.idbi.hcf.CustomFiles.Messages.Messages.stuck_finished.queue()); // Ez igy mi tell me ez a kukim same bro
                        }
                    }
                    //Stuck Time
                    if (Timers.HOME.nowExpire(player)) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                        Faction faction = Playertools.getPlayerFaction(player);
                        if(faction != null) {
                            if (faction.getHomeLocation() != null) {
                                player.teleport(faction.getHomeLocation());
                                player.sendMessage(me.idbi.hcf.CustomFiles.Messages.Messages.successfully_home_teleport.queue());
                            }
                        }
                    }
                    //Combat Time
                    if (Timers.COMBAT.has(player)) {
                        shouldRefresh = true;
                    }
                    //Scoreboards.refresh(player);
                    //Ep time
                    if (Timers.ENDER_PEARL.has(player)) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                    }
                    //Golden Apple
                    if (Timers.GOLDEN_APPLE.has(player)) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                    }
                    //OP Golden Apple
                    if (Timers.ENCHANTED_GOLDEN_APPLE.has(player)) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                    }
                    //PvPTimer
                    if (Timers.PVP_TIMER.has(player)) {
                        shouldRefresh = true;
                    }
//                    if (HCF_Timer.getPvPTimerCoolDownSpawn(player) != 0) {
//                        shouldRefresh = true;
//                    }
                    if (Timers.LOGOUT.has(player)) {
                        shouldRefresh = true;
                    }
                    if (Timers.SOTW.has(player) || Timers.EOTW.has(player)) {
                        shouldRefresh = true;
                    }
                    HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);
                    if (hcfPlayer.isInDuty()) {
                        AdminScoreboard.refresh(player);
                    }
                    if (!(hcfPlayer.getBardEnergy() >= bard.maxBardEnergy))
                        shouldRefresh = true;
                    if (!Timers.PVP_TIMER.has(player) && !Timers.COMBAT.has(player)) {
                        //DeleteWallsForPlayer(player);
                    }
                    if (shouldRefresh)
                        Scoreboards.refresh(player);
                        //Scoreboards.refresh(player);
                    //Class selector
                    //HCFPlayer hcf = HCFPlayer.getPlayer(player);
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


    public void potionLimiter() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        if (HCF_Rules.potionLimits.containsKey(effect.getType())) {
                            if (effect.getAmplifier() > HCF_Rules.potionLimits.get(effect.getType())) {
                                player.removePotionEffect(effect.getType());
                                player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), HCF_Rules.potionLimits.get(effect.getType()), false, false));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
    }

    public void cleanupFakeWalls() {
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
                            if(loc.distance(player.getLocation()) > 12) {
                                player.sendBlockChange(loc, Material.AIR, (byte) 0);
                                it.remove();
                            }
                        }
                    }catch (Exception ignored){}

//                    try {
//
//                        for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
//                            Location loc = it.next();
//
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                }

            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 1);
    }

    public void archerTagEffect() {
        /*
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (playertools.getPlayerFaction(player) != null) {
                        if (HCF_Claiming.startpositions.containsKey(playertools.getPlayerFaction(player).id) && HCF_Claiming.endpositions.containsKey(playertools.getPlayerFaction(player).id)) {
                            HCF_Claiming.Point bottom_left = HCF_Claiming.startpositions.get(playertools.getPlayerFaction(player).id);
                            HCF_Claiming.Point top_right = HCF_Claiming.endpositions.get(playertools.getPlayerFaction(player).id);
                            HCF_Claiming.Point top_left = new HCF_Claiming.Point(top_right.x, bottom_left.z);
                            HCF_Claiming.Point bottom_right = new HCF_Claiming.Point(bottom_left.x, top_right.z);
                            int width = getDistanceBetweenPoints2D(bottom_left, top_left)+1;

                            int height = getDistanceBetweenPoints2D(bottom_left, bottom_right)+1;

                            for (int x = bottom_left.x; x <= bottom_left.x+width; x++) {
                                player.playEffect(
                                        new Location(
                                            player.getWorld(),x,player.getLocation().getBlockY(),bottom_left.z
                                        ),Effect.HAPPY_VILLAGER,Effect.HAPPY_VILLAGER.getId()
                                );
                                //System.out.println(""+x+" "+(player.getLocation().getBlockY())+"  "+bottom_left.z);
                                player.playEffect(
                                        new Location(
                                                player.getWorld(),x,player.getLocation().getBlockY(),height-1
                                        ),Effect.HAPPY_VILLAGER,Effect.HAPPY_VILLAGER.getId()
                                );
                                //System.out.println(""+x+" "+(player.getLocation().getBlockY())+"  "+bottom_left.z+(height-1));
                                /*if(FindPoint_old(l1.x, l1.z, r1.x, r1.z, x, bottom_left.z)) {
                                    return true;
                                }
                                if(FindPoint_old(l2.x, l2.z, r2.x, r2.z, x, height-1)) {
                                    return true;
                                }

                                //Main.sendCmdMessage("");
                            }
                            for (int y = bottom_left.z; y <= bottom_left.z+height; y++) {
                                player.playEffect(
                                        new Location(
                                                player.getWorld(),bottom_left.x,player.getLocation().getBlockY(),y
                                        ),Effect.HAPPY_VILLAGER,Effect.HAPPY_VILLAGER.getId()
                                );
                                player.playEffect(
                                        new Location(
                                                player.getWorld(),width-1,player.getLocation().getBlockY(),y
                                        ),Effect.HAPPY_VILLAGER,Effect.HAPPY_VILLAGER.getId()
                                );
                                /*if(FindPoint_old(l2.x, l2.z, r2.x, r2.z, minX, y)) {
                                    return true;
                                }
                                if(FindPoint_old(l2.x, l2.z, r2.x, r2.z, width-1, y)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20L);*/
    }

    public void DeleteWallsForPlayer(Player player) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) return;
                ListIterator<Location> it = Main.playerBlockChanges.get(player.getUniqueId()).listIterator();
                while (it.hasNext()) {
                    Location loc = it.next();
                    if(loc.distance(player.getLocation()) > 12) {
                        player.sendBlockChange(loc, Material.AIR, (byte) 0);
                        it.remove();
                    }
                }
            }
        }.runTaskAsynchronously(Main.getPlugin(Main.class));
    }

//    public long getTimeOfEOTW() {
//        long current = System.currentTimeMillis();
//        //int timeInSeconds = Integer.parseInt(ConfigLibrary.EOTW_time.getValue()) * 60;
//        return Main.EOTWStarted - current;
//    }
//
//    public long getTimeOfSOTW() {
//        //int timeInSeconds = Integer.parseInt(ConfigLibrary.EOTW_time.getValue()) * 60;
//        return Main.SOTWStarted - System.currentTimeMillis();
//    }

    public void autoSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.saveAll();
                Runtime runtime = Runtime.getRuntime();
                long totalMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();

                //System.out.println("Memory: Used=" + (totalMemory - freeMemory) + " Total=" + totalMemory + " Free=" + freeMemory);

            }
        }.runTaskTimer(Main.getPlugin(Main.class), 6000, 6000);
    }
}

