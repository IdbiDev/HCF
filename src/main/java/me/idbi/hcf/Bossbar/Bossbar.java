package me.idbi.hcf.Bossbar;

import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Bossbar {

    public static HashMap<String, Bossbar> bars;

    private final Player p;

    private final String message;

    private EntityWither w;

    public Bossbar(Player p, String message) {
        this.p = p;
        this.message = message;
        update();
    }

    public void update() {
        Vector d = this.p.getLocation().getDirection();
        Location loc = this.p.getLocation().add(d.multiply(20));
        removeWither();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        this.w = new EntityWither(world);
        this.w.setLocation(loc.getX(), this.p.getLocation().getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
        this.w.setCustomName(this.message);
        this.w.setInvisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(this.w);
        (((CraftPlayer) this.p).getHandle()).playerConnection.sendPacket(packet);
    }

    private void removeWither() {
        if (this.w != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.w.getId());
            (((CraftPlayer) this.p).getHandle()).playerConnection.sendPacket(packet);
        }
    }

    public void end() {
        removeWither();
    }
}
