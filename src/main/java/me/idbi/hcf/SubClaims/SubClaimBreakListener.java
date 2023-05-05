package me.idbi.hcf.SubClaims;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SubClaimBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        if(e.isCancelled()) return;
        if (e.getBlock().getType() == Material.CHEST) {
            if(SubClaimTool.hasSubClaim(e.getBlock())) {
                Sign sign = SubClaimTool.getSign(e.getBlock());
                if(sign == null) return;
                String signRankName = sign.getLine(1);
                Claiming.Faction_Claim c = Claiming.sendClaimByXZ(e.getPlayer().getWorld(),e.getBlock().getX(),e.getBlock().getZ());
                if(c != null) {
                    if (!SubClaimTool.hasAccess(e.getPlayer(), signRankName,c.getFaction())) {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(Messages.subclaim_no_access.language(e.getPlayer()).queue());
                    }
                }
            }
        } else if(e.getBlock().getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) e.getBlock().getState();
            if(SubClaimTool.isSubClaim(sign)) {
                if(sign.getLine(0).isBlank()) return;
                if(sign.getLine(1).isBlank()) return;
                Claiming.Faction_Claim c = Claiming.sendClaimByXZ(e.getPlayer().getWorld(),e.getBlock().getX(),e.getBlock().getZ());
                if(!SubClaimTool.hasAccess(e.getPlayer(), sign.getLine(1),c.getFaction())) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(Messages.subclaim_no_access.language(e.getPlayer()).queue());
                }
            }
        }
    }
}
