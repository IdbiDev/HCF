package me.idbi.hcf.tools;

import me.idbi.hcf.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

import static me.idbi.hcf.tools.HCF_Timer.checkCombatTimer;
import static me.idbi.hcf.tools.playertools.getDistanceBetweenPoints2D;

public class SpawnShield {

    public static void CalcWall(Player p) {
        HCF_Claiming.Point player_point = new HCF_Claiming.Point(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim claim : thisFaction.getValue().claims) {
                if(!(checkCombatTimer(p) && claim.attribute.equalsIgnoreCase("Protected")))
                    continue;


                // Négy sarokpont
                HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
                HCF_Claiming.Point top_left = new HCF_Claiming.Point(claim.startX, claim.endZ);
                HCF_Claiming.Point bottom_right = new HCF_Claiming.Point(claim.endX, claim.startZ);

                //Legközelebbi pont a playerhez
                HCF_Claiming.Point record_point = new HCF_Claiming.Point(0, 0);
                int record = 9999;

                int minX = Math.min(top_right.x, bottom_left.x);
                int maxX = Math.max(top_right.x, bottom_left.x);

                int minZ = Math.min(top_right.z, bottom_left.z);
                int maxZ = Math.max(top_right.z, bottom_left.z);


                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, z), player_point);
                        if (distance <= record) {
                            record = distance;
                            record_point.x = x;
                            record_point.z = z;
                        }
                    }
                }
                if(getDistanceBetweenPoints2D(record_point,player_point) > 5){
                    return;
                }
                String side = "x";
                if (bottom_left.x == record_point.x) {
                    side = "x";
                } else {
                    side = "z";
                }

                if (bottom_right.x == record_point.x) {
                    side = "x";
                } else {
                    side = "z";
                }

                if (top_left.x == record_point.x) {
                    side = "x";
                } else {
                    side = "z";
                }

                if (top_right.x == record_point.x) {
                    side = "x";
                } else {
                    side = "z";
                }
                //System.out.printf("SIDE: " + side);
                forloop(p, new Location(p.getWorld(), record_point.x, p.getLocation().getBlockY(), record_point.z), side,claim);
            }
        }
    }

    public static void forloop(Player p, Location loc, String side,HCF_Claiming.Faction_Claim claim) {
        HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
        HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
        if(side.equals("x")) {
            for (int x = -5; x <= 5; x++) {
                for (int y = p.getLocation().getBlockY(); y < p.getLocation().getBlockY() + 5; y++) {
                    HCF_Claiming.Point temp = new HCF_Claiming.Point(loc.getBlockX(),loc.getBlockZ()+x);
                    if(HCF_Claiming.doOverlap(bottom_left,top_right,temp,temp)) {
                        Location location = new Location(loc.getWorld(), loc.getBlockX(), y, loc.getBlockZ() + x);

                        if (!location.getBlock().getType().equals(Material.AIR)) continue;

                        p.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
                        playertools.placeBlockChange(p, location);
                    }
                }
            }
        }else if(side.equals("z")) {
            for (int z = -5; z <= 5; z++) {
                for (int y = p.getLocation().getBlockY(); y < p.getLocation().getBlockY() + 5; y++) {
                    HCF_Claiming.Point temp = new HCF_Claiming.Point(loc.getBlockX()+z,loc.getBlockZ());
                    if(HCF_Claiming.doOverlap(bottom_left,top_right,temp,temp)){
                        Location location = new Location(loc.getWorld(), loc.getBlockX() + z, y, loc.getBlockZ());
                        if(!location.getBlock().getType().equals(Material.AIR)) continue;
                        p.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
                        playertools.placeBlockChange(p, location);
                    }
                }
            }
        }
    }
}

