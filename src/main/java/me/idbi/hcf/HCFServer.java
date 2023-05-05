package me.idbi.hcf;


import lombok.Getter;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.LimitsFile;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class HCFServer {

    private HashMap<World.Environment, HCFMap> maps;

    @Getter private static HCFServer server;

    public HCFServer() {
        maps = new HashMap<>();
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        World nether = Bukkit.getWorld(Config.NetherName.asStr());
        World end = Bukkit.getWorld(Config.EndName.asStr());

        HCFMap worldMap = new HCFMap(world, Playertools.parseLoc(world, Config.SpawnLocation.asStr()));
        HCFMap netherMap = new HCFMap(world, nether.getSpawnLocation());
        HCFMap endMap = new HCFMap(world, Playertools.parseLoc(world, Config.EndSpawn.asStr()));

        maps.put(world.getEnvironment(), worldMap);
        maps.put(nether.getEnvironment(), netherMap);
        maps.put(end.getEnvironment(), endMap);

        setupEntities();
        server = this;
    }

    public HCFMap getMap(World world) {
        return maps.get(world.getEnvironment());
    }

    public HCFMap getMap(World.Environment environment) {
        return maps.get(environment);
    }

    private void setupEntities() {
        for (String key : LimitsFile.get().getConfigurationSection("ENTITY_LIMITER").getKeys(false)) {
            for (String world : LimitsFile.get().getConfigurationSection(key).getKeys(false)) {
                HCFMap map = getMap(World.Environment.NORMAL);
                if(world.equalsIgnoreCase("nether"))
                    map = getMap(World.Environment.NETHER);
                if(world.equalsIgnoreCase("the_end"))
                    map = getMap(World.Environment.THE_END);

                boolean limited = LimitsFile.get().getBoolean(key);
                EntityType type = null;
                try {
                    type = EntityType.valueOf(key.toUpperCase());
                } catch (Exception e) {
                    continue;
                }
                if(type == null) continue;
                map.put(type, limited);
            }
        }
    }
}
