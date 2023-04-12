package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Miner;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
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
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        int bx = Math.round(block.getX());
        int bz = Math.round(block.getZ());
        HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(bx, bz);
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (claim != null) {
            if (claim.getFaction() == null) {
                Bukkit.getLogger().severe("Töröld az SQLTTT BUZIAA AIGFAKEJAFTZHEAFF!!!!444 1000%% véssszély!!! aesteregg");
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin(Main.class));
                return;
            }
            Faction baszogatottFaction = claim.getFaction();

            if (hcfPlayer.isInDuty()) {
                // System.out.println("BUZ");
                e.setCancelled(false);
            } else if (HCF_Claiming.isEnemyClaim(p, claim)) {
                // System.out.println("MAU");
                e.setCancelled(true);
                Faction f = Playertools.getPlayerFaction(p);
                if (f != null) {
                    if (!f.hasAllyPermission(baszogatottFaction, Permissions.BREAK_BLOCK)) {

                        // System.out.println("uwuwuwwuuwuuwuuwuw");
                        e.setCancelled(true);
                        p.sendMessage(Messages.you_cant_do.language(p).setFaction(HCF_Claiming.sendFactionTerretoryByXZ(p, bx, bz)).queue());
                    } else if (HCF_Rules.usableBlacklist.contains(e.getBlock().getType())) {
                        if (!f.hasAllyPermission(baszogatottFaction, Permissions.INVENTORY_ACCESS)) {
                            e.setCancelled(true);
                            p.sendMessage(Messages.you_cant_do.language(p).setFaction(HCF_Claiming.sendFactionTerretoryByXZ(p, bx, bz)).queue());
                        } else {
                            e.setCancelled(false);
                        }
                    } else {
                        e.setCancelled(false);
                    }
                }
            } else {
                // System.out.println("ODA KELL asd MÁ AFDőpédfklőpsdkfglpdfkgpldfkglőéfgkőlpdgl,");
            }
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
