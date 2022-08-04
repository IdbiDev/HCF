package me.idbi.hcf.koth;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class KOTH implements Listener {

    public static koth_area GLOBAL_AREA = null;
    public static Player GLOBAL_PLAYER  = null;
    public static int GLOBAL_TIME;
    @EventHandler(priority = EventPriority.LOWEST)
    public static void KothMoveEvent(PlayerMoveEvent e){
        //Ha van aktív koth
        if(GLOBAL_AREA != null){
            HCF_Claiming.Point start = new HCF_Claiming.Point(GLOBAL_AREA.claim.startX,GLOBAL_AREA.claim.startZ);
            HCF_Claiming.Point end = new HCF_Claiming.Point(GLOBAL_AREA.claim.endX,GLOBAL_AREA.claim.endZ);
            HCF_Claiming.Point point = new HCF_Claiming.Point(e.getTo().getBlockX(),e.getTo().getBlockZ());
            if(GLOBAL_PLAYER == null){
                // ha kothba ment ÉS nem foglalja senki
                if(GLOBAL_AREA.claim.attribute.equalsIgnoreCase("koth") && HCF_Claiming.doOverlap(start,end,point,point)){
                    GLOBAL_PLAYER = e.getPlayer();
                }
            }else{
                if(GLOBAL_AREA.claim.attribute.equalsIgnoreCase("koth") && !HCF_Claiming.doOverlap(start,end,point,point)){
                    if(GLOBAL_PLAYER == e.getPlayer()){
                        GLOBAL_PLAYER = null;
                    }
                }
            }

        }
    }
    //Todo: VAlahogy kothot csinálni XDDDDD
    public static class koth_area{
        HCF_Claiming.Faction_Claim claim;
        Main.Faction faction;
        public koth_area(String name,HCF_Claiming.Point start, HCF_Claiming.Point end){
            faction = Main.faction_cache.get(playertools.createCustomFaction(name));
            claim = new HCF_Claiming.Faction_Claim(start.x,end.x,start.z,end.z,faction.factionid,"koth");
        }
    }
}
