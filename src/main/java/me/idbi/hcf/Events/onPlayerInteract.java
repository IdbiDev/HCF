package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Bard;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Permissions;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

import static me.idbi.hcf.Classes.SubClasses.Assassin.TeleportBehindPlayer;


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
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(block.getX(), block.getZ());
            if (claim == null) {
                return;
            }
            if (claim.faction == null) {
                Bukkit.getLogger().severe("Töröld az SQLTTT BUZIAA AIGFAKEJAFTZHEAFF!!!!444 1000%% véssszély!!! aesteregg");
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin(Main.class));
            }
            Faction baszogatottFaction = claim.faction;
            //if(Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty"))) {
//                shouldCancel = false;
//            }
//
            Faction f = Playertools.getPlayerFaction(p);
            HCFPlayer hcf = HCFPlayer.getPlayer(p);
            if (hcf.inDuty) {
                e.setCancelled(false);
                return;
            }
            if (f != null) {
                if (HCF_Claiming.isEnemyClaim(p, claim) && !Objects.requireNonNull(f).HaveAllyPermission(baszogatottFaction, Permissions.INTERACT)) {
                    if (HCF_Rules.blacklistedBlocks.contains(e.getClickedBlock().getType())) {
                        e.setCancelled(true);
                        p.sendMessage(Messages.no_permission.language(p).queue());
                    }
                }
                if (HCF_Rules.usableBlacklist.contains(e.getClickedBlock().getType())) {
                    if (HCF_Claiming.isEnemyClaim(p, claim) && !Objects.requireNonNull(f).HaveAllyPermission(baszogatottFaction, Permissions.INVENTORY_ACCESS)) {
                        e.setCancelled(true);
                        p.sendMessage(Messages.no_permission.language(p).queue());
                    }
                }

            } else {
                if (HCF_Claiming.isEnemyClaim(p, claim)) {
                    e.setCancelled(true);
                    if (HCF_Rules.blacklistedBlocks.contains(e.getClickedBlock().getType())) {
                        e.setCancelled(true);
                        p.sendMessage(Messages.no_permission.language(p).queue());
                    }
                }
                /*else{
                if (HCF_Rules.usableBlacklist.contains(e.getClickedBlock().getType())) {
                    if(!Objects.requireNonNull(f).HaveAllyPermission(baszogatottFaction, Permissions.VIEWITEMS)){
                        e.setCancelled(true);
                        p.sendMessage(Messages.no_permission.language(p).queue());
                    }
                }
            }*/
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
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null) {
            if (player.playerClass == Classes.BARD) {
                Bard.OhLetsBreakItDown(e.getPlayer());
            }
        }
        else if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null) {
            if (player.playerClass == Classes.BARD) {
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

