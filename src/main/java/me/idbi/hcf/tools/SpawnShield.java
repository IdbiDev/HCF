package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

import static me.idbi.hcf.tools.HCF_Timer.checkCombatTimer;
import static me.idbi.hcf.tools.playertools.getDistanceBetweenPoints2D;

public class SpawnShield {
    public static boolean pvpCooldown(){
        return true;
    }
    public static void CalcWall() {

        for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()){
            for(HCF_Claiming.Faction_Claim claim : thisFaction.getValue().claims){
                for(Player p : Bukkit.getOnlinePlayers()){
                    //if(Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty"))) continue;
                    //Todo: SOTW / PVP COOLDOWN
                    //Main.sendCmdMessage("SOTW cooldown:%s\tClaim Attribute:%s".formatted(pvpCooldown(),claim.attribute));
                    if(pvpCooldown()){
                        if(claim.attribute.equalsIgnoreCase("normal"))
                            continue;
                    }
                    if(claim.attribute.equalsIgnoreCase("protected")){
                        if(checkCombatTimer(p))
                            continue;
                    }
                    if(!(pvpCooldown() && claim.attribute.equalsIgnoreCase("normal"))){
                        continue;
                    }
                    // Négy sarokpont
                    HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                    HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
                    HCF_Claiming.Point top_left = new HCF_Claiming.Point(top_right.x, bottom_left.z);
                    HCF_Claiming.Point bottom_right = new HCF_Claiming.Point(bottom_left.x, top_right.z);
                    HCF_Claiming.Point player_point = new HCF_Claiming.Point(p.getLocation().getBlockX(), p.getLocation().getBlockZ());

                    int width = getDistanceBetweenPoints2D(bottom_left, top_left)+1;

                    int height = getDistanceBetweenPoints2D(bottom_left, bottom_right)+1;

                    int minX = Math.min(top_right.x, bottom_left.x);
                    int minZ = Math.min(top_right.z, bottom_left.z);
                    int record = 9999;
                    HCF_Claiming.Point record_point = new HCF_Claiming.Point(0,0);
                    // -21+
                    for(int x = minX;x<=minX+width;x++){
                        int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ), player_point);
                        if (distance < record) {
                            record = distance;
                            record_point.x = x;
                            record_point.z = minZ;
                        }
                        //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ),Material.BEDROCK,(byte) 0);
                        distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(x, minZ+height-1), player_point);
                        if (distance < record) {
                            record = distance;
                            record_point.x = x;
                            record_point.z = minZ+height-1;
                        }
                        //p.sendBlockChange(new Location(p.getWorld(),x,p.getLocation().getBlockY(),minZ+height-1),Material.BEDROCK,(byte) 0);
                    }

                    for(int z = minZ;z<=minZ+height;z++){
                        int distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX, z), player_point);
                        if (distance < record) {
                            record = distance;
                            record_point.x = minX;
                            record_point.z = z;
                        }
                        //p.sendBlockChange(new Location(p.getWorld(),minX,p.getLocation().getBlockY(),z),Material.BRICK,(byte) 0);
                        distance = getDistanceBetweenPoints2D(new HCF_Claiming.Point(minX+width-1, z), player_point);
                        if (distance < record) {
                            record = distance;
                            record_point.x = minX+width-1;
                            record_point.z = z;
                        }
                        //p.sendBlockChange(new Location(p.getWorld(),minX+width-1,p.getLocation().getBlockY(),z),Material.BRICK,(byte) 0);
                    }
                    if(getDistanceBetweenPoints2D(record_point,player_point) > 10)
                        return;

                    String side;
                    if (bottom_left.z == record_point.z || top_left.z == record_point.z) {
                        side = "x";
                    }else if (bottom_right.z == record_point.z || top_right.z == record_point.z) {
                        side = "x";
                    } else {
                        side = "z";
                    }
                    //Main.sendCmdMessage("Side: %s Record point Z: %d Bottom right Z: %d Bottom left Z: %d".formatted(side,record_point.z,bottom_right.z,bottom_left.z));

                    //System.out.printf("SIDE: " + side);
                    placeWall(p,new Location(p.getWorld(),record_point.x,p.getLocation().getBlockY(),record_point.z),side,claim);
                    //forloop(p, new Location(p.getWorld(), record_point.x, p.getLocation().getBlockY(), record_point.z), side,claim);
                }
            }
        }
    }
    public static void placeWall(Player p,Location loc,String side,HCF_Claiming.Faction_Claim claim){
        if(side.equals("x")){
            for(int x= -5;x<=5;x++){
                for(int y=-5;y<=5;y++){
                    Location temp = new Location(loc.getWorld(),loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ());
                    if (!temp.getBlock().getType().equals(Material.AIR)) continue;

                    HCF_Claiming.Point point = new HCF_Claiming.Point(temp.getBlockX(),temp.getBlockZ());
                    HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                    HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
                    if(HCF_Claiming.FindPoint_old(bottom_left.x,bottom_left.z,top_right.x,top_right.z,point.x,point.z)){
                        p.sendBlockChange(temp, Material.STAINED_GLASS, (byte) 14);
                        playertools.placeBlockChange(p, temp);
                    }
                }
            }
        }else{
            for(int z= -5;z<=5;z++){
                for(int y=-5;y<=5;y++){
                    Location temp = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+y,loc.getBlockZ()+z);
                    if (!temp.getBlock().getType().equals(Material.AIR)) continue;
                    HCF_Claiming.Point point = new HCF_Claiming.Point(temp.getBlockX(),temp.getBlockZ());
                    HCF_Claiming.Point bottom_left = new HCF_Claiming.Point(claim.startX, claim.startZ);
                    HCF_Claiming.Point top_right = new HCF_Claiming.Point(claim.endX, claim.endZ);
                    if(HCF_Claiming.FindPoint_old(bottom_left.x,bottom_left.z,top_right.x,top_right.z,point.x,point.z)) {
                        p.sendBlockChange(temp, Material.STAINED_GLASS, (byte) 14);
                        playertools.placeBlockChange(p, temp);
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

