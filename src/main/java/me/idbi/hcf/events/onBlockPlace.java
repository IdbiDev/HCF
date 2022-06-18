package me.idbi.hcf.events;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
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

public class onBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (HCF_Claiming.checkEnemyClaimAction(block.getX(), block.getZ(), Integer.parseInt(playertools.getMetadata(p, "factionid")))) {
            p.sendMessage(Messages.YOU_CANT_DO.setFaction(HCF_Claiming.sendFactionTerretoryByXZ(block.getX(), block.getZ())).queue());
            e.setCancelled(true);
            return;
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