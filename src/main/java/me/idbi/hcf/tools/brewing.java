package me.idbi.hcf.tools;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class brewing {
    private static final ArrayList<BrewingStand> brewingStands = new ArrayList<>();
    private static final ArrayList<Furnace> furnaces = new ArrayList<>();

    public static void setAllBrewingStands() {
        long start = (System.currentTimeMillis());
        World w = Bukkit.getWorld(ConfigLibrary.World_name.getValue());
        for (int x = -Main.world_border_radius; x <= Main.world_border_radius; x++) {
            for (int y = 3; y <= 253; y++) {
                for (int z = -Main.world_border_radius; z <= Main.world_border_radius; z++) {
                    if (w.isChunkLoaded(x, z)) {
                        Block block = w.getBlockAt(x, y, z);
                        if (block.getType() == Material.AIR) continue;
                        if (block.getType() == Material.BREWING_STAND) {
                            BrewingStand stand = (BrewingStand) block.getState();
                            brewingStands.add(stand);
                            System.out.println("Found brewing stand at " + x + "," + y + "," + z);
                        }
                        if (block.getType() == Material.FURNACE) {
                            Furnace stand = (Furnace) block.getState();
                            furnaces.add(stand);
                            System.out.println("Found furnace at " + x + "," + y + "," + z);
                        }
                    }
                }
            }
        }
        System.out.println("Finished check: " + (System.currentTimeMillis() - start));
    }

    public static void Async_Cache_BrewingStands() {
        new BukkitRunnable() {
            @Override
            public void run() {
                setAllBrewingStands();
            }
        }.runTaskAsynchronously(Main.getPlugin(Main.class));
    }

    public static void SpeedBoost() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (BrewingStand stand : brewingStands) {
                    if (stand.getBrewingTime() != 0) {
                        stand.setBrewingTime((short) stand.getBrewingTime() - 20);
                    }
                }
                for (Furnace stand : furnaces) {
                    if (stand.getCookTime() != 0) {
                        System.out.println("time:" + stand.getCookTime());
                        stand.setCookTime((short) (stand.getCookTime() - 20));
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 1);
    }
}
