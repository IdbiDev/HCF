package me.idbi.hcf.tools;


import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.classes.ClassSelector;
import me.idbi.hcf.classes.subClasses.Bard;
import me.idbi.hcf.koth.KOTH;
import me.idbi.hcf.particles.Shapes;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static me.idbi.hcf.koth.KOTH.GLOBAL_TIME;
import static me.idbi.hcf.koth.KOTH.stopKoth;
import static me.idbi.hcf.tools.playertools.getDistanceBetweenPoints2D;

public class Misc_Timers {

    public static void CheckArmors() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    ClassSelector.addClassToPlayer(player);
                    if (!playertools.HasMetaData(player, "class")) continue;
                    if (playertools.getMetadata(player, "class").equalsIgnoreCase("bard")){
                        Bard.useSimpleBardEffect(player);
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
    public static void DTR_Timer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer, Long> entry : Main.DTR_REGEN.entrySet()) {
                    int key = entry.getKey();
                    long val = entry.getValue();
                    Main.Faction f = Main.faction_cache.get(key);
                    // 4000+10000 <= 4000
                    if (val <= System.currentTimeMillis()) {
                        Main.DTR_REGEN.remove(key);
                        if (Main.debug)
                           Main.sendCmdMessage("DTR REGEN finished: ");
                        f.DTR = f.DTR_MAX;
                    } //DTR regen: 0 Minutes 10 Seconds
                    f.DTR_TIMEOUT = val-System.currentTimeMillis();
                }
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
                for(Map.Entry<Player, Main.PlayerStatistic> entry : Main.playerStatistics.entrySet()){
                    entry.getValue().TimePlayed += 1000L;
                    if (!playertools.HasMetaData(entry.getKey(), "class")) continue;
                    if(playertools.getMetadata(entry.getKey(), "class").equalsIgnoreCase("Assassin")){
                        entry.getValue().TotalAssassinClassTime += 1000L;
                    }
                    if(playertools.getMetadata(entry.getKey(), "class").equalsIgnoreCase("Archer")){
                        entry.getValue().TotalArcherClassTime += 1000L;
                    }
                    if(playertools.getMetadata(entry.getKey(), "class").equalsIgnoreCase("Bard")){
                        entry.getValue().TotalBardClassTime += 1000L;
                    }
                    if(playertools.getMetadata(entry.getKey(), "class").equalsIgnoreCase("Miner")){
                        entry.getValue().TotalMinerClassTime += 1000L;
                    }
                }

            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 20);
    }

    //LEc see
    //2Tick
    public static void Bard_Energy() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    boolean shouldRefresh = false;
                    //Stuck Time
                    if (HCF_Timer.getStuckTime(player) != 0) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                        Location loc = HCF_Claiming.ReturnSafeSpot(player.getLocation());
                        if(loc != null){
                            player.teleport(loc);
                            player.sendMessage(Messages.STUCK_FINISHED.queue());
                        }
                    }
                    //Combat Time
                    if (HCF_Timer.getCombatTime(player) != 0) {
                        shouldRefresh = true;
                    }
                        //Scoreboards.refresh(player);
                    //Ep time
                    if (HCF_Timer.getEpTime(player) != 0) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                    }
                    //Golden Apple
                    if (HCF_Timer.get_Golden_Apple_Time(player) != 0) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                    }
                    //OP Golden Apple
                    if (HCF_Timer.get_OP_Golden_Apple_Time(player) != 0) {
                        shouldRefresh = true;
                        //Scoreboards.refresh(player);
                    }
                    //PvPTimer
                    if (HCF_Timer.getPvPTimerCoolDownSpawn(player) != 0) {
                        shouldRefresh = true;
                    }
                    if(HCF_Timer.getPvPTimerCoolDownSpawn(player) != 0){
                        shouldRefresh = true;
                    }
                    if(Main.SOTW_ENABLED || Main.EOTW_ENABLED){
                        shouldRefresh = true;
                    }
                    if(HCF_Timer.getPvPTimerCoolDownSpawn(player) == 0 && HCF_Timer.getCombatTime(player) == 0) {
                        DeleteWallsForPlayer(player);
                    }
                    if(shouldRefresh)
                        Scoreboards.refresh(player);
                    //Class selector
                    if (!playertools.HasMetaData(player, "class")) continue;
                    if (!playertools.getMetadata(player, "class").equalsIgnoreCase("bard")) continue;
                    if (!(Double.parseDouble(playertools.getMetadata(player, "bardenergy")) >= 100D)) {
                        Scoreboards.refresh(player);
                        playertools.setMetadata(player, "bardenergy", Double.parseDouble(playertools.getMetadata(player, "bardenergy")) + 50D);
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
    }
    
    public static void KOTH_Countdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(KOTH.GLOBAL_AREA != null && KOTH.GLOBAL_PLAYER != null){
                    //TODO: Scoreboard format MIN:SS
                    KOTH.GLOBAL_TIME--;
                    if(KOTH.GLOBAL_TIME % 30 == 0)
                        Bukkit.broadcastMessage(Messages.KOTH_CAPTURE_TIMER.setFaction(KOTH.GLOBAL_AREA.faction.name).repTime_formatted(GLOBAL_TIME).queue());
                      //  Bukkit.broadcastMessage(Messages.KOTH_CAPTURE_TIMER.setFaction(KOTH.GLOBAL_AREA.faction.factioname).repTime_formatted(GLOBAL_TIME).queue());
                    if(KOTH.GLOBAL_TIME <= 0){

                        stopKoth();
                    }
                }

            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
    }


    public static void PotionLimiter() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        if (HCF_Rules.PotionLimits.containsKey(effect.getType())) {
                            if (effect.getAmplifier() > HCF_Rules.PotionLimits.get(effect.getType())) {
                                player.removePotionEffect(effect.getType());
                                player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), HCF_Rules.PotionLimits.get(effect.getType()), false, false));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
    }

    public static void CleanupFakeWalls() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    SpawnShield.CalcWall(player);
                    if(!Main.player_block_changes.containsKey(player)) continue;
                    List<Location> copy = Main.player_block_changes.get(player);
                    Location cur = null;
                    try{
                        for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {

                            Location loc = it.next();
                            cur = loc;
                            //System.out.println(player.getLocation().distance(loc));
                            if (player.getLocation().distance(loc) > 12) {
                                player.sendBlockChange(loc, Material.AIR, (byte) 0);
                                if (Main.player_block_changes.containsKey(player)) {
                                    List<Location> l = Main.player_block_changes.get(player);
                                    it.remove();
                                    //l.remove(loc);
                                    Main.player_block_changes.put(player, l);
                                }
                            }
                        }
                    }catch (Exception e){
                        try{
                            copy.remove(cur);
                        }catch (Exception ignored){}

                    }

                }

            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 1);
    }
    public static void ArcherTagEffect() {
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
                                }*/

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
                                }*/
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20L);
    }

    public static void DeleteWallsForPlayer(Player player){

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Main.player_block_changes.containsKey(player)) return;
                List<Location> copy = Main.player_block_changes.get(player);
                Location cur = null;
                try{
                    for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {

                        Location loc = it.next();
                        //cur = loc;
                        player.sendBlockChange(loc, Material.AIR, (byte) 0);
                        if (Main.player_block_changes.containsKey(player)) {
                            List<Location> l = Main.player_block_changes.get(player);
                            it.remove();
                            //l.remove(loc);
                            Main.player_block_changes.put(player, l);
                        }
                    }
                }catch (Exception e){
                    try{

                        copy.remove(null);
                    }catch (Exception ignored){}
                }
            }
        }.runTaskAsynchronously(Main.getPlugin(Main.class));
    }

    public static long getTimeOfEOTW() {
        long current = System.currentTimeMillis();
        //int timeInSeconds = Integer.parseInt(ConfigLibrary.EOTW_time.getValue()) * 60;
        return Main.EOTWStarted - current;
    }
    public static long getTimeOfSOTW() {
        //int timeInSeconds = Integer.parseInt(ConfigLibrary.EOTW_time.getValue()) * 60;
        return Main.SOTWSTARTED - System.currentTimeMillis();
    }
    public static void AutoSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.SaveAll();
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 6000, 6000);
    }
}

