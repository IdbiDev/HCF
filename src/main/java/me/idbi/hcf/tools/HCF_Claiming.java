package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.Objects.Faction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.idbi.hcf.tools.playertools.getDistanceBetweenPoints2D;

public class HCF_Claiming {
    public static final HashMap<Integer, Point> startpositions = new HashMap<>();
    public static final HashMap<Integer, Point> endpositions = new HashMap<>();

    public static final HashMap<Player, ArrayList<Location>> ConnectPosition = new HashMap<>();

    public static void setStartPosition(int faction, int x, int z) {
        if (!startpositions.containsKey(faction)) {
            Point p = new Point(x,z);
            startpositions.put(faction, p);
        } else {
            startpositions.remove(faction);
            Point p = new Point(x,z);
            startpositions.put(faction, p);
        }
    }

    public static void setEndPosition(int faction, int x, int z) {
        try{
            if (!endpositions.containsKey(faction)) {
                Point p = new Point(x,z);
                endpositions.put(faction, p);
            } else {
                endpositions.remove(faction);
                Point p = new Point(x,z);
                endpositions.put(faction, p);
            }
        }catch (Exception ignored){}

    }

    public static void removeClaiming(int faction) {
        startpositions.remove(faction);
        endpositions.remove(faction);
    }

    public static boolean FinishClaiming(int faction,Player p,ClaimAttributes attribute) {
        try {
            if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
                Point faction_start = startpositions.get(faction);
                Point faction_end = endpositions.get(faction);
                //for (Map.Entry<Integer, Faction_Claim> entry : Main.faction_cache.get(faction).claims.entrySet()) {
                for (Faction f : Main.faction_cache.values()) {

                    //if (f.claims.isEmpty()) continue;

                    for (HCF_Claiming.Faction_Claim val : f.claims) {

                        Point start_this = new Point(val.startX, val.startZ);
                        Point end_this = new Point(val.endX, val.endZ);

                        /*Point start_other = new Point(faction_start.x, faction_start.z);
                        Point end_other = new Point(faction_end.x, faction_end.z);*/


                        if (doOverlap3(faction_start, faction_end, start_this, end_this)) {
                            //if (doOverlap(new Point(val.startX, val.endX), new Point(val.startZ, val.endZ), new Point(start[0], end[0]), new Point(start[1], end[1]))) {
                            p.sendMessage(Messages.FACTION_CLAIM_OVERLAP.queue());
                            return false;
                        }
                        if (calcBlocks(p) < 4) {
                            p.sendMessage(Messages.FACTION_CLAIM_TOO_SMALL.queue());
                            return false;
                        }
                        if(playertools.CheckClaimPlusOne(faction_start, faction_end,1, start_this, end_this)) {
                            p.sendMessage(Messages.FACTION_CLAIM_OVERLAP_PLUS_ONE.queue());
                            return false;
                        }

                    }
                }
                if (playertools.getPlayerBalance(p) - calcMoneyOfArea(p) >= 0) {
                    playertools.setMetadata(p, "money", playertools.getPlayerBalance(p) - calcMoneyOfArea(p));
                } else {
                    if(attribute.equals(ClaimAttributes.NORMAL)){
                        p.sendMessage(Messages.NOT_ENOUGH_MONEY.queue());
                        return false;
                    }
                }

                int claimid = SQL_Connection.dbExecute(Main.getConnection("commands.Claim"), "INSERT INTO claims SET factionid='?',startX='?',startZ='?',endX='?',endZ='?',type='?'",
                        String.valueOf(faction), faction_start.x + "", faction_start.z + "", faction_end.x + "", faction_end.z + "",attribute.name().toLowerCase());
                //HandleSpawn
                    Faction f = Main.faction_cache.get(faction);
                    endpositions.remove(faction);
                    startpositions.remove(faction);
                    HCF_Claiming.Faction_Claim claim;
                    claim = new HCF_Claiming.Faction_Claim(faction_start.x, faction_end.x, faction_start.z, faction_end.z, claimid,attribute);
                    f.addClaim(claim);
                /*if(faction == 1){
                    claim = new HCF_Claiming.Faction_Claim(faction_start.x, faction_end.x, faction_start.z, faction_end.z, claimid,"protected");
                }else{
                    claim = new HCF_Claiming.Faction_Claim(faction_start.x, faction_end.x, faction_start.z, faction_end.z, claimid,"normal");
                }*/


                return true;
            }
            return false;
        } catch (Exception e) {
            p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
        }
        return false;
    }
    public static boolean ForceFinishClaim(int faction,Player p,ClaimAttributes attribute) {
        try {
            if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
                Point faction_start = startpositions.get(faction);
                Point faction_end = endpositions.get(faction);

                int claimid = SQL_Connection.dbExecute(
                        Main.getConnection("commands.Claim"),
                        "INSERT INTO claims SET factionid='?',startX='?',startZ='?',endX='?',endZ='?', type='?'",
                        String.valueOf(faction),
                        faction_start.x + "",
                        faction_start.z + "",
                        faction_end.x + "",
                        faction_end.z + "",
                        attribute.name().toLowerCase()
                );
                //HandleSpawn
                Faction f = Main.faction_cache.get(faction);
                endpositions.remove(faction);
                startpositions.remove(faction);
                HCF_Claiming.Faction_Claim claim;
                claim = new HCF_Claiming.Faction_Claim(faction_start.x, faction_end.x, faction_start.z, faction_end.z, claimid,attribute);
                f.addClaim(claim);

                return true;
            }
            return false;
        } catch (Exception e) {
            p.sendMessage(Messages.ERROR_WHILE_EXECUTING.queue());
        }
        return false;
    }

    //                              1. kezdő  1. end   2. kezdő   2. end
    public static boolean doOverlap(Point l1, Point r1, Point l2, Point r2) {
        // If one rectangle is on left side of other
        int firstRectangle_Starting_X = l1.x;
        int firstRectangle_Starting_Z = l1.z;

        int firstRectangle_Ending_X = r1.x;
        int firstRectangle_Ending_Z = r1.z;

        int maxX = Math.max(firstRectangle_Starting_X, firstRectangle_Ending_X);
        int minX = Math.min(firstRectangle_Starting_X, firstRectangle_Ending_X);

        int maxZ = Math.max(firstRectangle_Starting_Z, firstRectangle_Ending_Z);
        int minZ = Math.min(firstRectangle_Starting_Z, firstRectangle_Ending_Z);

        for(int x = minX; x <= maxX; x++) {
            for(int z = minZ; z <= maxZ; z++) {
                if(FindPoint_old(l2.x, l2.z, r2.x, r2.z, x, z)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean doOverlap3(Point l1, Point r1, Point l2, Point r2) {
        // If one rectangle is on left side of other
        int firstRectangle_Starting_X = l1.x;
        int firstRectangle_Starting_Z = l1.z;

        int firstRectangle_Ending_X = r1.x;
        int firstRectangle_Ending_Z = r1.z;


        int minX = Math.min(firstRectangle_Starting_X, firstRectangle_Ending_X);
        int minZ = Math.min(firstRectangle_Starting_Z, firstRectangle_Ending_Z);
        int maxX = Math.max(firstRectangle_Starting_X, firstRectangle_Ending_X);
        int maxZ = Math.max(firstRectangle_Starting_Z, firstRectangle_Ending_Z);

        Point bottom_left = new Point(minX,minZ);
        Point top_right = new Point(maxX, maxZ);
        Point top_left = new Point(top_right.x, bottom_left.z);
        Point bottom_right = new Point(bottom_left.x, top_right.z);

        int width = getDistanceBetweenPoints2D(bottom_left, top_left)+1;

        int height = getDistanceBetweenPoints2D(bottom_left, bottom_right)+1;

        for (int x = minX; x <= minX+width; x++) {
            if(FindPoint_old(l1.x, l1.z, r1.x, r1.z, x, minZ)) {
                return true;
            }
            if(FindPoint_old(l2.x, l2.z, r2.x, r2.z, x, height-1)) {
                return true;
            }
            //Main.sendCmdMessage("");
        }
        for (int y = minZ; y <= minZ+height; y++) {
            if(FindPoint_old(l2.x, l2.z, r2.x, r2.z, minX, y)) {
                return true;
            }
            if(FindPoint_old(l2.x, l2.z, r2.x, r2.z, width-1, y)) {
                return true;
            }
        }
        return false;
    }


    public static boolean megvanakellotavolsag(Point ownClaim1, Point ownClaim2, Point otherClaim1, Point otherClaim2) {
        if ((ownClaim1.x - otherClaim1.x) > 1
                || (ownClaim1.x - otherClaim1.x) < -1) {
            if ((ownClaim1.x - otherClaim2.x) > 1
                    || (ownClaim1.x - otherClaim2.x) < -1) {
                if ((ownClaim2.x - otherClaim1.x) > 1
                        || (ownClaim2.x - otherClaim1.x) < -1) {
                    if ((ownClaim2.x - otherClaim2.x) > 1
                            || (ownClaim2.x - otherClaim2.x) < -1) {
                        return true;
                    }
                }
            }
        }

        ////////////////////////////////////////////

        if ((ownClaim1.z - otherClaim1.z) > 1
                || (ownClaim1.z - otherClaim1.z) < -1) {
            if ((ownClaim1.z - otherClaim2.z) > 1
                    || (ownClaim1.z - otherClaim2.z) < -1) {
                if ((ownClaim2.z - otherClaim1.z) > 1
                        || (ownClaim2.z - otherClaim1.z) < -1) {
                    return (ownClaim2.z - otherClaim2.z) > 1
                            || (ownClaim2.z - otherClaim2.z) < -1;
                }
            }
        }


        return false;
    }


    public static boolean validClaim(int startX1, int startX2, int endX1, int endX2) {
        if (startX2 > startX1 && endX2 > startX1) {
            //False
            return startX2 >= endX1 && endX2 >= endX1;
        } else if (startX1 > startX2 && endX2 < startX1) {
            //False
            return startX2 <= endX1 && endX2 <= endX1;
        } else {
            //False
            return false;
        }
    }

    // true -> igen ez enemy claim action
    public static boolean checkEnemyClaimAction(int x, int z, Faction faction) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                if (FindPoint_old(val.startX, val.startZ, val.endX, val.endZ, x, z)) {
                    if(thisFaction.getValue().DTR <= 0) return false;
                    if (thisFaction.getKey() <= -1) {
                        return true;
                    }
                    if(faction == null){
                        return false;
                    }
                    return faction.id != thisFaction.getValue().id;
                }
            }
        }
        return false;
    }

    //Old find point FUCKED UP IDK MIEZ
    public static boolean FindPoint_old(int x1, int y1, int x2,
                                    int y2, int x, int y) {
        return x >= x1 && x <= x2 &&
                y >= y1 && y <= y2;
    }
    public static boolean FindPoint(int x1, int y1, int x2,
                                    int y2, int x, int y) {
        Point rc = new Point(x1,y1);
        Point lc = new Point(x2,y2);
        Point p = new Point(x,y);
        return doOverlap(rc,lc,p,p);
    }
/// TODO:     This need some optimization
    public static String sendFactionTerritory(Player p) {
        int faction = Integer.parseInt(playertools.getMetadata(p, "factionid"));
        Faction MeineFaction = Main.faction_cache.get(faction);
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                Point playerPoint= new Point(p.getLocation().getBlockX(),p.getLocation().getBlockZ());
                Point claimrc= new Point(val.startX, val.startZ);
                Point claimlc= new Point(val.endX, val.endZ);
                //if(FindPoint(claimrc.x,claimrc.z,claimlc.x,claimlc.z,playerPoint.x,playerPoint.z)){
                if(doOverlap(claimlc,claimrc,playerPoint,playerPoint)){
                //if (FindPoint(val.startX, val.startZ, val.endX, val.endZ, p.getLocation().getBlockX(), p.getLocation().getBlockZ())) {
                    if (faction == thisFaction.getValue().id)
                        return ChatColor.GREEN + MeineFaction.name;
                    return ChatColor.RED + Main.factionToname.get(thisFaction.getKey());
                }
            }
        }
        return ChatColor.DARK_GREEN + "Wilderness";
    }
    public static boolean isAreaNeutral(Location loc){
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                if (FindPoint_old(val.startX, val.startZ, val.endX, val.endZ, loc.getBlockX(), loc.getBlockZ())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String sendFactionTerretoryByXZ(int x, int z) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                if (FindPoint_old(val.startX, val.startZ, val.endX, val.endZ, x, z)) {
                    return ChatColor.RED + Main.factionToname.get(thisFaction.getKey());
                }
            }
        }
        return ChatColor.DARK_RED + "Unknown";
    }
    public static Faction_Claim sendClaimByXZ(int x, int z) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                if (FindPoint_old(val.startX, val.startZ, val.endX, val.endZ, x, z)) {
                   return val;
                }
            }
        }
        return null;
    }

    public static void CreateNewFakeTower(Player p, Location loc) {
        loc.setY(0);
        for (int i = 0; i <= 200; i++) {
            p.sendBlockChange(loc, Material.GLOWSTONE, (byte) 0);
            loc.setY(Double.parseDouble(Integer.toString(i)));
        }
        //fakePos.put(p, loc);
    }

    /*public static void DeletFakeTowers(Player p) {
        p.getWorld().regenerateChunk(fakePos.get(p).getChunk().getX(), fakePos.get(p).getChunk().getZ());
    }*/

    public static int calcMoneyOfArea(Player p) {
        int faction = Integer.parseInt(playertools.getMetadata(p, "factionid"));
        if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
            return (int) (calcBlocks(p) * Main.claim_price_multiplier);
        }
        return -1;
    }

    public static int calcBlocks(Player p) {
        int faction = Integer.parseInt(playertools.getMetadata(p, "factionid"));
        if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
            Point start = startpositions.get(faction);
            Point end = endpositions.get(faction);
            return Math.round((Math.abs(end.x - start.x) * Math.abs(end.z - start.z)));
        }
        return -1;
    }

    public static boolean SpawnPrepare(Player p) {
        if (p.getInventory().firstEmpty() != -1) {
            ItemStack wand = new ItemStack(Material.GOLD_HOE);
            ItemMeta meta = wand.getItemMeta();

            meta.setDisplayName("§e§oSpawn Claimer");

            List<String> str = new ArrayList<>();
            str.add("§aUse §o/admin claimspawn claim");
            str.add("§7to claim the area.");
            str.add("§cUse §o/admin claimspawn stop");
            str.add("§7to exit from the claim mode");
            str.add("§cDo NOT modify the faction with the ID 1");

            meta.setLore(str);

            wand.setItemMeta(meta);
            p.getInventory().setItem(p.getInventory().firstEmpty(), wand);
            return true;
        } else {
            p.sendMessage(Messages.NOT_ENOUGH_SLOT.queue());
        }

        return false;
    }
    public static boolean KothPrepare(Player p) {
        if (p.getInventory().firstEmpty() != -1) {
            ItemStack wand = new ItemStack(Material.IRON_HOE);
            ItemMeta meta = wand.getItemMeta();

            meta.setDisplayName("§e§oKoth Claimer");

            List<String> str = new ArrayList<>();
            str.add("§aMeow");

            meta.setLore(str);

            wand.setItemMeta(meta);
            p.getInventory().setItem(p.getInventory().firstEmpty(), wand);
            return true;
        } else {
            p.sendMessage(Messages.NOT_ENOUGH_SLOT.queue());
        }

        return false;
    }
    public static boolean CustomClaimPreparer(Player p,int factionid) {
        if (p.getInventory().firstEmpty() != -1) {
            ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
            ItemMeta meta = wand.getItemMeta();

            meta.setDisplayName("§e§oCustom Claimer");
            Faction f = Main.faction_cache.get(factionid);
            List<String> str = new ArrayList<>();
            if(f != null)
                str.add("§cName: §4"+ f.name);
            str.add("§cDo NOT modify the faction with the ID 1");



            meta.setLore(str);

            wand.setItemMeta(meta);
            p.getInventory().setItem(p.getInventory().firstEmpty(), wand);
            return true;
        } else {
            p.sendMessage(Messages.NOT_ENOUGH_SLOT.queue());
        }

        return false;
    }

    // Marci <333333 Adbi Igen adrián?
    public static class Point {
        public int x, z;

        public Point(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }
    public static enum ClaimAttributes {
        PROTECTED,
        NORMAL,
        KOTH,
        SPECIAL,
    }
    public static class Faction_Claim {
        public int startX;
        public int endX;
        public int startZ;
        public int endZ;
        public int faction;
        //Attributes: Protected, KOTH, normal,Special
        public ClaimAttributes attribute;
        public Faction_Claim(int startX, int endX, int startZ, int endZ, int faction,ClaimAttributes attribute) {
            this.startX = startX;
            this.endX = endX;
            this.startZ = startZ;
            this.endZ = endZ;
            this.faction = faction;
            this.attribute = attribute;
        }
    }

    public static Location ReturnSafeSpot(Location old){
        Location loc = null;
        double record = 9999;
        int playerx= old.getBlockX();
        int playery = old.getBlockY();
        int playerz = old.getBlockZ();
        for(int blockx=playerx-60;blockx<playerx+60;blockx++){
            for(int blocky=playery-60;blocky<playery+60;blocky++){
                for(int blockz=playerz-60;blockz<playerz+60;blockz++){
                    loc = new Location(old.getWorld(),blockx,blocky,blockz);
                    if(loc.getBlock().getType().equals(Material.AIR)){
                        if(isAreaNeutral(loc)){
                            if(old.distance(loc) < record){
                                record = old.distance(loc);
                            }
                        }
                    }
                }
            }
        }

        return loc;
    }



}
