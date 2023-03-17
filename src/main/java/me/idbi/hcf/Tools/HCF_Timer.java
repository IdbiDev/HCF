package me.idbi.hcf.Tools;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Scoreboard.Scoreboards;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class HCF_Timer {
    // Combatlog Timer
    private static final HashMap<UUID, Long> timers = new HashMap<java.util.UUID, Long>();
    private static final HashMap<UUID, Long> stuckTimers = new HashMap<>();
    private static final HashMap<UUID, Long> golden_apple_Timers = new HashMap<>();
    private static final HashMap<UUID, Long> OP_Golden_Apple_Timers = new HashMap<>();
    private static final HashMap<UUID, Long> PVPTimers = new HashMap<>();
    private static final HashMap<UUID, Long> BardCooldown = new HashMap<>();
    private static final HashMap<UUID, Long> Archertimers = new HashMap<>();
    private static final HashMap<UUID, Long> eptimers = new HashMap<>();
    private static final HashMap<UUID, Long> logoutTimers = new HashMap<>();
    private static final HashMap<UUID, Long> homeTimers = new HashMap<>();
    private static final HashMap<UUID, Long> SOTWTimers = new HashMap<>();
    private static final HashMap<UUID, Long> EOTWTimers = new HashMap<>();
    // Ms >> Duration
    private static final int combatTimerDuration = Config.CombatTag.asInt() * 1000;
    // Ms >> Duration
    private static final int ArcherTimerDuration = Config.ArcherTag.asInt() * 1000;

    private static final int EpTimerDuration = Config.EnderPearl.asInt() * 1000;

    private static final int StuckTimerDuration = Config.StuckTimer.asInt() * 1000;
    private static final int Golden_Apple_Cooldown = Config.GolderApple.asInt() * 1000;
    private static final int OP_Golden_Apple_Cooldown = Config.EnchantedGoldenApple.asInt() * 1000;
    private static final int PvpTimerDuration = Config.PvPTimer.asInt() * 1000;
    private static final int BardCooldownDuration = Config.BardEnergy.asInt() * 1000;
    private static final int LogoutCooldownDuration = Config.Logout.asInt() * 1000;
    private static final int HomeCooldownDuration = Config.Logout.asInt() * 1000;
    private static final int EOTWDuration = Config.EOTWDuration.asInt() * 1000;

    public static boolean addCombatTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne adjuk hozzá
        if (!timers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            timers.put(player.getUniqueId(), System.currentTimeMillis() + combatTimerDuration);
            return true;
        } else {
            timers.put(player.getUniqueId(), System.currentTimeMillis() + combatTimerDuration);
            return false;
        }
    }
    public static boolean addLogoutTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne adjuk hozzá
        if (!logoutTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            logoutTimers.put(player.getUniqueId(), System.currentTimeMillis() + LogoutCooldownDuration);
            return true;
        } else {
            logoutTimers.put(player.getUniqueId(), System.currentTimeMillis() + LogoutCooldownDuration);
            return false;
        }
    }
    public static boolean addSOTWTimer(Player player,long time) {
        // Ha már van rajta CombatTimer, akkor ne adjuk hozzá
        if (!SOTWTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            SOTWTimers.put(player.getUniqueId(), System.currentTimeMillis() + time);
            return true;
        } else {
            SOTWTimers.put(player.getUniqueId(), System.currentTimeMillis() + time);
            return false;
        }
    }
    public static long getSOTWTime(Player player) {
        // Ha van rajta CombatTimer
        if (SOTWTimers.containsKey(player.getUniqueId())) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= SOTWTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                SOTWTimers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return SOTWTimers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }
    public static boolean addEOTWTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne adjuk hozzá
        if (!EOTWTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            EOTWTimers.put(player.getUniqueId(), System.currentTimeMillis() + EOTWDuration);
            return true;
        } else {
            EOTWTimers.put(player.getUniqueId(), System.currentTimeMillis() + EOTWDuration);
            return false;
        }
    }
    public static long getEOTWTimer(Player player) {
        // Ha van rajta CombatTimer
        if (EOTWTimers.containsKey(player.getUniqueId())) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= EOTWTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                EOTWTimers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return EOTWTimers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

    public static void removeEOTWTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        Timers.EOTW.remove(player);
        /*if (EOTWTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            EOTWTimers.remove(player.getUniqueId());
            return true;
        } else {
            return true;
        }*/
    }

    public static void removeSOTWTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        Timers.SOTW.remove(player);
        /*if (SOTWTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            SOTWTimers.remove(player.getUniqueId());
            return true;
        } else {
            return true;
        }*/
    }

    public static long getLogoutTime(Player player) {
        // Ha van rajta CombatTimer
        if (logoutTimers.containsKey(player.getUniqueId())) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= logoutTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                logoutTimers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                player.kickPlayer("LOGOUT GECI");
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return logoutTimers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }
    public static boolean addHomeTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne adjuk hozzá
        if (!homeTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            homeTimers.put(player.getUniqueId(), System.currentTimeMillis() + HomeCooldownDuration);
            return true;
        } else {
            homeTimers.put(player.getUniqueId(), System.currentTimeMillis() + HomeCooldownDuration);
            return false;
        }
    }
    public static long getHomeTime(Player player) {
        // Ha van rajta CombatTimer
        if (homeTimers.containsKey(player.getUniqueId())) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= homeTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                //homeTimers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return homeTimers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }
    public static boolean removeHomeTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (homeTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            homeTimers.remove(player.getUniqueId());
            return true;
        } else {
            return true;
        }

    }
    public static boolean removeStuckTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (stuckTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            stuckTimers.remove(player.getUniqueId());
            return true;
        } else {
            return true;
        }

    }
    public static boolean removeLogoutTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (logoutTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            logoutTimers.remove(player.getUniqueId());
            return true;
        } else {
            return true;
        }

    }

    public static boolean addStuckTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!stuckTimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            stuckTimers.put(player.getUniqueId(), System.currentTimeMillis() + StuckTimerDuration);
            return true;
        } else {
            stuckTimers.put(player.getUniqueId(), System.currentTimeMillis() + StuckTimerDuration);
            return false;
        }
    }

    public static boolean checkCombatTimer(Player player) {
        // Ha van rajta CombatTimer
        if (timers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= timers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                timers.remove(player.getUniqueId());
                //DeleteWallsForPlayer(player);
                return false;
            } else {
                // Ellenkező esetben: Van rajta
                return true;
            }
        } else {
            //DeleteWallsForPlayer(player);
            return false;
        }
    }

    public static boolean checkStuckTimer(Player player) {
        // Ha van rajta CombatTimer
        if (stuckTimers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= stuckTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                stuckTimers.remove(player.getUniqueId());
                return false;
            } else {
                // Ellenkező esetben: Van rajta
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean expiredNowStuck(Player player) {
        // Ha van rajta CombatTimer
        if (stuckTimers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= stuckTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                stuckTimers.remove(player.getUniqueId());
                return true;
            } else {
                // Ellenkező esetben: Van rajta
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean expiredNowHome(Player player) {
        // Ha van rajta CombatTimer
        if (homeTimers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= homeTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                homeTimers.remove(player.getUniqueId());
                return true;
            } else {
                // Ellenkező esetben: Van rajta
                return false;
            }
        } else {
            return false;
        }
    }

    public static long getCombatTime(Player player) {
        // Ha van rajta CombatTimer
        if (timers.containsKey(player.getUniqueId())) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= timers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                timers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return timers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

    public static long getStuckTime(Player player) {
        // Ha van rajta CombatTimer
        if (stuckTimers.containsKey(player.getUniqueId())) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= stuckTimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                //stuckTimers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return stuckTimers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

    public static boolean addArcherTimer(Player player) {
        // Ha már van rajta Archertag, akkor ne addjuk hozzá
        if (!Archertimers.containsKey(player)) {
            // A mostani időhöz hozzá adjuk a Archertag idejét
            Archertimers.put(player.getUniqueId(), System.currentTimeMillis() + ArcherTimerDuration);
            return true;
        } else {
            return false;
        }
    }

    // true active; false nem
    public static boolean checkArcherTimer(Player player) {
        // Ha van rajta Archertag
        if (Archertimers.containsKey(player.getUniqueId())) {
            // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta Archertag
            if (System.currentTimeMillis() >= Archertimers.get(player.getUniqueId())) {
                Archertimers.remove(player.getUniqueId());

                return false;
            } else {
                // Ellenkező esetben: Van rajta

                return true;
            }
        } else {

            return false;
        }
    }

    public static boolean checkEpTimer(Player player) {
        // Ha van rajta CombatTimer
        if (eptimers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= eptimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                eptimers.remove(player.getUniqueId());
                return false;
            } else {
                // Ellenkező esetben: Van rajta
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean addEpTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!eptimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            eptimers.put(player.getUniqueId(), System.currentTimeMillis() + EpTimerDuration);
            return true;
        } else {
            return false;
        }
    }

    public static boolean removeEpTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (eptimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            eptimers.remove(player.getUniqueId());
            return true;
        } else {
            return true;
        }

    }

    public static boolean addEpTimer(Player player, int cooldown) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!eptimers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            eptimers.put(player.getUniqueId(), System.currentTimeMillis() + cooldown * 1000L);
            return true;
        } else {
            return false;
        }
    }

    public static long getEpTime(Player player) {
        // Ha van rajta CombatTimer
        if (eptimers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= eptimers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                eptimers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                return eptimers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

    public static boolean add_Golden_Apple_Time(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!golden_apple_Timers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            golden_apple_Timers.put(player.getUniqueId(), System.currentTimeMillis() + Golden_Apple_Cooldown);
            return true;
        } else {
            return false;
        }
    }

    public static long get_Golden_Apple_Time(Player player) {
        // Ha van rajta CombatTimer
        if (golden_apple_Timers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= golden_apple_Timers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                golden_apple_Timers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                return golden_apple_Timers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

    public static boolean add_OP_Golden_Apple_Time(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!OP_Golden_Apple_Timers.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            OP_Golden_Apple_Timers.put(player.getUniqueId(), System.currentTimeMillis() + OP_Golden_Apple_Cooldown);
            return true;
        } else {
            return false;
        }
    }

    public static long get_OP_Golden_Apple_Time(Player player) {
        // Ha van rajta CombatTimer
        if (OP_Golden_Apple_Timers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= OP_Golden_Apple_Timers.get(player.getUniqueId())) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                OP_Golden_Apple_Timers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                return OP_Golden_Apple_Timers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }


    public static void removePVPTag(Player p) {
        timers.remove(p.getUniqueId());
    }

    public static boolean addPvPTimerCoolDownSpawn(Player player) {
        if (!PVPTimers.containsKey(player.getUniqueId())) {
            PVPTimers.put(player.getUniqueId(), System.currentTimeMillis() + PvpTimerDuration);
            return true;
        } else {
            return false;
        }
    }

    public static boolean addPvPTimerCoolDownSpawn(Player player, long duration) {
        if (!PVPTimers.containsKey(player.getUniqueId())) {
            PVPTimers.put(player.getUniqueId(), System.currentTimeMillis() + duration);
            return true;
        } else {
            return false;
        }
    }

    public static long getPvPTimerCoolDownSpawn(Player player) {
        if (PVPTimers.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= PVPTimers.get(player.getUniqueId())) {
                PVPTimers.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                return PVPTimers.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

    public static boolean addBardTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!BardCooldown.containsKey(player.getUniqueId())) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            BardCooldown.put(player.getUniqueId(), System.currentTimeMillis() + BardCooldownDuration);
            return true;
        } else {
            BardCooldown.put(player.getUniqueId(), System.currentTimeMillis() + BardCooldownDuration);
            return false;
        }
    }

    public static long getBardTimer(Player player) {
        if (BardCooldown.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() >= BardCooldown.get(player.getUniqueId())) {
                BardCooldown.remove(player.getUniqueId());
                Scoreboards.refresh(player);
                return 0;
            } else {
                return BardCooldown.get(player.getUniqueId()) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }


}
