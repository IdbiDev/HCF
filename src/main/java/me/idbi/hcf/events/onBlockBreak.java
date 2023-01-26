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
import org.bukkit.event.block.BlockBreakEvent;

import static me.idbi.hcf.Main.WARZONE_SIZE;

public class onBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        int bx = Math.round(block.getX());
        int bz = Math.round(block.getZ());
        if (HCF_Claiming.checkEnemyClaimAction(bx, bz, playertools.getPlayerFaction(p)) && !Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty"))) {
            //p.sendMessage(Main.servername+"§4Ezt nem teheted meg itt!");
            p.sendMessage(Messages.YOU_CANT_DO.setFaction(HCF_Claiming.sendFactionTerretoryByXZ(bx, bz)).queue());
            e.setCancelled(true);
            return;
        }
        if (playertools.getDistanceBetweenPoints2D(new HCF_Claiming.Point(block.getX(),block.getZ()),
                new HCF_Claiming.Point(playertools.getSpawn().getBlockX(),
                        playertools.getSpawn().getBlockZ())) == WARZONE_SIZE && !Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty"))) {
            //p.sendMessage(Main.servername+"§4Ezt nem teheted meg itt!");
            p.sendMessage(Messages.WARZONE_NO_PERMISSION.queue());
            e.setCancelled(true);
            return;
        }
        boolean shouldCancel = false;
        if(Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty"))) {
            shouldCancel = false;
        }
        if(HCF_Claiming.checkEnemyClaimAction(bx, bz, f)) {
            shouldCancel = true;
        }
        if(f.HaveAllyPermission(baszogatottFaction, Permissions.BREAKBLOCK)){
            shouldCancel = false;
        }

        //if (HCF_Claiming.checkEnemyClaimAction(bx, bz, f) && !Boolean.parseBoolean(playertools.getMetadata(p, "adminDuty")) && !f.HaveAllyPermission(baszogatottFaction, Permissions.BREAKBLOCK)) {
        if(shouldCancel){
            p.sendMessage(Messages.you_cant_do.language(p).replace("%faction%", HCF_Claiming.sendFactionTerretoryByXZ(bx, bz)).queue());
            e.setCancelled(true);
            return;
        }
        if (block.getType() == Material.BREWING_STAND) {
            BrewingStand stand = (BrewingStand) block.getState();
            brewing.brewingStands.remove(stand);
        }
        if (block.getType() == Material.FURNACE) {
            Furnace stand = (Furnace) block.getState();
            brewing.furnaces.remove(stand);

        }

    }
}
