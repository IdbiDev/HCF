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
        return Timers.PVP_TIMER.has(p);
    }

    public static void CalcWall(Player p) {
        System.out.println(pvpCooldown(p) + " " + p.getName());
        HCF_Claiming.Point player_point = new HCF_Claiming.Point(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        Faction player_faction = Playertools.getPlayerFaction(p);
        for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
            for (HCF_Claiming.Faction_Claim claim : thisFaction.getValue().getClaims()) {
                if (!p.getWorld().getName().equalsIgnoreCase(claim.getWorld().getName())) continue;
                boolean kellfal = false;
                //true              //    true - > NINCS
                if ((pvpCooldown(p) && !claim.getAttribute().equals(HCF_Claiming.ClaimAttributes.PROTECTED))) {
                    kellfal = true;
                } else if (Main.SOTWEnabled && ((claim.getAttribute().equals(HCF_Claiming.ClaimAttributes.NORMAL) && claim.getFaction().getId() != (player_faction != null ? player_faction.getId() : 0)))) {
                    kellfal = true;
                } else if ((Timers.COMBAT_TAG.has(p) && claim.getAttribute().equals(HCF_Claiming.ClaimAttributes.PROTECTED))) {
                    kellfal = true;
                }
                if (!kellfal)
                    continue;
                System.out.println("Rakni le da fal");
                // Négy sarokpont
                HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.getStartX(), claim.getStartZ());
                HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.getEndX(), claim.getEndZ());

                HCF_Claiming.Point top_left = new HCF_Claiming.Point(top_right.getX(), bottom_left.getZ());
                HCF_Claiming.Point bottom_right = new HCF_Claiming.Point(bottom_left.getX(), top_right.getZ());

                int width = getDistanceBetweenPoints2D(bottom_left, top_left) + 1;
                int height = getDistanceBetweenPoints2D(bottom_left, bottom_right) + 1;
                int minX = Math.min(top_right.getX(), bottom_left.getX());
                int minZ = Math.min(top_right.getZ(), bottom_left.getZ());
                int record = 9999;
                HCF_Claiming.Point record_point = new HCF_Claiming.Point(0, 0);
                // -21+
                for (int x = minX; x < minX + width; x++) {
                    int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.setX(x);
                        record_point.setZ(minZ);
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ),Material.BEDROCK,(byte) 0);
                    distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ + height - 1), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.setX(x);
                        record_point.setZ(minZ + height - 1);
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ+height-1),Material.BEDROCK,(byte) 0);
                }
                for (int z = minZ; z < minZ + height; z++) {
                    int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX, z), player_point);
                    if (distance < record) {
                        record = distance;
                        record_point.setX(minX);
                        record_point.setZ(z);
                    }
                    //p.sendBlockChange(new Location(p.getWorld(),minX,p.getLocation().getBlockY(),z),Material.BRICK,(byte) 0);
                    distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX + width - 1, z), player_point);
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
        HCF_Claiming.Point playerPoint = new HCF_Claiming.Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ());

        for (Faction faction : Main.factionCache.values()) {
            for (HCF_Claiming.Faction_Claim claim : faction.getClaims()) {
                if (!player.getWorld().equals(claim.getWorld())) {
                    continue;
                }

                boolean shouldBuildWall = false;
                if (pvpCooldown(player) && claim.getAttribute() != HCF_Claiming.ClaimAttributes.PROTECTED) {
                    shouldBuildWall = true;
                } else if (Main.SOTWEnabled && claim.getAttribute() == HCF_Claiming.ClaimAttributes.NORMAL && claim.getFaction().getId() != (playerFaction != null ? playerFaction.getId() : 0)) {
                    shouldBuildWall = true;
                } else if (Timers.COMBAT_TAG.has(player) && claim.getAttribute() == HCF_Claiming.ClaimAttributes.PROTECTED) {
                    shouldBuildWall = true;
                }

                if (!shouldBuildWall) {
                    continue;
                }

                HCF_Claiming.Point bottomLeft = new HCF_Claiming.Point(claim.getStartX(), claim.getStartZ());
                HCF_Claiming.Point topRight = new HCF_Claiming.Point(claim.getEndX(), claim.getEndZ());
                HCF_Claiming.Point topLeft = new HCF_Claiming.Point(topRight.getX(), bottomLeft.getZ());
                HCF_Claiming.Point bottomRight = new HCF_Claiming.Point(bottomLeft.getX(), topRight.getZ());

                int width = getDistanceBetweenPoints2D(bottomLeft, topLeft) + 1;
                int height = getDistanceBetweenPoints2D(bottomLeft, bottomRight) + 1;
                int minX = Math.min(topRight.getX(), bottomLeft.getX());
                int minZ = Math.min(topRight.getZ(), bottomLeft.getZ());
                int record = 9999;
                HCF_Claiming.Point recordPoint = new HCF_Claiming.Point(0, 0);

                for (int x = minX; x < minX + width; x++) {
                    int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ), playerPoint);

                    if (distance < record) {
                        record = distance;
                        recordPoint.setX(x);
                        recordPoint.setZ(minZ);
                    }

                    distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ + height - 1), playerPoint);

                    if (distance < record) {
                        record = distance;
                        recordPoint.setX(x);
                        recordPoint.setZ(minZ + height - 1);
                    }
                }

                for (int z = minZ; z < minZ + height; z++) {
                    int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX, z), playerPoint);

                    if (distance < record) {
                        record = distance;
                        recordPoint.setX(minX);
                        recordPoint.setZ(z);
                    }

                    distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX + width - 1, z), playerPoint);

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


    public static void placeWall(Player p, Location loc, String side, HCF_Claiming.Faction_Claim claim) {

        if (side.equals("x")) {
            for (int x = -5; x <= 5; x++) {
                for (int y = -5; y <= 5; y++) {
                    Location temp = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ());
                    if (!temp.getBlock().getType().equals(Material.AIR)) continue;

                    HCF_Claiming.Point point = new HCF_Claiming.Point(temp.getBlockX(), temp.getBlockZ());
                    HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.getStartX(), claim.getStartZ());
                    HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.getEndX(), claim.getEndZ());
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
                    HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.getStartX(), claim.getStartZ());
                    HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.getEndX(), claim.getEndZ());
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

