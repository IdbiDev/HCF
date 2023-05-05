package me.idbi.hcf.Events.Claim;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.idbi.hcf.Tools.Claiming.endpositions;
import static me.idbi.hcf.Tools.Claiming.startpositions;

public class KothClaim implements Listener {

    @EventHandler
    public void Koth_Claim(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(e.getItem() == null) return;
        if(player.getClaimType() != Claiming.ClaimTypes.KOTH) return;
        if (!e.getItem().isSimilar(Claiming.Wands.claimWand())) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            p.sendMessage(Messages.claim_pos_end.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            Claiming.setEndPosition(player.getClaimID(), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            e.setCancelled(true);
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            p.sendMessage(Messages.claim_pos_start.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            e.setCancelled(true);
            Claiming.setStartPosition(player.getClaimID(), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
        }
        // Elvetés
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking()) {
            player.setClaimType(Claiming.ClaimTypes.NONE);
            player.setClaimID(0);
            endpositions.remove(player.getClaimID());
            startpositions.remove(player.getClaimID());
            p.getInventory().remove(Material.IRON_HOE);
        }
        // Elfogadás
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().isSneaking()) {
            Claiming.ForceFinishClaim(player.getClaimID(), p, Claiming.ClaimAttributes.KOTH);
            player.setClaimType(Claiming.ClaimTypes.NONE);
            player.setClaimID(0);
            endpositions.remove(player.getClaimID());
            startpositions.remove(player.getClaimID());
            p.getInventory().remove(Material.IRON_HOE);
//                if (HCF_Claiming.FinishClaiming(player.faction.id, e.getPlayer(), HCF_Claiming.ClaimAttributes.NORMAL)) {
//                    e.getPlayer().sendMessage(Messages.faction_claim_accept.language(p).queue());
//                    e.getPlayer().getInventory().remove(e.getItem());
//                } else {
//                    e.getPlayer().sendMessage(Messages.faction_claim_invalid_zone.language(p).queue());
//                }
        }
    }
}
