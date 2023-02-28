package me.idbi.hcf.Events.Claim;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class SpawnClaim implements Listener {
    @EventHandler
    public void Spawn_Claim(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(e.getItem() == null) return;
        if (e.getItem().isSimilar(HCF_Claiming.Wands.claimWand())) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.SPAWN) {
            p.sendMessage(Messages.claim_pos_end.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            HCF_Claiming.setEndPosition(1, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            e.setCancelled(true);
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.SPAWN) {
            p.sendMessage(Messages.claim_pos_start.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            e.setCancelled(true);
            HCF_Claiming.setStartPosition(1, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
        }
        // Elvetés
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking() && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.SPAWN) {
            Playertools.cancelSpawnClaim(p);
        }
        // Elfogadás
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null && e.getPlayer().isSneaking() && player.claimType == HCF_Claiming.ClaimTypes.SPAWN) {
            Playertools.finishSpawn(p);
//                if (HCF_Claiming.FinishClaiming(player.faction.id, e.getPlayer(), HCF_Claiming.ClaimAttributes.NORMAL)) {
//                    e.getPlayer().sendMessage(Messages.faction_claim_accept.language(p).queue());
//                    e.getPlayer().getInventory().remove(e.getItem());
//                } else {
//                    e.getPlayer().sendMessage(Messages.faction_claim_invalid_zone.language(p).queue());
//                }
        }
    }
}