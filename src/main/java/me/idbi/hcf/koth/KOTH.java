package me.idbi.hcf.koth;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.koth.GUI.KOTHItemManager;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class KOTH implements Listener {

    public static koth_area GLOBAL_AREA = null;
    public static Player GLOBAL_PLAYER  = null;
    public static int GLOBAL_TIME;
    @EventHandler(priority = EventPriority.LOWEST)
    public static void KothMoveEvent(PlayerMoveEvent e){
        //Ha van aktív koth
        if(!(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()))
            return;
        if(GLOBAL_AREA != null){
            HCF_Claiming.Point start = new HCF_Claiming.Point(GLOBAL_AREA.claim.startX,GLOBAL_AREA.claim.startZ);
            HCF_Claiming.Point end = new HCF_Claiming.Point(GLOBAL_AREA.claim.endX,GLOBAL_AREA.claim.endZ);
            HCF_Claiming.Point point = new HCF_Claiming.Point(e.getTo().getBlockX(),e.getTo().getBlockZ());
            if(GLOBAL_PLAYER == null){
                // ha kothba ment ÉS nem foglalja senki
                if(GLOBAL_AREA.claim.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH) && HCF_Claiming.doOverlap(start,end,point,point)){
                    if(!playertools.getMetadata(e.getPlayer(),"factionid").equals("0")) {
                        GLOBAL_PLAYER = e.getPlayer();


                        Bukkit.broadcastMessage(Messages.KOTH_CAPTURING_STARTED.setFaction(GLOBAL_AREA.faction.name).queue());
                    }
                }
            }else{
                if(GLOBAL_AREA.claim.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH) && !HCF_Claiming.doOverlap(start,end,point,point)){
                    if(GLOBAL_PLAYER == e.getPlayer()){
                        GLOBAL_PLAYER = null;
                        GLOBAL_TIME = Integer.parseInt(ConfigLibrary.KOTH_TIME.getValue()) * 60;
                        Bukkit.broadcastMessage(Messages.KOTH_CAPTURING_ENDED.setFaction(GLOBAL_AREA.faction.name).queue());
                    }
                }
            }

        }
    }
    public static class koth_area {
        HCF_Claiming.Faction_Claim claim;
        public Main.Faction faction;
        public koth_area(Main.Faction f,HCF_Claiming.Point start, HCF_Claiming.Point end){
            faction = f;
            claim = new HCF_Claiming.Faction_Claim(start.x,end.x,start.z,end.z,faction.id, HCF_Claiming.ClaimAttributes.KOTH);
        }
    }

    public static int createKoth(String name) {
        return playertools.createCustomFaction(name,"");
//        KOTH.koth_area temp = new KOTH.koth_area(
//                faction,
//                new HCF_Claiming.Point(claim.startX,claim.startZ),
//                new HCF_Claiming.Point(claim.endX,claim.endZ)
//        );
    }


    public static void startKoth(String f){
        try{
            stopKoth();
            Main.sendCmdMessage("KOTH: "+   Main.koth_cache.get(f).faction.name);
            GLOBAL_AREA = Main.koth_cache.get(f);
            String msg = "§4§n%s§f§4 KOTH STARTED!".replace("%s", Main.koth_cache.get(f).faction.name);
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendTitle(
                        msg,
                        "HCF+ UwU"
                );
                p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL,1f,1f);
            }

        }catch (Exception e){
            Main.sendCmdMessage("Nem található koth ezzel a névvel..");
        }
    }
    public static void StartRandomKoth(){
        if(Main.koth_cache.isEmpty()){
            Main.sendCmdMessage("§4 NO KOTH BITCH");
            return;
        }
        int c = new Random().nextInt(Main.koth_cache.size());
        List<String> names = new ArrayList<>(Main.koth_cache.keySet());
        startKoth(names.get((c == 0 && names.size() > 0) ? c : c - 1));
    }
    public static void stopKoth(){
        GLOBAL_AREA = null;
        //TODO:  Koth Elfoglalta xy
        //TODO: Set the items
        KOTHItemManager.addRewardsToPlayer(GLOBAL_PLAYER);
        GLOBAL_PLAYER  = null;
        GLOBAL_TIME = Integer.parseInt(ConfigLibrary.KOTH_TIME.getValue()) * 60;
        ///Main.autoKoth.StopAutoKoth();
    }

    public static int getKothFromName(String name){
        Main.Faction k = Main.nameToFaction.get(name);
        if(k !=null){
            return k.id;
        }
        return 0;
    }
}
