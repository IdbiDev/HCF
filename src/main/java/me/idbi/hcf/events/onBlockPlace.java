package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.Permissions;
import me.idbi.hcf.tools.brewing;
import me.idbi.hcf.tools.playertools;
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
        Faction f = playertools.getPlayerFaction(p);
        HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(block.getX(),block.getZ());
        if(claim == null) {
            return;
        }
        if(claim.faction == null) {
            Bukkit.getLogger().severe("Töröld az SQLTTT BUZIAA AIGFAKEJAFTZHEAFF!!!!444 1000%% véssszély!!! aesteregg");
            Bukkit.getPluginManager().disablePlugin(Main.getPlugin(Main.class));
        }
        Faction baszogatottFaction = claim.faction;

        if (playertools.getDistanceBetweenPoints2D(new HCF_Claiming.Point(block.getX(),block.getZ()),
                new HCF_Claiming.Point(playertools.getSpawn().getBlockX(),
                        playertools.getSpawn().getBlockZ())) == WARZONE_SIZE && !Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty"))) {
            //p.sendMessage(Main.servername+"§4Ezt nem teheted meg itt!");
            p.sendMessage(Messages.warzone_no_permission.language(p).queue());
            e.setCancelled(true);
            return;
        }
        if(hcf.inDuty) {
            e.setCancelled(false);
        } else if(HCF_Claiming.checkEnemyClaimAction(block.getX(), block.getZ(), baszogatottFaction)) {
            Faction f = playertools.getPlayerFaction(p);
            if(f == null) return;
            e.setCancelled(true);
            if(!f.HaveAllyPermission(baszogatottFaction,Permissions.BREAKBLOCK)) {
                e.setCancelled(true);
                p.sendMessage(Messages.you_cant_do.language(p).replace("%faction%", HCF_Claiming.sendFactionTerretoryByXZ(p, block.getX(), block.getZ())).queue());
            } else {
                e.setCancelled(false);
            }

        }

        if (block.getType() == Material.BREWING_STAND) {
            BrewingStand stand = (BrewingStand) block.getState();
            brewing.brewingStands.add(stand);
        }
        if (block.getType() == Material.FURNACE) {
            Furnace stand = (Furnace) block.getState();
            brewing.furnaces.add(stand);
        }

    }
}