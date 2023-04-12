package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TowerTools {

    public static CompletableFuture<List<HCF_Claiming.Point>> getMapTowers(Player p) {
        CompletableFuture<List<HCF_Claiming.Point>> result = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                Location pLoc = p.getLocation();
                List<HCF_Claiming.Point> locs = new ArrayList<>();
                for (Faction f : Main.factionCache.values()) {
                    if(f.getClaims().isEmpty()) continue;
                    int minX = Integer.MAX_VALUE;
                    int minZ = Integer.MAX_VALUE;

                    int maxX = -Integer.MAX_VALUE;
                    int maxZ = -Integer.MAX_VALUE;
                    for (HCF_Claiming.Faction_Claim claim : f.getClaims()) {
                        if(pLoc.distanceSquared(new Location(
                                claim.getWorld(),
                                claim.getStartX(), pLoc.getBlockY(), claim.getStartZ())
                        ) > (128^2)) continue;
                        int xMin = Math.min(claim.getStartX(), claim.getEndX());
                        minX = Math.min(minX, xMin);

                        int zMin = Math.min(claim.getStartZ(), claim.getEndZ());
                        minZ = Math.min(minZ, zMin);

                        int xMax = Math.max(claim.getStartX(), claim.getEndX());
                        maxX = Math.max(maxX, xMax);

                        int zMax = Math.max(claim.getStartZ(), claim.getEndZ());
                        maxZ = Math.max(maxZ, zMax);

                    }


                    HCF_Claiming.Point fClaim = new HCF_Claiming.Point(minX, minZ);
                    locs.add(fClaim);

                    HCF_Claiming.Point fClaim2 = new HCF_Claiming.Point(minX, maxZ);
                    locs.add(fClaim2);

                    HCF_Claiming.Point fClaim3 = new HCF_Claiming.Point(maxX, minZ);
                    locs.add(fClaim3);

                    HCF_Claiming.Point fClaim4 = new HCF_Claiming.Point(maxX, maxZ);
                    locs.add(fClaim4);
                }
                result.complete(locs);
            }
        }.runTask(Main.getInstance());


        return result;
    }

    public static void createPillar(Player p, List<HCF_Claiming.Point> list) {
        Tasks.executeAsync(() -> {
            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
            for (HCF_Claiming.Point point : list) {
                hcfPlayer.addFactionViewMapLocation(point);
                Location loc = new Location(p.getWorld(), point.getX(), p.getLocation().getBlockY(), point.getZ());
                for (int i = 0; i <= 256; i++) {
                    loc.setY(i);
                    if(loc.getBlock().getType().equals(Material.AIR)) {
                        if(i % 2 == 0)
                            p.sendBlockChange(loc, Material.GLOWSTONE, (byte) 0);
                        else if(i % 5 == 0)
                            p.sendBlockChange(loc, Material.DIAMOND_BLOCK, (byte) 0);
                        else
                            p.sendBlockChange(loc, Material.GLASS, (byte) 0);
                    }
                }
            }
        });
    }
    public static void removePillar(Player p, List<HCF_Claiming.Point> list) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        Tasks.executeAsync(() -> {
            List<HCF_Claiming.Point> points = new ArrayList<>(list);
            for (HCF_Claiming.Point point : points) {
                Location loc = new Location(p.getWorld(), point.getX(), 0, point.getZ());
                for (int i = 0; i <= 256; i++) {
                    loc.setY(i);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        p.sendBlockChange(loc, Material.AIR, (byte) 0);
                    }
                }
                hcfPlayer.removeFactionViewMapLocation(point);
            }
        });
    }
}
