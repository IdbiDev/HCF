package me.idbi.hcf.Tools.Objects;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class Claim {
    @Getter
    @Setter
    private World world;
    @Getter
    private int startX;
    @Getter
    private int endX;
    @Getter
    private int startZ;
    @Getter
    private int endZ;
    @Getter
    private Faction faction;
    //Attributes: Protected, KOTH, normal,Special
    @Getter
    @Setter
    private ClaimAttributes attribute;
    @Getter
    private int size;
    @Getter
    private Point start;
    @Getter
    private Point end;
    @Getter
    private int height;
    @Getter
    private int width;

    public Claim(int startX, int endX, int startZ, int endZ, int faction, ClaimAttributes attribute, String world) {
        this.startX = startX;
        this.endX = endX;
        this.startZ = startZ;
        this.endZ = endZ;
        this.start = new Point(startX, startZ);
        this.end = new Point(endX, endZ);
        this.faction = Main.factionCache.get(faction);
        this.attribute = attribute;
        this.world = Bukkit.getWorld(world);
        if (this.world == null) {
            this.world = Bukkit.getWorld(Config.WorldName.asStr());
        }
        this.size = getDistanceBetweenPoints2D(start, end);
        this.height = getDistanceBetweenPoints2D(start, new Point(startX, endZ));
        this.width = getDistanceBetweenPoints2D(start, new Point(endX, startZ));
    }

    public boolean in(Location loc) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        if (startX <= x && x <= endX) {
            return z >= startZ && z <= endZ;
        }

        return false;
    }
}
