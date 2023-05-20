package me.idbi.hcf.SubClaims;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Claim;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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
            Claim c = Claiming.sendClaimByXZ(e.getPlayer().getWorld(),e.getClickedBlock().getX(),e.getClickedBlock().getY(),e.getClickedBlock().getZ());
            if(!SubClaimTool.hasAccess(e.getPlayer(), signRankName,c.getFaction())) {
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
