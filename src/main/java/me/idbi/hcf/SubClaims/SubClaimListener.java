package me.idbi.hcf.SubClaims;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Attachable;
import org.bukkit.material.Directional;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class SubClaimListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen (PlayerInteractEvent e) {
        if(e.isCancelled()) return;
        if (!e.hasBlock())
            return;

        Block clickedBlock = e.getClickedBlock();

        if (clickedBlock.getType() != Material.CHEST)
            return;

        /*if(SubClaimTool.isDoubleChest(e.getClickedBlock())) {
            SubClaimTool.hasSubClaim()
        }*/
        if(SubClaimTool.hasSubClaim(e.getClickedBlock())) {
            Sign sign = SubClaimTool.getSign(e.getClickedBlock());
            if(sign == null) {
                sign = SubClaimTool.getSign(SubClaimTool.getDoubleChest(e.getClickedBlock()));
                if(sign == null) return;
            }
            String signRankName = sign.getLine(1);
            if(!SubClaimTool.hasAccess(e.getPlayer(), signRankName)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Messages.subclaim_no_access.language(e.getPlayer()).queue());
            }
        }/*

        Block chest = e.getClickedBlock();
        Directional signData = (Directional) chest.getState().getData();
        BlockFace frontDirection = signData.getFacing();

        Block inFront = chest.getRelative(frontDirection);
        if(inFront.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) inFront.getState();
            if(SubClaimTool.isSubClaim(sign)) {
                String signRankName = sign.getLine(1);
                if (!SubClaimTool.hasAccess(e.getPlayer(), signRankName)) {
                    e.getPlayer().sendMessage(Messages.no_permission.language(e.getPlayer()).queue());
                    e.setCancelled(true);
                }
            }
        }*/
    }
}
