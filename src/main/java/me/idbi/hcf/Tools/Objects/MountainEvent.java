package me.idbi.hcf.Tools.Objects;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MountainEvent {

    /**
     * Reset time in seconds, like every 1h -> 3600
     */
    @Getter @Setter private int time;
    /**
     * Added time, unixTime >= Reset { reset(); }
     */
    @Getter @Setter private long reset;
    @Getter @Setter private Location loc1;
    @Getter @Setter private Location loc2;
    @Getter @Setter private Material material;
    @Getter private static MountainEvent instance;

    public MountainEvent() {
        this.time = Config.MountainEventReset.asInt();
        this.reset = 0;
        this.material = Config.MountainEventMaterial.asMaterial();
        if(this.material == null)
            this.material = Material.AIR;
        this.loc1 = Playertools.parseLoc2(Main.netherWorld, Config.MountainEventLocation1.asStr());
        this.loc2 = Playertools.parseLoc2(Main.netherWorld, Config.MountainEventLocation2.asStr());
        instance = this;
        reset();
    }

    public long getRemaining() {
        return this.reset - System.currentTimeMillis();
    }

    public static void create() {
        new MountainEvent();
    }

    public void reset() {
        fillArea();
        this.reset = this.time * 1000L + System.currentTimeMillis();
    }

    public boolean isReset() {
        long currentUnix = System.currentTimeMillis();
        if(this.reset < currentUnix) {
            return true;
        }
        return false;
    }

    public void broadcast() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Messages.mountain_respawned.language(player).queue());
        }
    }

    public boolean isMountainBlock(Location location) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        if(x >= minX && x <= maxX) {
            if (y >= minY && y <= maxY) {
                if (z >= minZ && z <= maxZ) {
                    return true;
                }
            }
        }


        return false;
    }

    public void fillArea() {
        broadcast();
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    loc1.getWorld().getBlockAt(x, y, z).setType(this.material);
                }
            }
        }
    }
}
