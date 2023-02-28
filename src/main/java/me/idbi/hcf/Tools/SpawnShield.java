package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class SpawnShield {
    public static boolean pvpCooldown(Player p) {
        return HCF_Timer.getPvPTimerCoolDownSpawn(p) != 0;
    }

    public static void CalcWall(Player p) {
        HCF_Claiming.Point player_point = new HCF_Claiming.Point(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        Faction player_faction = Playertools.getPlayerFaction(p);
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim claim : thisFaction.getValue().claims) {
                if (!p.getWorld().getName().equalsIgnoreCase(claim.world.getName())) continue;
                boolean kellfal = false;
                //true              //    true - > NINCS
                if ((pvpCooldown(p) && !claim.attribute.equals(HCF_Claiming.ClaimAttributes.PROTECTED))) {
                    kellfal = true;
                } else if ((Main.SOTW_ENABLED && (claim.attribute.equals(HCF_Claiming.ClaimAttributes.NORMAL) && claim.faction.id != (player_faction != null ? player_faction.id : 0)))) {
                    kellfal = true;
                } else if ((HCF_Timer.getCombatTime(p) != 0 && claim.attribute.equals(HCF_Claiming.ClaimAttributes.PROTECTED))) {
                    kellfal = true;
                }
                if (!kellfal)
                    continue;

                // Négy sarokpont
                HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);

                HCF_Claiming.Point top_left = new HCF_Claiming.Point(top_right.x, bottom_left.z);
                HCF_Claiming.Point bottom_right = new HCF_Claiming.Point(bottom_left.x, top_right.z);

                int width = getDistanceBetweenPoints2D(bottom_left, top_left) + 1;
                int height = getDistanceBetweenPoints2D(bottom_left, bottom_right) + 1;
                int minX = Math.min(top_right.x, bottom_left.x);
                int minZ = Math.min(top_right.z, bottom_left.z);
                int record = 9999;
                HCF_Claiming.Point record_point = new HCF_Claiming.Point(0, 0);
                // -21+
                for (int x = minX; x < minX + width; x++) {
                    int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.x = x;
                        record_point.z = minZ;
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ),Material.BEDROCK,(byte) 0);
                    distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ + height - 1), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.x = x;
                        record_point.z = minZ + height - 1;
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ+height-1),Material.BEDROCK,(byte) 0);
                }
                for (int z = minZ; z < minZ + height; z++) {
                    int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX, z), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.x = minX;
                        record_point.z = z;
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),minX,p.getLocation().getBlockY(),z),Material.BRICK,(byte) 0);
                    distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX + width - 1, z), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.x = minX + width - 1;
                        record_point.z = z;
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),minX+width-1,p.getLocation().getBlockY(),z),Material.BRICK,(byte) 0);
                }
                if (getDistanceBetweenPoints2D(record_point, player_point) > 10)
                    continue;
                String side;

                if (bottom_left.z == record_point.z || top_left.z == record_point.z) {
                    side = "x";
                } else if (bottom_right.z == record_point.z || top_right.z == record_point.z) {
                    side = "x";
                } else {
                    side = "z";
                }
                //Main.sendCmdMessage("Side: %s Record point Z: %d Bottom right Z: %d Bottom left Z: %d".formatted(side,record_point.z,bottom_right.z,bottom_left.z));
                //System.out.printf("SIDE: " + side);
                placeWall(p, new Location(p.getWorld(), record_point.x, p.getLocation().getBlockY(), record_point.z), side, claim);
                //forloop(p, new Location(p.getWorld(), record_point.x, p.getLocation().getBlockY(), record_point.z), side,claim);
            }
        }
    }


    public static void placeWall(Player p, Location loc, String side, HCF_Claiming.Faction_Claim claim) {

        if (side.equals("x")) {
            for (int x = -5; x <= 5; x++) {
                for (int y = -5; y <= 5; y++) {
                    Location temp = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ());
                    if (!temp.getBlock().getType().equals(Material.AIR)) continue;

                    HCF_Claiming.Point point = new HCF_Claiming.Point(temp.getBlockX(), temp.getBlockZ());
                    HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                    HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
                    /*p.sendBlockChange(new Location(loc.getWorld(),bottom_left.x,loc.getY(),bottom_left.z), Material.GOLD_BLOCK, (byte) 14);
                    p.sendBlockChange(new Location(loc.getWorld(),top_right.x,loc.getY(),top_right.z), Material.GOLD_BLOCK, (byte) 14);
                    p.sendBlockChange(new Location(loc.getWorld(),point.x,loc.getY(),point.z), Material.WOOL, (byte) 14);*/
                    if (HCF_Claiming.doOverlap(bottom_left, top_right, point, point)) {
                        p.sendBlockChange(temp, Material.STAINED_GLASS, (byte) 14);
                        Playertools.placeBlockChange(p, temp);
                    }
                }
            }
        } else {
            for (int z = -5; z <= 5; z++) {
                for (int y = -5; y <= 5; y++) {
                    Location temp = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + y, loc.getBlockZ() + z);
                    if (!temp.getBlock().getType().equals(Material.AIR)) continue;

                    HCF_Claiming.Point point = new HCF_Claiming.Point(temp.getBlockX(), temp.getBlockZ());
                    HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                    HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
                    //p.sendBlockChange(new Location(loc.getWorld(),point.x,loc.getY(),point.z), Material.DIAMOND_BLOCK, (byte) 14);
                    if (HCF_Claiming.doOverlap(bottom_left, top_right, point, point)) {
                        p.sendBlockChange(temp, Material.STAINED_GLASS, (byte) 14);
                        Playertools.placeBlockChange(p, temp);
                    }
                }
            }
        }
    }
    /*public static void forloop(Player p, Location loc, String side,HCF_Claiming.Faction_Claim claim) {
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
    }*/
}

