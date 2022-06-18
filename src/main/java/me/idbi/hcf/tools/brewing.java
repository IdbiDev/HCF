package me.idbi.hcf.tools;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class brewing {
    public static final ArrayList<BrewingStand> brewingStands = new ArrayList<>();
    public static final ArrayList<Furnace> furnaces = new ArrayList<>();

    public static void setAllBrewingStands() {
        long start = (System.currentTimeMillis());
        World w = Bukkit.getWorld(ConfigLibrary.World_name.getValue());
        for (Chunk c : w.getLoadedChunks()) {
            BlockState[] blocks = c.getTileEntities();
            for(BlockState b : blocks){
                if (b.getType() == Material.BREWING_STAND) {
                    BrewingStand stand = (BrewingStand) b;
                    brewingStands.add(stand);
                }
                if (b.getType() == Material.FURNACE || b.getType() == Material.BURNING_FURNACE) {
                    Furnace stand = (Furnace) b;
                    furnaces.add(stand);
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
                    if (stand.getCookTime() != 200) {
                        stand.setCookTime((short) (stand.getCookTime() + 20));
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 1);
    }
}
