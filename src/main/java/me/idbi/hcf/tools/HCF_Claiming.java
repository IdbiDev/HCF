package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HCF_Claiming {
    private static final HashMap<Integer, Integer[] > startpositions = new HashMap<Integer, Integer[]>();
    private static final HashMap<Integer,Integer[]> endpositions = new HashMap<Integer, Integer[]>();

    private static final HashMap<Location,Player> fakePos = new HashMap<Location,Player>();
    public static void setStartPosition(int faction, int x,int z){
        if(Main.debug)
            System.out.println("Setting position to START");
        if(!startpositions.containsKey(faction)){
            Integer[] temp = {x,z};
            startpositions.put(faction,temp);
        }else {
            startpositions.remove(faction);
            Integer[] temp = {x,z};
            startpositions.put(faction,temp);
        }
    }
    public static void setEndPosition(int faction, int x,int z){
        if(Main.debug)
            System.out.println("Setting position to END");
        if(!endpositions.containsKey(faction)){
            Integer[] temp = {x,z};
            endpositions.put(faction,temp);
        }else {
            endpositions.remove(faction);
            Integer[] temp = {x,z};
            endpositions.put(faction,temp);
        }
    }
    public static void removeClaiming(int faction){
        startpositions.remove(faction);
        endpositions.remove(faction);
    }
    public static boolean FinishClaiming(int faction){
        if(Main.debug)
            System.out.println("Finishing Claiming");
        if(startpositions.containsKey(faction) && endpositions.containsKey(faction)){
            Integer[] start = startpositions.get(faction);
            Integer[] end = endpositions.get(faction);
            //for (Map.Entry<Integer, Faction_Claim> entry : Main.faction_cache.get(faction).claims.entrySet()) {
            for(HCF_Claiming.Faction_Claim val : Main.faction_cache.get(faction).claims){
                if(doOverlap(new Point(val.startX, val.endX), new Point(val.startZ,val.endZ), new Point(start[0],end[0]), new Point(start[1],end[1]))){
                    System.out.println("Overlap");
                    return false;
                }
            }
            int claimid = SQL_Connection.dbExecute(Main.getConnection("commands.Claim"),"INSERT INTO claims SET factionid='?',startX='?',startZ='?',endX='?',endZ='?'",
                    String.valueOf(faction),start[0].toString(),start[1].toString(),end[0].toString(),end[1].toString());
            if(Main.debug)
                System.out.println("Claiming >> Success");
            Main.Faction f = Main.faction_cache.get(faction);
            HCF_Claiming.Faction_Claim claim = new HCF_Claiming.Faction_Claim(start[0],end[0],start[1],end[1],claimid);
            f.addClaim(claim);
            return true;
        }
        if(Main.debug)
            System.out.println("Claiming >> Invaild Zone");
        return false;
    }
    // Marci <333333
    static class Point {
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }
        int x, y;
    }
    static  boolean doOverlap(Point l1, Point r1, Point l2, Point r2) {

        // If one rectangle is on left side of other
        if (l1.x >r2.x || l2.x > r1.x) {
            return false;
        }

        // If one rectangle is above other
        // If one rectangle is above other
        if (r1.y > l2.y || r2.y > l1.y) {
            return false;
        }

        return true;
    }
    public static boolean validClaim(int startX1, int startX2, int endX1, int endX2)
    {
        if (startX2>startX1 && endX2>startX1){
            if (startX2 < endX1 || endX2 < endX1) {
                //False
                return false;
            }
        }
        else if (startX1>startX2 && endX2<startX1){
            if (startX2 > endX1 || endX2 > endX1) {
                //False
                return false;
            }
        }
        else {
            //False
            return false;
        }

        return true;
    }
    // true -> igen ez enemy claim action
    public static boolean checkEnemyClaimAction(int x, int z,int faction){
        Main.Faction MeineFaction = Main.faction_cache.get(faction);
        for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()) {
            for(HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims){
                if (FindPoint(val.startX,val.startZ,val.endX,val.endZ,x,z)) {
                    if(thisFaction.getKey() == -1){
                        return true;
                    }
                    return faction != thisFaction.getValue().factionid;
                }
            }
        }
        return false;
    }
    public static boolean FindPoint(int x1, int y1, int x2,
                             int y2, int x, int y)
    {
        return x > x1 && x < x2 &&
                y > y1 && y < y2;
    }

    public static String sendFactionTerretory(Player p){
        int faction = Integer.parseInt(playertools.getMetadata(p,"factionid"));
        Main.Faction MeineFaction = Main.faction_cache.get(faction);
        for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()) {
            for(HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims){
                if(FindPoint(val.startX,val.startZ,val.endX,val.endZ,p.getLocation().getBlockX(),p.getLocation().getBlockZ())) {
                    if (thisFaction.getKey() == -1) {
                        return ChatColor.RED + thisFaction.getValue().factioname;
                    }
                    if (faction == thisFaction.getValue().factionid)
                        return ChatColor.GREEN + MeineFaction.factioname;
                    return ChatColor.RED + Main.factionToname.get(thisFaction.getKey());

                    //X coords check
                    //claimX = validClaim(val.startX, p.getLocation().getBlockX(), val.endX,  p.getLocation().getBlockX());
                    //Z coords check
                    //claimZ = validClaim(val.startZ,  p.getLocation().getBlockZ(), val.endZ, p.getLocation().getBlockZ());
                    // Enemy check

                }
            }
        }
        return ChatColor.DARK_GREEN+"Wilderness";
    }
    public static String sendFactionTerretoryByXZ(int x, int z){
        for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()) {
            for(HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims){
                if(FindPoint(val.startX,val.startZ,val.endX,val.endZ,x,z)) {
                    return ChatColor.RED + Main.factionToname.get(thisFaction.getKey());

                }
            }
        }
        return ChatColor.DARK_RED+"Unknown";
    }

    public static void CreateNewFakeTower(Player p,Location loc){
        loc.setY(0);
        for(Integer i =0;i<=200;i++){
            p.sendBlockChange(loc, Material.GLOWSTONE,(byte) 0);
            loc.setY(Double.parseDouble(i.toString()));
        }
        fakePos.put(loc,p);
    }

    public static void DeletFakeTowers(Player p){
        for (Map.Entry<Location, Player> entry : fakePos.entrySet()) {
            Location key = entry.getKey();
            key.setY(0);
            for(int i = 0; i<=256; i++){

                key.getBlock().getState().update(true);
                key.setY(Double.parseDouble(Integer.toString(i)));
            }

        }
    }

    public static int calcMoneyOfArea(Player p){
        int faction = Integer.parseInt(playertools.getMetadata(p,"factionid"));
        if(startpositions.containsKey(faction) && endpositions.containsKey(faction)){
            Integer[] start = startpositions.get(faction);
            Integer[] end = endpositions.get(faction);
            return (int) Math.round((Math.abs(end[0]-start[0]) * Math.abs(end[1]-start[1]))*Main.claim_price_multiplier);
        }
        return -1;
    }
    public static int calcBlocks(Player p){
        int faction = Integer.parseInt(playertools.getMetadata(p,"factionid"));
        if(startpositions.containsKey(faction) && endpositions.containsKey(faction)){
            Integer[] start = startpositions.get(faction);
            Integer[] end = endpositions.get(faction);
            return Math.round((Math.abs(end[0]-start[0]) * Math.abs(end[1]-start[1])));
        }
        return -1;
    }


    public static class Faction_Claim {
        int startX;
        int endX;
        int startZ;
        int endZ;
        int faction;
        public Faction_Claim(int startX,int endX,int startZ,int endZ,int faction){
            this.startX = startX;
            this.endX = endX;
            this.startZ = startZ;
            this.endZ = endZ;
            this.faction = faction;
        }
        @Deprecated
        public void getRetardedVersion(){
            System.out.println("meleg");
        }
    }
    public static boolean SpawnPrepare(Player p){
        if(p.getInventory().firstEmpty() != -1){
            ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
            ItemMeta meta = wand.getItemMeta();

            List<String> str = Arrays.asList("Cicacsomag");

            meta.setLore(str);
            wand.setItemMeta(meta);
            p.getInventory().setItem(p.getInventory().firstEmpty(),wand);
            return true;
        }else{
            p.sendMessage(Messages.NOT_ENOUGH_SLOT.queue());
        }

        return false;
    }

}
