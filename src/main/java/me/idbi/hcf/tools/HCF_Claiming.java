package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
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
                    boolean claimX;
                    boolean claimZ;
                    //X coords check
                    claimX = validClaim(val.startX, start[0], val.endX, end[0]);
                    //Z coords check
                    claimZ = validClaim(val.startZ,  start[1], val.endZ, end[1]);
                    // Enemy check
                    if (!claimX && !claimZ) {
                        if(Main.debug)
                            System.out.println("Claiming >> Enemy zone");
                        return false;
                    }
            }

            SQL_Connection.dbExecute(Main.getConnection("commands.Claim"),"INSERT INTO claims SET factionid='?',startX='?',startZ='?',endX='?',endZ='?'",
                    String.valueOf(faction),start[0].toString(),start[1].toString(),end[0].toString(),end[1].toString());
            if(Main.debug)
                System.out.println("Claiming >> Success");
            playertools.cacheFactionClaims();
            return true;
        }
        if(Main.debug)
            System.out.println("Claiming >> Invaild Zone");
        return false;
    }
    // Marci <333333
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
        boolean claimX;
        boolean claimZ;
        //for (Map.Entry<Integer, Faction_Claim> entry : Main.faction_cache.get(faction).claims.entrySet()) {
        if(faction == 0){
            return true;
        }
        for(HCF_Claiming.Faction_Claim val : Main.faction_cache.get(faction).claims){
            //X coords check
            claimX = validClaim(val.startX,x, val.endX, x);
            //Z coords check
            claimZ = validClaim(val.startZ, z, val.endZ, z);
            // Enemy check
            if (!claimX && !claimZ) {
                return true;
            }
        }
        return false;
    }

    public static String sendFactionTerretory(Player p){
        int faction = Integer.parseInt(playertools.getMetadata(p,"factionid"));
        boolean claimX;
        boolean claimZ;
        Main.Faction MeineFaction = Main.faction_cache.get(faction);
        for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()) {
            for(HCF_Claiming.Faction_Claim val : thisFaction.getValue().claims){
                //X coords check
                claimX = validClaim(val.startX, p.getLocation().getBlockX(), val.endX,  p.getLocation().getBlockX());
                //Z coords check
                claimZ = validClaim(val.startZ,  p.getLocation().getBlockZ(), val.endZ, p.getLocation().getBlockZ());
                // Enemy check
                if (!claimX && !claimZ) {
                    if (faction == MeineFaction.factionid) return ChatColor.GREEN+MeineFaction.factioname;
                    return ChatColor.RED+Main.factionToname.get(thisFaction.getKey());
                }
            }
        }
        return ChatColor.DARK_GREEN+"Wilderness";
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

}
