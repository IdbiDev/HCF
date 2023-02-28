package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
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
import org.bukkit.event.block.BlockPlaceEvent;

import static me.idbi.hcf.Main.WARZONE_SIZE;

public class onBlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(block.getX(), block.getZ());
        if (claim == null) {
            return;
        }
        if (claim.faction == null) {
            Bukkit.getLogger().severe("Töröld az SQLTTT BUZIAA AIGFAKEJAFTZHEAFF!!!!444 1000%% véssszély!!! aesteregg");
            Bukkit.getPluginManager().disablePlugin(Main.getPlugin(Main.class));
        }
        Faction baszogatottFaction = claim.faction;

        if (Playertools.getDistanceBetweenPoints2D(new HCF_Claiming.Point(block.getX(), block.getZ()),
                new HCF_Claiming.Point(Playertools.getSpawn().getBlockX(),
                        Playertools.getSpawn().getBlockZ())) == WARZONE_SIZE && !hcf.inDuty) {

            p.sendMessage(Messages.warzone_no_permission.language(p).queue());
            e.setCancelled(true);
            return;
        }
//        if(hcf.inDuty) {
//            e.setCancelled(false);
//        } else if(HCF_Claiming.checkEnemyClaimAction(block.getX(), block.getZ(), baszogatottFaction)) {
//            Faction f = playertools.getPlayerFaction(p);
//            if(f == null) return;
//            e.setCancelled(true);
//            if(!f.HaveAllyPermission(baszogatottFaction, Permissions.BREAK_BLOCK)) {
//                e.setCancelled(true);
//                p.sendMessage(Messages.you_cant_do.language(p).replace("%faction%", HCF_Claiming.sendFactionTerretoryByXZ(p, block.getX(), block.getZ())).queue());
//            } else {
//                e.setCancelled(false);
//            }
//
//        }
        if (hcf.inDuty) {
            // System.out.println("BUZ");
            e.setCancelled(false);
        } else if (HCF_Claiming.isEnemyClaim(p, claim)) {
            // System.out.println("MAU");
            e.setCancelled(true);
            Faction f = Playertools.getPlayerFaction(p);
            if (f == null) return;
            if (!f.HaveAllyPermission(baszogatottFaction, Permissions.PLACE_BLOCK)) {
                System.out.println("uwuwuwwuuwuuwuuwuw");
                e.setCancelled(true);
                p.sendMessage(Messages.you_cant_do.language(p).replace("%faction%", HCF_Claiming.sendFactionTerretoryByXZ(p, block.getX(), block.getZ())).queue());
            } else {
                e.setCancelled(false);
            }

        } else {
            // System.out.println("ODA KELL asd MÁ AFDőpédfklőpsdkfglpdfkgpldfkglőéfgkőlpdgl,");
        }

        if (block.getType() == Material.BREWING_STAND) {
            BrewingStand stand = (BrewingStand) block.getState();
            SpeedModifiers.brewingStands.add(stand);
        }
        if (block.getType() == Material.FURNACE) {
            Furnace stand = (Furnace) block.getState();
            SpeedModifiers.furnaces.add(stand);
        }

    }
}