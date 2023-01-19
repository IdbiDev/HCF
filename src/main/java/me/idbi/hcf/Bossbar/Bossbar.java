package me.idbi.hcf.Bossbar;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Bossbar {

    public static HashMap<String, Bossbar> bars;

    private Player p;

    private String message;

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
        WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
        this.w = new EntityWither((World)world);
        this.w.setLocation(loc.getX(), this.p.getLocation().getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
        this.w.setCustomName(this.message);
        this.w.setInvisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving)this.w);
        (((CraftPlayer)this.p).getHandle()).playerConnection.sendPacket((Packet)packet);
    }

    private void removeWither() {
        if (this.w != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.w.getId() });
            (((CraftPlayer)this.p).getHandle()).playerConnection.sendPacket((Packet)packet);
        }
    }

    public void end() {
        removeWither();
    }
}
