package me.idbi.hcf.Tools;

import me.idbi.hcf.CustomFiles.Configs.Config;
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

import static me.idbi.hcf.Main.*;

public class SpeedModifiers {
    public static final ArrayList<BrewingStand> brewingStands = new ArrayList<>();
    public static final ArrayList<Furnace> furnaces = new ArrayList<>();

    public static void setAllBrewingStands() {
        World w = Bukkit.getWorld(Config.WorldName.asStr());
        if (w == null) {
            Main.sendCmdMessage("§cWorld not found! Make sure to select the world name in the config.yml file!\nShutting down..");
            Bukkit.shutdown();
            return;
        }
        for (Chunk c : w.getLoadedChunks()) {
            BlockState[] blocks = c.getTileEntities();
            for (BlockState b : blocks) {
                if (b.getType() == Material.BREWING_STAND && !brewingStands.contains((BrewingStand) b)) {
                    BrewingStand stand = (BrewingStand) b;
                    brewingStands.add(stand);
                }
                if ((b.getType() == Material.FURNACE || b.getType() == Material.BURNING_FURNACE) && !furnaces.contains((Furnace) b)) {
                    Furnace stand = (Furnace) b;
                    furnaces.add(stand);
                }
            }
        }
    }

    public static void asyncCacheBrewingStands() {
        new BukkitRunnable() {
            @Override
            public void run() {
                setAllBrewingStands();
            }
        }.runTaskAsynchronously(Main.getPlugin(Main.class));
    }

    public static void speedBoost() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(brewingSpeedMultiplier != 0){
                    for (BrewingStand stand : brewingStands) {
                        if (stand.getBrewingTime() - 20 >= 0)
                            stand.setBrewingTime((short) stand.getBrewingTime() - (10* brewingSpeedMultiplier));

                    }
                }
                if(cookSpeedMultiplier != 0){
                    for (Furnace stand : furnaces) {
                        if (stand.getCookTime() + 20 <= 200)
                            stand.setCookTime((short) (stand.getCookTime() + (10*cookSpeedMultiplier)));

                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 1);
    }
}
