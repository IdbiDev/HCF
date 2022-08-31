package me.idbi.hcf.events;

import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.classes.Bard;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class onPlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(e.getItem() != null){
                if(e.getItem().getType().equals(Material.ENDER_PEARL)){
                    if(HCF_Timer.checkEpTimer(p))
                        e.setCancelled(true);
                    else
                        HCF_Timer.addEpTimer(p);
                }
            }
            if (HCF_Claiming.checkEnemyClaimAction(e.getClickedBlock().getX(), e.getClickedBlock().getZ(), Integer.parseInt(playertools.getMetadata(p, "factionid")))) {
                if (HCF_Rules.blacklistedBlocks.contains(e.getClickedBlock().getType())) {
                    e.setCancelled(true);
                    p.sendMessage(Messages.NO_PERMISSION.queue());
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getItem() != null) {
                if (e.getItem().getType().equals(Material.ENDER_PEARL)) {
                    if (HCF_Timer.checkEpTimer(p))
                        e.setCancelled(true);
                    else
                        HCF_Timer.addEpTimer(p);
                }
            }
        }
        ///Bard
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null) {
            if (playertools.getMetadata(e.getPlayer(), "class").equalsIgnoreCase("bard")) {
                Bard.OhLetsBreakItDown(e.getPlayer());
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null) {
            if (playertools.getMetadata(e.getPlayer(), "class").equalsIgnoreCase("bard")) {
                Bard.OhLetsBreakItDown(e.getPlayer());
            }
        }



        //SPAWN CLAIM

        // Magic wand UwU [[Jobb klikk]]


    }
}

