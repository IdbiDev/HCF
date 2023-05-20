package me.idbi.hcf.Events.Claim;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.ClaimAttributes;
import me.idbi.hcf.Tools.Objects.ClaimTypes;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Wand;
import me.idbi.hcf.Tools.TowerTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.idbi.hcf.Tools.Claiming.endpositions;
import static me.idbi.hcf.Tools.Claiming.startpositions;

public class ProtectedClaim implements Listener {

    @EventHandler
    public void Protected_Claim(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(e.getItem() == null) return;
        if(player.getClaimType() != ClaimTypes.PROTECTED) return;
        if (!e.getItem().isSimilar(Wand.claimWand())) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            TowerTools.removePillar(p, endpositions.get(player.getClaimID()));
            p.sendMessage(Messages.claim_pos_end.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            Claiming.setEndPosition(player.getClaimID(), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            e.setCancelled(true);
            TowerTools.createPillar(p, endpositions.get(player.getClaimID()));
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            TowerTools.removePillar(p, startpositions.get(player.getClaimID()));
            p.sendMessage(Messages.claim_pos_start.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            e.setCancelled(true);
            Claiming.setStartPosition(player.getClaimID(), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            TowerTools.createPillar(p, startpositions.get(player.getClaimID()));
        }
        // Elvetés
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking()) {
            TowerTools.removePillar(p,Claiming.startpositions.get(player.getClaimID()));
            TowerTools.removePillar(p,Claiming.endpositions.get(player.getClaimID()));
            player.setClaimType(ClaimTypes.NONE);
            endpositions.remove(player.getClaimID());
            startpositions.remove(player.getClaimID());
            player.sendMessage(Messages.faction_claim_decline);
            p.getInventory().remove(e.getItem());
            player.setClaimID(0);
        }
        // Elfogadás
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().isSneaking()) {
            Claiming.ForceFinishClaim(player.getClaimID(), p, ClaimAttributes.PROTECTED);
            TowerTools.removePillar(p,Claiming.startpositions.get(player.getClaimID()));
            TowerTools.removePillar(p,Claiming.endpositions.get(player.getClaimID()));
            player.setClaimType(ClaimTypes.NONE);
            endpositions.remove(player.getClaimID());
            startpositions.remove(player.getClaimID());
            player.sendMessage(Messages.faction_claim_accept);
            p.getInventory().remove(e.getItem());
            player.setClaimID(0);
//                if (HCF_Claiming.FinishClaiming(player.faction.id, e.getPlayer(), HCF_Claiming.ClaimAttributes.NORMAL)) {
//                    e.getPlayer().sendMessage(Messages.faction_claim_accept.language(p).queue());
//                    e.getPlayer().getInventory().remove(e.getItem());
//                } else {
//                    e.getPlayer().sendMessage(Messages.faction_claim_invalid_zone.language(p).queue());
//                }
        }
    }
}
