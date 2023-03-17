package me.idbi.hcf.Tools;

import lombok.Getter;
import lombok.Setter;
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

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static me.idbi.hcf.Tools.Playertools.CheckClaimPlusOne;
import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class HCF_Claiming {
    private static final Connection con = Main.getConnection();
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
                int startX = Math.min(faction_start.getX(), faction_end.getX());
                int startZ = Math.min(faction_start.getZ(), faction_end.getZ());
                int endX = Math.max(faction_start.getX(), faction_end.getX());
                int endZ = Math.max(faction_start.getZ(), faction_end.getZ());

                HCF_Claiming.Point top_left = new HCF_Claiming.Point(faction_start.getX(), faction_end.getZ());
                HCF_Claiming.Point bottom_right = new HCF_Claiming.Point(faction_end.getX(), faction_start.getZ());
                if(getDistanceBetweenPoints2D(top_left,faction_end) < Config.MinClaimSize.asInt() || getDistanceBetweenPoints2D(faction_start,top_left) < Config.MinClaimSize.asInt() ){
                    p.sendMessage(Messages.faction_claim_too_small.language(p).setNumber(Config.MinClaimSize.asInt()).queue());
                    return false;
                }
                if(getDistanceBetweenPoints2D(top_left,faction_end) > Config.MaxClaimSize.asInt() || getDistanceBetweenPoints2D(faction_start,top_left) > Config.MaxClaimSize.asInt() ){
                    p.sendMessage(Messages.faction_claim_too_small.language(p).setNumber(Config.MinClaimSize.asInt()).queue());
                    return false;
                }
                //for (Map.Entry<Integer, Faction_Claim> entry : Main.faction_cache.get(faction).claims.entrySet()) {
                for (Faction f : Main.factionCache.values()) {

                    //if (f.claims.isEmpty()) continue;

                    for (HCF_Claiming.Faction_Claim val : f.getClaims()) {

                        Point start_this = new Point(Math.min(val.getStartX(), val.getEndX()), Math.min(val.getStartZ(), val.getEndZ()));
                        Point end_this = new Point(Math.max(val.getStartX(), val.getEndX()), Math.max(val.getStartZ(), val.getEndZ()));

                        /*Point start_other = new Point(faction_start.getX(), faction_start.getZ());
                        Point end_other = new Point(faction_end.getX(), faction_end.getZ());*/


                        if (doOverlap(faction_start, faction_end, start_this, end_this)) {
                            //if (doOverlap(new Point(val.getStartX(), val.getEndX()), new Point(val.getStartZ(), val.getEndZ()), new Point(start[0], end[0]), new Point(start[1], end[1]))) {
                            p.sendMessage(Messages.faction_claim_overlap.language(p).queue());
                            return false;
                        }
                        if (CheckClaimPlusOne(faction_start, faction_end, 1, start_this, end_this)) {
                            p.sendMessage(Messages.faction_claim_overlap_plus_one.language(p).queue());
                            return false;
                        }

                    }
                }
                Faction f = Main.factionCache.get(faction);
                boolean validClaim = false;

                if(!f.getClaims().isEmpty() && Config.MustBeConnected.asBoolean()) {
                    for(Faction_Claim claim : f.getClaims()) {
                        if (isClaimConnected(new Faction_Claim(startX,endX,startZ,endZ,1,ClaimAttributes.NORMAL,"UwU"),claim)) {
                            validClaim = true;
                        }
                    }
                }
                if(!validClaim && !f.getClaims().isEmpty() && Config.MustBeConnected.asBoolean()){
                    p.sendMessage(Messages.faction_claim_not_connected.language(p).queue());
                    return false;
                }

                if (Playertools.getPlayerBalance(p) - calcMoneyOfArea(p) >= 0) {
                    HCFPlayer hcf = HCFPlayer.getPlayer(p);
                    hcf.setMoney(hcf.getMoney() - calcMoneyOfArea(p));
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

                endpositions.remove(faction);
                startpositions.remove(faction);
                HCF_Claiming.Faction_Claim claim;
                claim = new HCF_Claiming.Faction_Claim(startX, endX, startZ, endZ, f.getId(), attribute, p.getWorld().getName());
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
                int startX = Math.min(faction_start.getX(), faction_end.getX());
                int startZ = Math.min(faction_start.getZ(), faction_end.getZ());
                int endX = Math.max(faction_start.getX(), faction_end.getX());
                int endZ = Math.max(faction_start.getZ(), faction_end.getZ());
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
                Faction f = Main.factionCache.get(faction);
                endpositions.remove(faction);
                startpositions.remove(faction);
                HCF_Claiming.Faction_Claim claim = new HCF_Claiming.Faction_Claim(startX, endX, startZ, endZ, f.getId(), attribute, p.getWorld().getName());
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
        int firstRectangle_Starting_X = l1.getX();
        int firstRectangle_Starting_Z = l1.getZ();

        int firstRectangle_Ending_X = r1.getX();
        int firstRectangle_Ending_Z = r1.getZ();

        int maxX = Math.max(firstRectangle_Starting_X, firstRectangle_Ending_X);
        int minX = Math.min(firstRectangle_Starting_X, firstRectangle_Ending_X);

        int maxZ = Math.max(firstRectangle_Starting_Z, firstRectangle_Ending_Z);
        int minZ = Math.min(firstRectangle_Starting_Z, firstRectangle_Ending_Z);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (FindPoint_old(l2.getX(), l2.getZ(), r2.getX(), r2.getZ(), x, z)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean doOverlap3(Point l1, Point r1, Point l2, Point r2) {
        // If one rectangle is on left side of other
        int firstRectangle_Starting_X = l1.getX();
        int firstRectangle_Starting_Z = l1.getZ();

        int firstRectangle_Ending_X = r1.getX();
        int firstRectangle_Ending_Z = r1.getZ();


        int minX = Math.min(firstRectangle_Starting_X, firstRectangle_Ending_X);
        int minZ = Math.min(firstRectangle_Starting_Z, firstRectangle_Ending_Z);
        int maxX = Math.max(firstRectangle_Starting_X, firstRectangle_Ending_X);
        int maxZ = Math.max(firstRectangle_Starting_Z, firstRectangle_Ending_Z);

        Point bottom_left = new Point(minX, minZ);
        Point top_right = new Point(maxX, maxZ);
        Point top_left = new Point(top_right.getX(), bottom_left.getZ());
        Point bottom_right = new Point(bottom_left.getX(), top_right.getZ());

        int width = getDistanceBetweenPoints2D(bottom_left, top_left) + 1;

        int height = getDistanceBetweenPoints2D(bottom_left, bottom_right) + 1;

        for (int x = minX; x <= minX + width; x++) {
            if (FindPoint_old(l1.getX(), l1.getZ(), r1.getX(), r1.getZ(), x, minZ)) {
                return true;
            }
            if (FindPoint_old(l2.getX(), l2.getZ(), r2.getX(), r2.getZ(), x, height - 1)) {
                return true;
            }
            //Main.sendCmdMessage("");
        }
        for (int y = minZ; y <= minZ + height; y++) {
            if (FindPoint_old(l2.getX(), l2.getZ(), r2.getX(), r2.getZ(), minX, y)) {
                return true;
            }
            if (FindPoint_old(l2.getX(), l2.getZ(), r2.getX(), r2.getZ(), width - 1, y)) {
                return true;
            }
        }
        return false;
    }
    public static boolean megvanakellotavolsag(Point ownClaim1, Point ownClaim2, Point otherClaim1, Point otherClaim2) {
        if ((ownClaim1.getX() - otherClaim1.getX()) > 1
                || (ownClaim1.getX() - otherClaim1.getX()) < -1) {
            if ((ownClaim1.getX() - otherClaim2.getX()) > 1
                    || (ownClaim1.getX() - otherClaim2.getX()) < -1) {
                if ((ownClaim2.getX() - otherClaim1.getX()) > 1
                        || (ownClaim2.getX() - otherClaim1.getX()) < -1) {
                    if ((ownClaim2.getX() - otherClaim2.getX()) > 1
                            || (ownClaim2.getX() - otherClaim2.getX()) < -1) {
                        return true;
                    }
                }
            }
        }

        ////////////////////////////////////////////

        if ((ownClaim1.getZ() - otherClaim1.getZ()) > 1
                || (ownClaim1.getZ() - otherClaim1.getZ()) < -1) {
            if ((ownClaim1.getZ() - otherClaim2.getZ()) > 1
                    || (ownClaim1.getZ() - otherClaim2.getZ()) < -1) {
                if ((ownClaim2.getZ() - otherClaim1.getZ()) > 1
                        || (ownClaim2.getZ() - otherClaim1.getZ()) < -1) {
                    return (ownClaim2.getZ() - otherClaim2.getZ()) > 1
                            || (ownClaim2.getZ() - otherClaim2.getZ()) < -1;
                }
            }
        }


        return false;
    }

    // true -> igen ez enemy claim action
//    public static boolean checkEnemyAction(int x, int z, Faction faction) {
//        for (Map.Entry<Integer, Faction> thisFaction : Main.faction_cache.entrySet()) {
//            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims) {
//                Point start = new Point(val.getStartX(),val.getStartZ());
//                Point end = new Point(val.getEndX(),val.getEndZ());
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

    public static boolean isClaimConnected(Faction_Claim a,Faction_Claim b) {
        Point aStart = new Point(a.getStartX(),a.getStartZ());
        Point aEnd = new Point(a.getEndX(),a.getEndZ());
        Point bStart = new Point(b.getStartX(),b.getStartZ());
        Point bEnd = new Point(b.getEndX(),b.getEndZ());
        if(!doOverlap(aStart,aEnd,bStart,bEnd)) {
            return CheckClaimPlusOne(aStart, aEnd, 1, bStart, bEnd);
        }
        return false;
    }

    public static boolean isEnemyClaim(Player executePlayer, Faction_Claim actionClaim) {

        Faction playerFac = Playertools.getPlayerFaction(executePlayer);
        Faction actionFaction = actionClaim.getFaction();
        if (playerFac == null) {
            return true;
        }

        return playerFac.getId() != actionFaction.getId();
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
        for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().getClaims()) {

                int x = p.getLocation().getBlockX();
                int z = p.getLocation().getBlockZ();

                if (val.getStartX() <= x && val.getEndX() >= x) {
                    if (val.getStartZ() <= z && val.getEndZ() >= z) {
                        return val;
                    }
                }

                //if(FindPoint(claimrc.getX(),claimrc.getZ(),claimlc.getX(),claimlc.getZ(),playerPoint.getX(),playerPoint.getZ())){
                /*if(doOverlap(claimlc,claimrc,playerPoint,playerPoint)) {
                    return val;
                }*/
            }
        }
        return null;
    }

    public static boolean isAreaNeutral(Location loc) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().getClaims()) {
                if (FindPoint_old(val.getStartX(), val.getStartZ(), val.getEndX(), val.getEndZ(), loc.getBlockX(), loc.getBlockZ())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String sendFactionTerretoryByXZ(Player p, int x, int z) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().getClaims()) {
                if (FindPoint_old(val.getStartX(), val.getStartZ(), val.getEndX(), val.getEndZ(), x, z)) {
                    return Config.EnemyColor.asStr() + thisFaction.getValue().getName();
                }
            }
        }
        return Messages.wilderness.language(p).queue();
    }

    public static Faction_Claim sendClaimByXZ(int x, int z) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().getClaims()) {
                Point playerPoint = new Point(x, z);
                Point claimrc = new Point(val.getStartX(), val.getStartZ());
                Point claimlc = new Point(val.getEndX(), val.getEndZ());
                if (doOverlap(claimlc, claimrc, playerPoint, playerPoint)) {
                    return val;
                }
            }
        }
        return null;
    }
    public static boolean isClaimBorder(int x, int z) {
        for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
            for (HCF_Claiming.Faction_Claim val : thisFaction.getValue().getClaims()) {
                Point playerPoint = new Point(x, z);

                int maxX = Math.max(val.getStartX(), val.getEndX());
                int minX = Math.min(val.getStartX(), val.getEndX());

                int maxZ = Math.max(val.getStartZ(), val.getEndZ());
                int minZ = Math.min(val.getStartZ(), val.getEndZ());

                if(x == maxX || x == minX) {
                    if(z >= minZ && z <= maxZ) {
                        return true;
                    }
                }

                if(z == maxZ || z == minZ) {
                    if(x >= minX && x <= maxX) {
                        return true;
                    }
                }

                /*Point claimrc = new Point(val.getStartX(), val.getStartZ());
                Point claimlc = new Point(val.getEndX(), val.getEndZ());
                if (doOverlap3(claimlc, claimrc, playerPoint, playerPoint)) {
                    System.out.println("Found target");
                    return true;
                }*/
            }
        }
        return false;
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
        int faction = player.getFaction().getId();
        if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
            return (int) (calcBlocks(p) * Main.claimPriceMultiplier);
        }
        return -1;
    }

    public static int calcBlocks(Player p) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        int faction = player.getFaction().getId();
        if (startpositions.containsKey(faction) && endpositions.containsKey(faction)) {
            Point start = startpositions.get(faction);
            Point end = endpositions.get(faction);
            return Math.round((Math.abs(end.getX() - start.getX()) * Math.abs(end.getZ() - start.getZ())));
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

            meta.setDisplayName(Config.ClaimingWandTitle.asStr());
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
        @Getter @Setter private int x, z;

        public Point(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    public static class Faction_Claim {
        @Getter @Setter private World world;
        @Getter @Setter private int startX;
        @Getter @Setter private int endX;
        @Getter @Setter private int startZ;
        @Getter @Setter private int endZ;
        @Getter @Setter private Faction faction;
        //Attributes: Protected, KOTH, normal,Special
        @Getter @Setter private ClaimAttributes attribute;

        public Faction_Claim(int startX, int endX, int startZ, int endZ, int faction, ClaimAttributes attribute, String world) {
            this.startX = startX;
            this.endX = endX;
            this.startZ = startZ;
            this.endZ = endZ;
            this.faction = Main.factionCache.get(faction);
            this.attribute = attribute;
            this.world = Bukkit.getWorld(world);
            if (this.world == null) {
                this.world = Bukkit.getWorld(Config.WorldName.asStr());
            }

        }
    }

    public static class Wands {
        public static ItemStack claimWand() {
            ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
            ItemMeta meta = wand.getItemMeta();

            meta.setDisplayName(Config.ClaimingWandTitle.asStr());
            meta.setLore(Config.ClaimingWandTitle.asChatColorList());

            wand.setItemMeta(meta);
            return wand;
        }
    }

}
