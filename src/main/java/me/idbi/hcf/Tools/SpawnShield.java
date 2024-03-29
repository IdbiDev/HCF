package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Claim;
import me.idbi.hcf.Tools.Objects.ClaimAttributes;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.Point;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class SpawnShield {
    public static boolean pvpCooldown(Player p) {
        return Timers.PVP_TIMER.has(p);
    }

    public static void CalcWall(Player p) {
        Point player_point = new Point(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        //Faction player_faction = Playertools.getPlayerFaction(p);
        for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
            for (Claim claim : thisFaction.getValue().getClaims()) {
                if (!p.getWorld().getName().equalsIgnoreCase(claim.getWorld().getName())) continue;
                if(claim.getAttribute() != ClaimAttributes.NORMAL) continue;
                boolean placeWall = false;
                //true              //    true - > NINCS
                /*if ((pvpCooldown(p) && !claim.getAttribute().equals(ClaimAttributes.PROTECTED))) {
                    placeWall = true;
                } else if (Main.SOTWEnabled && ((claim.getAttribute().equals(ClaimAttributes.NORMAL) && claim.getFaction().getId() != (player_faction != null ? player_faction.getId() : 0)))) {
                    placeWall = true;
                } else if ((Timers.COMBAT_TAG.has(p) && claim.getAttribute().equals(ClaimAttributes.PROTECTED))) {
                    placeWall = true;
                }*/
                //Combat tagban fal
                if(Timers.COMBAT_TAG.has(p) && claim.getAttribute().equals(ClaimAttributes.PROTECTED))
                    placeWall = true;
                //PVP timer rak falat a normal claimekre
                if(Timers.PVP_TIMER.has(p) && claim.getAttribute().equals(ClaimAttributes.NORMAL))
                    placeWall = true;
                if (!placeWall)
                    continue;
                // Négy sarokpont
                Point bottom_left = new Point(claim.getStartX(), claim.getStartZ());
                Point top_right = new Point(claim.getEndX(), claim.getEndZ());

                Point top_left = new Point(top_right.getX(), bottom_left.getZ());
                Point bottom_right = new Point(bottom_left.getX(), top_right.getZ());

                int width = getDistanceBetweenPoints2D(bottom_left, top_left) + 1;
                int height = getDistanceBetweenPoints2D(bottom_left, bottom_right) + 1;

                int minX = Math.min(top_right.getX(), bottom_left.getX());
                int minZ = Math.min(top_right.getZ(), bottom_left.getZ());

                int record = 9999;
                Point record_point = new Point(0, 0);
                // -21+
                for (int x = minX; x < minX + width; x++) {
                    int distance = getDistanceBetweenPoints2D(new Point(x, minZ), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.setX(x);
                        record_point.setZ(minZ);
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ),Material.BEDROCK,(byte) 0);
                    distance = getDistanceBetweenPoints2D(new Point(x, minZ + height - 1), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.setX(x);
                        record_point.setZ(minZ + height - 1);
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ+height-1),Material.BEDROCK,(byte) 0);
                }
                for (int z = minZ; z < minZ + height; z++) {
                    int distance = getDistanceBetweenPoints2D(new Point(minX, z), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.setX(minX);
                        record_point.setZ(z);
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),minX,p.getLocation().getBlockY(),z),Material.BRICK,(byte) 0);
                    distance = getDistanceBetweenPoints2D(new Point(minX + width - 1, z), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.setX(minX + width - 1);
                        record_point.setZ(z);
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),minX+width-1,p.getLocation().getBlockY(),z),Material.BRICK,(byte) 0);
                }
                if (getDistanceBetweenPoints2D(record_point, player_point) > 10)
                    continue;
                String side;

