package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Tools.HCF_Claiming;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockToBlock implements Listener {
    @EventHandler
    public void BlockToBlockListener(BlockFromToEvent e) {
        Location to = e.getToBlock().getLocation();
        if(HCF_Claiming.isClaimBorder(to.getBlockX(),to.getBlockZ()) && !Config.EnableFlowingToClaim.asBoolean()){
            e.setCancelled(true);
        }
    }
}
