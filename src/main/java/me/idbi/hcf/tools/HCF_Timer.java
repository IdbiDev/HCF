package me.idbi.hcf.tools;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class HCF_Timer {
    // Combatlog Timer
    public static final HashMap<Player, Long> timers = new HashMap<>();
    private static final HashMap<Player, Long> Archertimers = new HashMap<>();
    private static final HashMap<Player, Long> eptimers = new HashMap<>();

    public static final HashMap<Player, Long> stuckTimers = new HashMap<>();
    // Ms >> Duration
    private static final int combatTimerDuration = Integer.parseInt(ConfigLibrary.Combat_time.getValue()) * 1000;
    // Ms >> Duration
    private static final int ArcherTimerDuration = Integer.parseInt(ConfigLibrary.Archer_tag.getValue()) * 1000;

    private static final int EpTimerDuration = Integer.parseInt(ConfigLibrary.enderpearl_delay.getValue()) * 1000;

    private static final int StuckTimerDuration = Integer.parseInt(ConfigLibrary.STUCK_TIMER_DURATION.getValue()) * 1000;

    public static boolean addCombatTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!timers.containsKey(player)) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            timers.put(player, System.currentTimeMillis() + combatTimerDuration);
            return true;
        } else {
            timers.put(player, System.currentTimeMillis() + combatTimerDuration);
            return false;
        }
    }

    public static boolean addStuckTimer(Player player) {
        // Ha már van rajta CombatTimer, akkor ne addjuk hozzá
        if (!stuckTimers.containsKey(player)) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            stuckTimers.put(player, System.currentTimeMillis() + StuckTimerDuration);
            return true;
        } else {
            stuckTimers.put(player, System.currentTimeMillis() + StuckTimerDuration);
            return false;
        }
    }

    public static boolean checkCombatTimer(Player player) {
        // Ha van rajta CombatTimer
        if (timers.containsKey(player)) {
            if (System.currentTimeMillis() >= timers.get(player)) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                timers.remove(player);
                return false;
            } else {
                // Ellenkező esetben: Van rajta
                return true;
            }
        } else {
            return false;
        }
    }
    public static boolean checkStuckTimer(Player player) {
        // Ha van rajta CombatTimer
        if (stuckTimers.containsKey(player)) {
            if (System.currentTimeMillis() >= stuckTimers.get(player)) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                stuckTimers.remove(player);
                return false;
            } else {
                // Ellenkező esetben: Van rajta
                return true;
            }
        } else {
            return false;
        }
    }

    public static long getCombatTime(Player player) {
        // Ha van rajta CombatTimer
        if (timers.containsKey(player)) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= timers.get(player)) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                timers.remove(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return timers.get(player) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }
    public static long getStuckTime(Player player) {
        // Ha van rajta CombatTimer
        if (stuckTimers.containsKey(player)) {
            //5600                            5000+500
            if (System.currentTimeMillis() >= stuckTimers.get(player)) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                stuckTimers.remove(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                //     5000 + 500ms      -     5000ms
                return stuckTimers.get(player) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

    public static boolean addArcherTimer(Player player) {
        // Ha már van rajta Archertag, akkor ne addjuk hozzá
        if (!Archertimers.containsKey(player)) {
            // A mostani időhöz hozzá adjuk a Archertag idejét
            Archertimers.put(player, System.currentTimeMillis() + ArcherTimerDuration);
            return true;
        } else {
            return false;
        }
    }

    // true active; false nem
    public static boolean checkArcherTimer(Player player) {
        // Ha van rajta Archertag
        if (Archertimers.containsKey(player)) {
            // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta Archertag
            if (System.currentTimeMillis() >= Archertimers.get(player)) {
                Archertimers.remove(player);
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
        if (eptimers.containsKey(player)) {
            if (System.currentTimeMillis() >= eptimers.get(player)) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                eptimers.remove(player);
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
        if (!eptimers.containsKey(player)) {
            // A mostani időhöz hozzá adjuk a CombatTimer idejét
            eptimers.put(player, System.currentTimeMillis() + EpTimerDuration);
            return true;
        } else {
            return false;
        }
    }

    public static long getEpTime(Player player) {
        // Ha van rajta CombatTimer
        if (eptimers.containsKey(player)) {
            if (System.currentTimeMillis() >= eptimers.get(player)) {
                // Ha lejárt, akkor kivesszük a listából, majd vissza dobjuk hogy nincs már rajta CombatTimer
                eptimers.remove(player);
                return 0;
            } else {
                // Ellenkező esetben: Van rajta
                return eptimers.get(player) - System.currentTimeMillis();
            }
        } else {
            return 0;
        }
    }

}
