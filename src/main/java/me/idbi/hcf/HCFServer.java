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

    @Getter private HashMap<World.Environment, HCFMap> maps;

    @Getter private static HCFServer server;

    public HCFServer() {
        server = this;
        maps = new HashMap<>();
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        World nether = Bukkit.getWorld(Config.NetherName.asStr());
        World end = Bukkit.getWorld(Config.EndName.asStr());

        HCFMap worldMap = new HCFMap(world, Playertools.parseLoc(world, Config.SpawnLocation.asStr()));
        HCFMap netherMap = new HCFMap(nether, Playertools.parseLoc(nether, Config.NetherSpawn.asStr()));
        HCFMap endMap = new HCFMap(end, Playertools.parseLoc(end, Config.EndSpawn.asStr()));

        maps.put(world.getEnvironment(), worldMap);
        maps.put(nether.getEnvironment(), netherMap);
        maps.put(end.getEnvironment(), endMap);

        setupEntities();
    }
    public void clearMaps(){
        for(HCFMap m : maps.values())
            m.clearLists();
        this.maps.clear();
    }
    public HCFMap getMap(World world) {
        return maps.get(world.getEnvironment());
    }

    public HCFMap getMap(World.Environment environment) {
        return maps.get(environment);
    }

    private void setupEntities() {
        for (String key : LimitsFile.get().getConfigurationSection("ENTITY_LIMITER").getKeys(false)) {
            for (String world : LimitsFile.get().getConfigurationSection("ENTITY_LIMITER." + key).getKeys(false)) {
                HCFMap map = getMap(World.Environment.NORMAL);
                if(world.equalsIgnoreCase("nether"))
                    map = getMap(World.Environment.NETHER);
                if(world.equalsIgnoreCase("the_end"))
                    map = getMap(World.Environment.THE_END);

                boolean limited = LimitsFile.get().getBoolean("ENTITY_LIMITER." + key + "." + world);
                EntityType type = null;
                try {
                    type = EntityType.valueOf(world);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                if(type == null) continue;
                map.put(type, limited);
            }
        }
    }

    public void reload() {
        maps.clear();
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        World nether = Bukkit.getWorld(Config.NetherName.asStr());
        World end = Bukkit.getWorld(Config.EndName.asStr());

        HCFMap worldMap = new HCFMap(world, Playertools.parseLoc(world, Config.SpawnLocation.asStr()));
        HCFMap netherMap = new HCFMap(nether, Playertools.parseLoc(nether, Config.NetherSpawn.asStr()));
        HCFMap endMap = new HCFMap(end, Playertools.parseLoc(end, Config.EndSpawn.asStr()));

        maps.put(world.getEnvironment(), worldMap);
        maps.put(nether.getEnvironment(), netherMap);
        maps.put(end.getEnvironment(), endMap);

        setupEntities();
    }
}
