package me.idbi.hcf.tools;

import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.classes.Bard;
import me.idbi.hcf.classes.ClassSelector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

import static me.idbi.hcf.tools.HCF_Timer.checkCombatTimer;

public class Misc_Timers {

    public static void CheckArmors() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    ClassSelector.addClassToPlayer(player);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 60);
    }

    public static void addBardEffects() {
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
    }

    public static void DTR_Timer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer, Long> entry : Main.DTR_REGEN.entrySet()) {
                    int key = entry.getKey();
                    long val = entry.getValue();
                    // 4000 <= 5000
                    if (val <= System.currentTimeMillis()) {
                        Main.DTR_REGEN.remove(key);
                        if (Main.debug)
                            System.out.println("DTR Regen >> Removed task cuz reached max DTR Faction: " + Main.factionToname.get(key));
                        Main.faction_cache.get(key).DTR = Double.parseDouble(playertools.CalculateDTR(Main.faction_cache.get(key)));
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 20);
    }

    public static void Bard_Energy() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (!playertools.HasMetaData(player, "class")) continue;
                    if (!playertools.getMetadata(player, "class").equalsIgnoreCase("bard")) continue;
                    if (!(Double.parseDouble(playertools.getMetadata(player, "bardenergy")) >= 100D)) {
                        Scoreboards.refresh(player);
                        playertools.setMetadata(player, "bardenergy", Double.parseDouble(playertools.getMetadata(player, "bardenergy")) + 0.1D);
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
    }


    public static void AutoSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.SaveAll();
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 6000, 6000);
    }

    public static void PvpTag() {
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
    }

    public static void pvpTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (!checkCombatTimer(player)) continue;
                    Scoreboards.refresh(player);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
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
    public static void StuckTimers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (HCF_Timer.checkStuckTimer(player)) {
                        Scoreboards.refresh(player);
                        Location loc = HCF_Claiming.ReturnSafeSpot(player.getLocation());
                        if(loc != null){
                            player.teleport(loc);
                            player.sendMessage(Messages.STUCK_FINISHED.queue());
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
    }
    public static void PearlTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (HCF_Timer.checkEpTimer(player)) {
                        Scoreboards.refresh(player);
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
    }


}

