package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Miner;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFRules;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Permissions;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SpeedModifiers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        int bx = Math.round(block.getX());
        int bz = Math.round(block.getZ());
        Claiming.Faction_Claim claim = Claiming.sendClaimByXZ(block.getWorld(), bx, bz);
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (claim != null) {
            if (claim.getFaction() == null)
                return;
            Faction baszogatottFaction = claim.getFaction();

            if (hcfPlayer.isInDuty()) {
                e.setCancelled(false);
            } else if (Claiming.isEnemyClaim(p, claim)) {
                e.setCancelled(true);
                Faction f = Playertools.getPlayerFaction(p);
                if (f != null) {
                    if (!f.hasAllyPermission(baszogatottFaction, Permissions.BREAK_BLOCK)) {
                        e.setCancelled(true);
                        p.sendMessage(Messages.you_cant_do.language(p).setFaction(Claiming.sendFactionTerretoryByXZ(p, bx, bz)).queue());
                    } else if (HCFRules.getRules().isAlly(e.getBlock())) {
                        if (!f.hasAllyPermission(baszogatottFaction, Permissions.INVENTORY_ACCESS)) {
                            e.setCancelled(true);
                            p.sendMessage(Messages.you_cant_do.language(p).setFaction(Claiming.sendFactionTerretoryByXZ(p, bx, bz)).queue());
                        } else {
                            e.setCancelled(false);
                        }
                    } else {
                        e.setCancelled(false);
                    }
                } else {
                    e.setCancelled(false);
                }
            } else {}
                // System.out.println("ODA KELL asd MÁ AFDőpédfklőpsdkfglpdfkgpldfkglőéfgkőlpdgl,");
        }
        if (block.getType() == Material.BREWING_STAND) {
            BrewingStand stand = (BrewingStand) block.getState();
            SpeedModifiers.brewingStands.remove(stand);
        }
        if (block.getType() == Material.FURNACE) {
            Furnace stand = (Furnace) block.getState();
            SpeedModifiers.furnaces.remove(stand);

        }

        if(e.getBlock().getType() == Material.DIAMOND_ORE) {
            if(e.isCancelled()) return;
            if (hcfPlayer.getPlayerClass() == Classes.MINER) {
                int foundDiamonds = Miner.diamonds.get(e.getPlayer()) + 1;
                Miner.diamonds.put(e.getPlayer(), foundDiamonds);
            }
        }
    }
}
