package me.idbi.hcf;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.CustomFiles.Configs.Config;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class HCFMap {
    @Getter @Setter private Location worldSpawn;
    @Getter private World world;
    @Getter private World.Environment type;
    @Getter private HashMap<EntityType, Boolean> entityLimiter;

    public HCFMap(World world, Location spawn) {
        this.world = world;
        this.worldSpawn = spawn;
        this.type = world.getEnvironment();
    }

    public void setWorldSpawn(Location loc) {
        String string = loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
        if(type == World.Environment.NORMAL)
            Config.SpawnLocation.set(string);
        if(type == World.Environment.THE_END)
            Config.EndSpawn.set(string);
        if(type == World.Environment.NETHER)
            Config.NetherSpawn.set(string);
        this.worldSpawn = loc;
    }

    public void put(EntityType type, boolean state) {
        entityLimiter.put(type, state);
    }
}
