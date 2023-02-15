package me.idbi.hcf.events.claim;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.Objects.HCFPlayer;
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
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.SPAWN) {
            if (e.getItem().getType() != Material.GOLD_HOE) return;
            if(!e.getItem().hasItemMeta()) return;
            if (e.getItem().getItemMeta().hasLore()) {
                p.sendMessage(Messages.claim_pos_end.language(p).setLoc(e.getClickedBlock().getX(),e.getClickedBlock().getZ()).queue());
                HCF_Claiming.setEndPosition(1, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
                e.setCancelled(true);
            }
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.SPAWN) {
            if (e.getItem().getType() != Material.GOLD_HOE) return;
            if(!e.getItem().hasItemMeta()) return;
            if (e.getItem().getItemMeta().hasLore()) {
                p.sendMessage(Messages.claim_pos_start.language(p).setLoc(e.getClickedBlock().getX(),e.getClickedBlock().getZ()).queue());
                e.setCancelled(true);
                HCF_Claiming.setStartPosition(1, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            }
        }
    }
}