//                if (bottom_left.getZ() == record_point.getZ() || top_left.getZ() == record_point.getZ()) {
//                    side = "x";
//                } else if (bottom_right.getZ() == record_point.getZ() || top_right.getZ() == record_point.getZ()) {
//                    side = "x";
//                } else {
//                    side = "z";
//                }
                side = bottom_left.getZ() == record_point.getZ() || top_right.getZ() == record_point.getZ() ? "x" : "z";
                //Main.sendCmdMessage("Side: %s Record point Z: %d Bottom right Z: %d Bottom left Z: %d".formatted(side,record_point.z,bottom_right.z,bottom_left.z));
                //System.out.printf("SIDE: " + side);
                placeWall(p, new Location(p.getWorld(), record_point.getX(), p.getLocation().getBlockY(), record_point.getZ()), side, claim);
                //forloop(p, new Location(p.getWorld(), record_point.x, p.getLocation().getBlockY(), record_point.z), side,claim);
            }
        }
    }


    /*
    CHAT GPT USEDDD
     */
    public static void calcWall(Player player) {
        Faction playerFaction = Playertools.getPlayerFaction(player);
        Point playerPoint = new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ());

        for (Faction faction : Main.factionCache.values()) {
            for (Claim claim : faction.getClaims()) {
                if (!player.getWorld().equals(claim.getWorld())) {
                    continue;
                }

                boolean shouldBuildWall = false;
                if (pvpCooldown(player) && claim.getAttribute() != ClaimAttributes.PROTECTED) {
                    shouldBuildWall = true;
                } else if (Main.SOTWEnabled && claim.getAttribute() == ClaimAttributes.NORMAL && claim.getFaction().getId() != (playerFaction != null ? playerFaction.getId() : 0)) {
                    shouldBuildWall = true;
                } else if (Timers.COMBAT_TAG.has(player) && claim.getAttribute() == ClaimAttributes.PROTECTED) {
                    shouldBuildWall = true;
                }

                if (!shouldBuildWall) {
                    continue;
                }

                Point bottomLeft = new Point(claim.getStartX(), claim.getStartZ());
                Point topRight = new Point(claim.getEndX(), claim.getEndZ());
                Point topLeft = new Point(topRight.getX(), bottomLeft.getZ());
                Point bottomRight = new Point(bottomLeft.getX(), topRight.getZ());

                int width = getDistanceBetweenPoints2D(bottomLeft, topLeft) + 1;
                int height = getDistanceBetweenPoints2D(bottomLeft, bottomRight) + 1;
                int minX = Math.min(topRight.getX(), bottomLeft.getX());
                int minZ = Math.min(topRight.getZ(), bottomLeft.getZ());
                int record = 9999;
                Point recordPoint = new Point(0, 0);

                for (int x = minX; x < minX + width; x++) {
                    int distance = getDistanceBetweenPoints2D(new Point(x, minZ), playerPoint);

                    if (distance < record) {
                        record = distance;
                        recordPoint.setX(x);
                        recordPoint.setZ(minZ);
                    }

                    distance = getDistanceBetweenPoints2D(new Point(x, minZ + height - 1), playerPoint);

                    if (distance < record) {
                        record = distance;
                        recordPoint.setX(x);
                        recordPoint.setZ(minZ + height - 1);
                    }
                }

                for (int z = minZ; z < minZ + height; z++) {
                    int distance = getDistanceBetweenPoints2D(new Point(minX, z), playerPoint);

                    if (distance < record) {
                        record = distance;
                        recordPoint.setX(minX);
                        recordPoint.setZ(z);
                    }

                    distance = getDistanceBetweenPoints2D(new Point(minX + width - 1, z), playerPoint);

                    if (distance < record) {
                        record = distance;
                        recordPoint.setX(minX + width - 1);
                        recordPoint.setZ(z);
                    }
                }

                if (getDistanceBetweenPoints2D(recordPoint, playerPoint) > 10) {
                    continue;
                }

                String side = bottomLeft.getZ() == recordPoint.getZ() || topRight.getZ() == recordPoint.getZ() ? "x" : "z";

                placeWall(player, new Location(player.getWorld(), recordPoint.getX(), player.getLocation().getBlockY(), recordPoint.getZ()), side, claim);
            }
        }
    }


    public static void placeWall(Player p, Location loc, String side, Claim claim) {

        if (side.equals("x")) {
            for (int x = -5; x <= 5; x++) {
                for (int y = -5; y <= 5; y++) {
                    Location temp = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ());
                    if (!temp.getBlock().getType().equals(Material.AIR)) continue;

                    Point point = new Point(temp.getBlockX(), temp.getBlockZ());
                    Point bottom_left = new Point(claim.getStartX(), claim.getStartZ());
                    Point top_right = new Point(claim.getEndX(), claim.getEndZ());
                    /*p.sendBlockChange(new Location(loc.getWorld(),bottom_left.x,loc.getY(),bottom_left.z), Material.GOLD_BLOCK, (byte) 14);
                    p.sendBlockChange(new Location(loc.getWorld(),top_right.x,loc.getY(),top_right.z), Material.GOLD_BLOCK, (byte) 14);
                    p.sendBlockChange(new Location(loc.getWorld(),point.x,loc.getY(),point.z), Material.WOOL, (byte) 14);*/
                    if (Claiming.doOverlap(bottom_left, top_right, point, point)) {
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

                    Point point = new Point(temp.getBlockX(), temp.getBlockZ());
                    Point bottom_left = new Point(claim.getStartX(), claim.getStartZ());
                    Point top_right = new Point(claim.getEndX(), claim.getEndZ());
                    //p.sendBlockChange(new Location(loc.getWorld(),point.x,loc.getY(),point.z), Material.DIAMOND_BLOCK, (byte) 14);
                    if (Claiming.doOverlap(bottom_left, top_right, point, point)) {
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

