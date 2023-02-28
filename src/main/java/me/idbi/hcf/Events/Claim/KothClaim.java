package me.idbi.hcf.Events.Claim;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.idbi.hcf.Tools.HCF_Claiming.endpositions;
import static me.idbi.hcf.Tools.HCF_Claiming.startpositions;

public class KothClaim implements Listener {

    @EventHandler
    public void Koth_Claim(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(e.getItem() == null) return;
        if (e.getItem().isSimilar(HCF_Claiming.Wands.claimWand())) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.KOTH) {
            Main.sendCmdMessage("This is RIGHT click");
            p.sendMessage(Messages.claim_pos_end.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            HCF_Claiming.setEndPosition(player.kothId, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            e.setCancelled(true);
        } else {
            Main.sendCmdMessage("This is RIGHT click ERROR");
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.KOTH) {
            Main.sendCmdMessage("This is LEFT click");
            p.sendMessage(Messages.claim_pos_start.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            e.setCancelled(true);
            HCF_Claiming.setStartPosition(player.kothId, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
        } else {
            Main.sendCmdMessage("This is LEFT click ERROR");
        }
        // Elvetés
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking() && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.KOTH) {
            player.setClaimType(HCF_Claiming.ClaimTypes.NONE);
            player.setKothId(0);
            endpositions.remove(player.kothId);
            startpositions.remove(player.kothId);
            p.getInventory().remove(Material.IRON_HOE);
        }
        // Elfogadás
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null && e.getPlayer().isSneaking() && player.claimType == HCF_Claiming.ClaimTypes.KOTH) {
            HCF_Claiming.ForceFinishClaim(player.kothId, p, HCF_Claiming.ClaimAttributes.KOTH);
            player.setClaimType(HCF_Claiming.ClaimTypes.NONE);
            player.setKothId(0);
            endpositions.remove(player.kothId);
            startpositions.remove(player.kothId);
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