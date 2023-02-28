package me.idbi.hcf.Tools;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class HCF_Claiming {
    private static final Connection con = Main.getConnection("HCF_Claiming");
    public static final HashMap<Integer, Point> startpositions = new HashMap<>();
    public static final HashMap<Integer, Point> endpositions = new HashMap<>();

    public static final HashMap<Player, ArrayList<Location>> ConnectPosition = new HashMap<>();

    public static void setStartPosition(int faction, int x, int z) {
        if (!startpositions.containsKey(faction)) {
            Point p = new Point(x, z);
            startpositions.put(faction, p);
        } else {
            startpositions.remove(faction);
            Point p = new Point(x, z);
            startpositions.put(faction, p);
        }
    }

    public static void setEndPosition(int faction, int x, int z) {
        try {
            if (!endpositions.containsKey(faction)) {
                Point p = new Point(x, z);
                endpositions.put(faction, p);
            } else {
                endpositions.remove(faction);
                Point p = new Point(x, z);
                endpositions.put(faction, p);
            }
        } catch (Exception ignored) {
        }

    }

    public static void removeClaiming(int faction) {
        startpositions.remove(faction);
        endpositions.remove(faction);
    }

    public static boolean FinishClaiming(int faction, Player p, ClaimAttributes attribute) {
        try {
            if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
                Point faction_start = startpositions.get(faction);
                Point faction_end = endpositions.get(faction);
                int startX = Math.min(faction_start.x, faction_end.x);
                int startZ = Math.min(faction_start.z, faction_end.z);
                int endX = Math.max(faction_start.x, faction_end.x);
                int endZ = Math.max(faction_start.z, faction_end.z);

                //for (Map.Entry<Integer, Faction_Claim> entry : Main.faction_cache.get(faction).claims.entrySet()) {
                for (Faction f : Main.faction_cache.values()) {

                    //if (f.claims.isEmpty()) continue;

                    for (HCF_Claiming.Faction_Claim val : f.claims) {

                        Point start_this = new Point(Math.min(val.startX, val.endX), Math.min(val.startZ, val.endZ));
                        Point end_this = new Point(Math.max(val.startX, val.endX), Math.max(val.startZ, val.endZ));

                        /*Point start_other = new Point(faction_start.x, faction_start.z);
                        Point end_other = new Point(faction_end.x, faction_end.z);*/


                        if (doOverlap(faction_start, faction_end, start_this, end_this)) {
                            //if (doOverlap(new Point(val.startX, val.endX), new Point(val.startZ, val.endZ), new Point(start[0], end[0]), new Point(start[1], end[1]))) {
                            p.sendMessage(Messages.faction_claim_overlap.language(p).queue());
                            return false;
                        }
                        if (calcBlocks(p) < 4) {
                            p.sendMessage(Messages.faction_claim_too_small.language(p).queue());
                            return false;
                        }
                        if (Playertools.CheckClaimPlusOne(faction_start, faction_end, 1, start_this, end_this)) {
                            p.sendMessage(Messages.faction_claim_overlap.language(p).queue());
                            return false;
                        }

                    }
                }
                if (Playertools.getPlayerBalance(p) - calcMoneyOfArea(p) >= 0) {
                    HCFPlayer hcf = HCFPlayer.getPlayer(p);
                    hcf.setMoney(hcf.money - calcMoneyOfArea(p));
                } else {
                    if (attribute.equals(ClaimAttributes.NORMAL)) {
                        p.sendMessage(Messages.not_enough_money.language(p).queue());
                        return false;
                    }
                }
                SQL_Connection.dbExecute(
                        con,
                        "INSERT INTO claims SET factionid='?',startX='?',startZ='?',endX='?',endZ='?', type='?',world='?'",
                        String.valueOf(faction),
                        startX + "",
                        startZ + "",
                        endX + "",
                        endZ + "",
                        attribute.name().toLowerCase(),
                        p.getWorld().getName()
                );
                //HandleSpawn
                Faction f = Main.faction_cache.get(faction);
                endpositions.remove(faction);
                startpositions.remove(faction);
                HCF_Claiming.Faction_Claim claim;
                claim = new HCF_Claiming.Faction_Claim(startX, endX, startZ, endZ, f.id, attribute, p.getWorld().getName());
                f.addClaim(claim);
                Scoreboards.RefreshAll();
                return true;
            }
            return false;
        } catch (Exception e) {
            p.sendMessage(Messages.error_while_executing.language(p).queue());
        }
        return false;
    }

    public static boolean ForceFinishClaim(int faction, Player p, ClaimAttributes attribute) {
        try {
            if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
                Point faction_start = startpositions.get(faction);
                Point faction_end = endpositions.get(faction);
                int startX = Math.min(faction_start.x, faction_end.x);
                int startZ = Math.min(faction_start.z, faction_end.z);
                int endX = Math.max(faction_start.x, faction_end.x);
                int endZ = Math.max(faction_start.z, faction_end.z);
                SQL_Connection.dbExecute(
                        con,
                        "INSERT INTO claims SET factionid='?',startX='?',startZ='?',endX='?',endZ='?', type='?',world='?'",
                        String.valueOf(faction),
                        startX + "",
                        startZ + "",
                        endX + "",
                        endZ + "",
                        attribute.name().toLowerCase(),
                        p.getWorld().getName()
                );
                //HandleSpawn
                Faction f = Main.faction_cache.get(faction);
                endpositions.remove(faction);
                startpositions.remove(faction);
                HCF_Claiming.Faction_Claim claim = new HCF_Claiming.Faction_Claim(startX, startZ, endX, endZ, f.id, attribute, p.getWorld().getName());
                f.addClaim(claim);
                Scoreboards.RefreshAll();


                return true;
            }
            return false;
        } catch (Exception e) {
            p.sendMessage(Messages.error_while_executing.language(p).queue());
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

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (FindPoint_old(l2.x, l2.z, r2.x, r2.z, x, z)) {
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

        Point bottom_left = new Point(minX, minZ);
        Point top_right = new Point(maxX, maxZ);
        Point top_left = new Point(top_right.x, bottom_left.z);
        Point bottom_right = new Point(bottom_left.x, top_right.z);

        int width = getDistanceBetweenPoints2D(bottom_left, top_left) + 1;

        int height = getDistanceBetweenPoints2D(bottom_left, bottom_right) + 1;

        for (int x = minX; x <= minX + width; x++) {
            if (FindPoint_old(l1.x, l1.z, r1.x, r1.z, x, minZ)) {
                return true;
            }
            if (FindPoint_old(l2.x, l2.z, r2.x, r2.z, x, height - 1)) {
                return true;
            }
            //Main.sendCmdMessage("");
        }
        for (int y = minZ; y <= minZ + height; y++) {
            if (FindPoint_old(l2.x, l2.z, r2.x, r2.z, minX, y)) {
                return true;
            }
            if (FindPoint_old(l2.x, l2.z, r2.x, r2.z, width - 1, y)) {
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

    // true -> igen ez enemy claim action
//    public static boolean checkEnemyAction(int x, int z, Faction faction) {
//        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
//            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
//                Point start = new Point(val.startX,val.startZ);
//                Point end = new Point(val.endX,val.endZ);
//                Point p = new Point(x,z);
//
//                if (doOverlap(start,end,p,p)) {
//                    if (
//                            val.attribute.equals(ClaimAttributes.PROTECTED)
//                            || val.attribute.equals(ClaimAttributes.KOTH)
//                            || val.attribute.equals(ClaimAttributes.SPECIAL
//                    ))
//                        return true;
//
//                    if(thisFaction.getValue().DTR <= 0)
//                        return false;
//
//                    int factionId = faction == null ? -1 : faction.id;
//                    return (factionId != thisFaction.getValue().id);
//                }
//            }
//        }
//
//        return false;
//    }
    public static boolean isEnemyClaim(Player executePlayer, Faction_Claim actionClaim) {

        Faction playerFac = Playertools.getPlayerFaction(executePlayer);
        Faction actionFaction = actionClaim.faction;
        if (playerFac == null) {
            return true;
        }

        return playerFac.id != actionFaction.id;
    }

    //Old find point FUCKED UP IDK MIEZ
    public static boolean FindPoint_old(int x1, int y1, int x2,
                                        int y2, int x, int y) {
        return x >= x1 && x <= x2 &&
                y >= y1 && y <= y2;
    }

    public static boolean FindPoint(int x1, int y1, int x2,
                                    int y2, int x, int y) {
        Point rc = new Point(x1, y1);
        Point lc = new Point(x2, y2);
        Point p = new Point(x, y);
        return doOverlap(rc, lc, p, p);
    }

    /// TODO:     This need some optimization
    public static HCF_Claiming.Faction_Claim getPlayerArea(Player p) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {

                int x = p.getLocation().getBlockX();
                int z = p.getLocation().getBlockZ();

                if (val.startX <= x && val.endX >= x) {
                    if (val.startZ <= z && val.endZ >= z) {
                        return val;
                    }
                }

                //if(FindPoint(claimrc.x,claimrc.z,claimlc.x,claimlc.z,playerPoint.x,playerPoint.z)){
                /*if(doOverlap(claimlc,claimrc,playerPoint,playerPoint)) {
                    return val;
                }*/
            }
        }
        return null;
    }

    public static boolean isAreaNeutral(Location loc) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                if (FindPoint_old(val.startX, val.startZ, val.endX, val.endZ, loc.getBlockX(), loc.getBlockZ())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String sendFactionTerretoryByXZ(Player p, int x, int z) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                if (FindPoint_old(val.startX, val.startZ, val.endX, val.endZ, x, z)) {
                    return Config.enemy_color.asStr() + thisFaction.getValue().name;
                }
            }
        }
        return Messages.wilderness.language(p).queue();
    }

    public static Faction_Claim sendClaimByXZ(int x, int z) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
                Point playerPoint = new Point(x, z);
                Point claimrc = new Point(val.startX, val.startZ);
                Point claimlc = new Point(val.endX, val.endZ);
                if (doOverlap(claimlc, claimrc, playerPoint, playerPoint)) {
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
        HCFPlayer player = HCFPlayer.getPlayer(p);
        int faction = player.faction.id;
        if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
            return (int) (calcBlocks(p) * Main.claim_price_multiplier);
        }
        return -1;
    }

    public static int calcBlocks(Player p) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        int faction = player.faction.id;
        if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
            Point start = startpositions.get(faction);
            Point end = endpositions.get(faction);
            return Math.round((Math.abs(end.x - start.x) * Math.abs(end.z - start.z)));
        }
        return -1;
    }

    public static boolean SpawnPrepare(Player p) {
        if (p.getInventory().firstEmpty() != -1) {

            p.getInventory().setItem(p.getInventory().firstEmpty(), HCF_Claiming.Wands.claimWand());
            return true;
        } else {
            p.sendMessage(Messages.not_enough_slot.language(p).queue());
        }

        return false;
    }

    public static boolean KothPrepare(Player p) {
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().setItem(p.getInventory().firstEmpty(), HCF_Claiming.Wands.claimWand());
            return true;
        } else {
            p.sendMessage(Messages.not_enough_slot.language(p).queue());
        }

        return false;
    }

    public static boolean CustomClaimPreparer(Player p, int factionid) {
        if (p.getInventory().firstEmpty() != -1) {
            ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
            ItemMeta meta = wand.getItemMeta();

            meta.setDisplayName(Config.claiming_wand_name.asStr());
            // meta.setLore(Config.claiming_wand_name.asChatColorList());
            /*Faction f = Main.faction_cache.get(factionid);
            List<String> str = new ArrayList<>();
            if(f != null)
                str.add("§cName: §4"+ f.name);
            str.add("§cDo NOT modify the faction with the ID 1");*/


            //meta.setLore(str);

            wand.setItemMeta(meta);
            p.getInventory().setItem(p.getInventory().firstEmpty(), wand);
            return true;
        } else {
            p.sendMessage(Messages.not_enough_slot.language(p).queue());
        }

        return false;
    }

    public static Location ReturnSafeSpot(Location old) {
        Location loc = null;
        double record = 9999;
        int playerx = old.getBlockX();
        int playery = old.getBlockY();
        int playerz = old.getBlockZ();
        for (int blockx = playerx - 60; blockx < playerx + 60; blockx++) {
            for (int blocky = playery - 60; blocky < playery + 60; blocky++) {
                for (int blockz = playerz - 60; blockz < playerz + 60; blockz++) {
                    loc = new Location(old.getWorld(), blockx, blocky, blockz);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        if (isAreaNeutral(loc)) {
                            if (old.distance(loc) < record) {
                                record = old.distance(loc);
                            }
                        }
                    }
                }
            }
        }

        return loc;
    }

    public enum ClaimAttributes {
        PROTECTED,
        NORMAL,
        KOTH,
        SPECIAL,
    }

    public enum ClaimTypes {

        FACTION,
        PROTECTED,
        SPAWN,
        KOTH,
        END,
        SPECIAL,
        NONE
    }

    public static class Point {
        public int x, z;

        public Point(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    public static class Faction_Claim {
        public World world;
        public int startX;
        public int endX;
        public int startZ;
        public int endZ;
        public Faction faction;
        //Attributes: Protected, KOTH, normal,Special
        public ClaimAttributes attribute;

        public Faction_Claim(int startX, int endX, int startZ, int endZ, int faction, ClaimAttributes attribute, String world) {
            this.startX = startX;
            this.endX = endX;
            this.startZ = startZ;
            this.endZ = endZ;
            this.faction = Main.faction_cache.get(faction);
            this.attribute = attribute;
            this.world = Bukkit.getWorld(world);
            if (this.world == null) {
                this.world = Bukkit.getWorld(Config.world_name.asStr());
            }

        }
    }

    public static class Wands {
        public static ItemStack claimWand() {
            ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
            ItemMeta meta = wand.getItemMeta();

            meta.setDisplayName(Config.claiming_wand_name.asStr());
            meta.setLore(Config.claiming_wand_name.asChatColorList());

            wand.setItemMeta(meta);
            return wand;
        }
    }

}
