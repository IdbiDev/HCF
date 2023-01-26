package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.classes.subClasses.Bard;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.Permissions;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.idbi.hcf.classes.subClasses.Assassin.TeleportBehindPlayer;


public class onPlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem() != null) {
                if (e.getItem().getType().equals(Material.ENDER_PEARL)) {
                    if (HCF_Timer.checkEpTimer(p))
                        e.setCancelled(true);
                    else
                        HCF_Timer.addEpTimer(p);
                }
            }
        }
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            Block block = e.getClickedBlock();
            Faction f = playertools.getPlayerFaction(p);
            HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(block.getX(),block.getZ());
            Faction baszogatottFaction;
            if(claim != null){
                baszogatottFaction = Main.faction_cache.get(claim.faction);
            }else{
                baszogatottFaction = Main.faction_cache.get(1);
            }
            boolean shouldCancel = false;
            if(Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty"))) {
                shouldCancel = false;
            }
            if(HCF_Claiming.checkEnemyClaimAction(block.getX(),block.getZ(), f)) {
                shouldCancel = true;
            }
            if(f.HaveAllyPermission(baszogatottFaction, Permissions.USEBLOCK)){
                shouldCancel = false;
            }
            if (shouldCancel) {
                if (HCF_Rules.blacklistedBlocks.contains(e.getClickedBlock().getType())) {
                    e.setCancelled(true);
                    p.sendMessage(Messages.no_permission.language(p).queue());
                }
            }else{
                if (HCF_Rules.usableBlacklist.contains(e.getClickedBlock().getType())) {
                    if(!f.HaveAllyPermission(baszogatottFaction, Permissions.VIEWITEMS)){
                        e.setCancelled(true);
                        p.sendMessage(Messages.no_permission.language(p).queue());
                    }
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
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null) {
            if (e.getItem().getType().equals(Material.DIAMOND_AXE)) {
                TeleportBehindPlayer(e.getPlayer());
            }

        }



        //SPAWN CLAIM

        // Magic wand UwU [[Jobb klikk]]


    }
}

