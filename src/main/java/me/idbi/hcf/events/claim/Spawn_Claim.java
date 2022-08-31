package me.idbi.hcf.events.claim;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class Spawn_Claim implements Listener {
    @EventHandler
    public void Spawn_Claim(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && Boolean.parseBoolean(playertools.getMetadata(p, "spawnclaiming"))) {
            if (e.getItem().getType() != Material.GOLD_HOE) return;
            if (e.getItem().getItemMeta().hasLore()) {
                p.sendMessage(Messages.CLAIM_POS_END.repLoc(e.getClickedBlock().getX(),e.getClickedBlock().getZ()).queue());
                HCF_Claiming.setEndPosition(1, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
                e.setCancelled(true);
            }
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getItem() != null && Boolean.parseBoolean(playertools.getMetadata(p, "spawnclaiming"))) {
            if (e.getItem().getType() != Material.GOLD_HOE) return;
            if (e.getItem().getItemMeta().hasLore()) {
                p.sendMessage(Messages.CLAIM_POS_START.repLoc(e.getClickedBlock().getX(),e.getClickedBlock().getZ()).queue());
                e.setCancelled(true);
                HCF_Claiming.setStartPosition(1, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            }
        }
    }
}
