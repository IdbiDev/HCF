package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Map;

public class SpawnShield {

    public static void placeShield(Player p) {
        Main.Faction spawn = Main.faction_cache.get(1);

        if(!spawn.claims.isEmpty()) {
            for(HCF_Claiming.Faction_Claim claims : spawn.claims) {

                HCF_Claiming.Point claimP1 = new HCF_Claiming.Point(claims.startX, claims.startZ);
                HCF_Claiming.Point claimP2 = new HCF_Claiming.Point(claims.endX, claims.endZ);

                HCF_Claiming.Point playerPoint = new HCF_Claiming.Point(p.getLocation().getBlockX(), p.getLocation().getBlockZ());

                if(playertools.CheckClaimPlusOne(claimP1, claimP2, 8, playerPoint, playerPoint)) { // közel van
                    //sendBlockChange(p, claimP1, claimP2);
                }
            }
        } else {
            // owww :3
        }
    }

    public static void sendBlockChange(Player p, HCF_Claiming.Point start, HCF_Claiming.Point end) {
        int maxX = Math.max(start.x, end.x);
        int minX = Math.min(start.x, end.x);

        int maxZ = Math.max(start.z, end.z);
        int minZ = Math.min(start.z, end.z);

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = 0; y < 256; y++) {
                    Location loc = new Location(p.getWorld(), x, y, start.z);
                    if (loc.getBlock().getType() == Material.AIR) {
                        p.sendBlockChange(loc, Material.STAINED_GLASS, (byte) 14);
                    }
                }
            }
        }
    }

    public static void CalculateWall(Player p){
        HCF_Claiming.Point player = new HCF_Claiming.Point(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim claim : thisFaction.getValue().claims) {

                // Négy sarokpont
                HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
                HCF_Claiming.Point top_left = new HCF_Claiming.Point(claim.endX, claim.startZ);
                HCF_Claiming.Point bottom_right = new HCF_Claiming.Point(claim.startX, claim.endZ);

                System.out.println("BOTTOM LEFT: X:" + bottom_left.x + "    Z:" + bottom_left.z);
                System.out.println("BOTTOM RIGHT: X:" + bottom_right.x + "    Z:" + bottom_right.z);
                System.out.println("top_left: X:" + top_left.x + "    Z:" + top_left.z);
                System.out.println("top_right: X:" + top_right.x + "    Z:" + top_right.z);

                int width = Math.abs(top_right.z - top_left.z);
                int height = Math.abs(bottom_right.x - top_right.x);

                //System.out.println("Width: " + width);
                //System.out.println("Height: " + height);

                //Legközelebbi pont a playerhez
                HCF_Claiming.Point record_point = new HCF_Claiming.Point(0, 0);
                int record = 9999;
                // Player: X: 50 Z: 50
                //              69              // 69+30 == 99
                System.out.println(bottom_left.x + " ADDED: " + ((int) (bottom_left.x + height)));
                for(int x = top_left.x; x <= top_left.x + height; x++) {
                    System.out.println("X:"+ x);
                    //          50                          50 + 20 == 70
                    for(int z = top_left.z; z <= top_left.z + width; z++) {
                        //                          19   +              0
                        int distance =  Math.abs(x-player.x) + Math.abs(z-player.z);
                        //    50    <  9999
                        if(distance < record) {
                            record = distance;

                            //p.sendBlockChange(new Location(p.getWorld(), x, 64, z), Material.DIAMOND_BLOCK, (byte) 0);
                            record_point.x = x;
                            record_point.z = z;

                            //System.out.println("record: "+record);
                        }
                    }
                }
                if(true){
                    // Oldal megtekintése
                    System.out.println("RECORD POINT X: " + record_point.x);
                    System.out.println("RECORD POINT Z: " + record_point.z);
                    if(top_right.x == record_point.x && bottom_right.x == record_point.x) {
                        System.out.println("Jobb oldal");
                        // top right --- bottom right wall
                        sendBlockChange(p,top_right,bottom_right);
                    }
                    if(top_left.z == record_point.z && top_right.z == record_point.z) {
                        // top left --- top right wall
                        System.out.println("Felső");
                        sendBlockChange(p,top_right,top_left);
                    }
                    if(top_left.x == record_point.x && bottom_left.x == record_point.x) {
                        // top left --- bottom left wall
                        System.out.println("Bal oldal");
                        sendBlockChange(p,top_left,bottom_left);
                    }
                    if(bottom_left.z == record_point.z && bottom_right.z == record_point.z) {
                        // bottom right --- bottom left wall
                        System.out.println("Alul");
                        sendBlockChange(p,bottom_left,bottom_right);
                    }
                }
                System.out.println("Done");
            }
        }
    }
}

/*
public class BorderFinder {
  public static Collection<BlockPos> getBorderPoints(Region region) {
    HashSet<BlockPos> result = new HashSet<>();

    for (BlockPos point : region.getPoints()) {
      getAlongX(point, region, result);
      getAlongZ(point, region, result);
    }
    getBottomOrTop(region.getMin(), region, result);
    getBottomOrTop(region.getMax(), region, result);
    return result;
  }

  private static void getBottomOrTop(BlockPos bottomOrTop, Region region, HashSet<BlockPos> result) {
    for (int x = bottomOrTop.getX(); region.contains(x, bottomOrTop.getY(), bottomOrTop.getZ()); x++) {
      for (int z = bottomOrTop.getZ(); region.contains(x, bottomOrTop.getY(), z); z++)
        result.add(new BlockPos(x, bottomOrTop.getY(), z, region.getWorld()));
    }
  }

  private static void getAlongX(BlockPos start, Region region, HashSet<BlockPos> result) {
    if (region.contains(start.getX() + 1, start.getY(), start.getZ())) {
      for (int x = start.getX(); region.contains(x, start.getY(), start.getZ()); x++)
        result.add(new BlockPos(x, start.getY(), start.getZ(), region.getWorld()));
    } else {
      for (int x = start.getX(); region.contains(x, start.getY(), start.getZ()); x--)
        result.add(new BlockPos(x, start.getY(), start.getZ(), region.getWorld()));
    }
  }

  private static void getAlongZ(BlockPos start, Region region, HashSet<BlockPos> result) {
    if (region.contains(start.getX(), start.getY(), start.getZ() + 1)) {
      for (int z = start.getZ(); region.contains(start.getX(), start.getY(), z); z++)
        result.add(new BlockPos(start.getX(), start.getY(), z, region.getWorld()));
    } else {
      for (int z = start.getZ(); region.contains(start.getX(), start.getY(), z); z--)
        result.add(new BlockPos(start.getX(), start.getY(), z, region.getWorld()));
    }
  }
}
 */
