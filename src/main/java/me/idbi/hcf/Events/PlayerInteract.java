package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Bard;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFRules;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.*;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
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


public class PlayerInteract implements Listener {
    Bard bard = new Bard();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem() != null) {
                if (e.getItem().getType().equals(Material.ENDER_PEARL)) {
                    if (Timers.ENDER_PEARL.has(p))
                        e.setCancelled(true);
                    else
                        Timers.ENDER_PEARL.add(p);
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            //Claiming.Faction_Claim claim = Claiming.sendClaimByXZ(block.getWorld(), block.getX(), block.getZ());
            Claim claim = Playertools.getUpperClaim(block.getLocation());
            if (claim != null) {
                if (claim.getFaction() == null) {
                    Bukkit.getLogger().severe("Töröld az SQLTTT BUZIAA AIGFAKEJAFTZHEAFF!!!!444 1000%% véssszély!!! aesteregg");
                    Bukkit.getPluginManager().disablePlugin(Main.getPlugin(Main.class));
                }
                Faction baszogatottFaction = claim.getFaction();

                HCFRules rules = HCFRules.getRules();
                Faction f = Playertools.getPlayerFaction(p);
                if (hcfPlayer.isInDuty()) {
                    e.setCancelled(false);
                } else if (f != null) {
                    if (Claiming.isEnemyClaim(p, claim) && !Objects.requireNonNull(f).hasAllyPermission(baszogatottFaction, Permissions.INTERACT)) {
                        if (rules.isBlacklistedBlock(e.getClickedBlock())) {
                            e.setCancelled(true);
                            if(claim.getAttribute() != ClaimAttributes.PROTECTED)
                                p.sendMessage(Messages.you_cant_do.language(p).setFaction(claim.getFaction()).queue());
                        }
                    }
                    if (rules.isAlly(e.getClickedBlock())) {
                        if (Claiming.isEnemyClaim(p, claim) && !Objects.requireNonNull(f).hasAllyPermission(baszogatottFaction, Permissions.INVENTORY_ACCESS)) {
                            e.setCancelled(true);
                            if(claim.getAttribute() != ClaimAttributes.PROTECTED)
                                p.sendMessage(Messages.you_cant_do.language(p).setFaction(claim.getFaction()).queue());
                        }
                    }

                } else {
                    if (Claiming.isEnemyClaim(p, claim)) {
                        e.setCancelled(true);
                        if (rules.isBlacklistedBlock(e.getClickedBlock())) {
                            e.setCancelled(true);
                            if(claim.getAttribute() != ClaimAttributes.PROTECTED)
                                p.sendMessage(Messages.you_cant_do.language(p).setFaction(claim.getFaction()).queue());
                        }
                    }
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getItem() != null) {
                if (e.getItem().getType().equals(Material.ENDER_PEARL)) {
                    if (Timers.ENDER_PEARL.has(p))
                        e.setCancelled(true);
                    else
                        Timers.ENDER_PEARL.add(p);
                }
            }
        }
        ///Bard
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && e.getItem() != null) {
            if (player.getPlayerClass() == Classes.BARD) {
                bard.OhLetsBreakItDown(e.getPlayer());
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null) {
            if (e.getItem().getType().equals(Material.DIAMOND_AXE)) {
                TeleportBehindPlayer(e.getPlayer());
            }

        }
        if(hcfPlayer.isInDuty()) {
            e.setCancelled(false);
        }


        //SPAWN CLAIM

        // Magic wand UwU [[Jobb klikk]]


    }
}

