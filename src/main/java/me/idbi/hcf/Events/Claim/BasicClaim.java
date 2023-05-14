package me.idbi.hcf.Events.Claim;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.TowerTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BasicClaim implements Listener {
    @EventHandler
    public void Basic_Claim(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        //SIMA CLAIM
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (e.getItem() == null) return;
        if(player.getClaimType() != Claiming.ClaimTypes.NORMAL) return;
        if (!e.getItem().isSimilar(Claiming.Wands.claimWand())) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            TowerTools.removePillar(p,Claiming.endpositions.get(player.getFaction().getId()));
            Claiming.setEndPosition(player.getFaction().getId(), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            TowerTools.createPillar(p,Claiming.endpositions.get(player.getFaction().getId()));
            e.setCancelled(true);
            p.sendMessage(Messages.claim_pos_end.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());

            if (Claiming.calcMoneyOfArea(e.getPlayer()) != -1) {

                e.getPlayer().sendMessage(Messages.faction_claim_price
                        .language(p)
                        .setPrice(Claiming.calcMoneyOfArea(e.getPlayer()))
                        .replace("%blocks%", String.valueOf(Claiming.calcBlocks(e.getPlayer())))
                        .queue()
                );
                //e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: $"+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
            }
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            e.setCancelled(true);
            TowerTools.removePillar(p,Claiming.startpositions.get(player.getFaction().getId()));
            Claiming.setStartPosition(player.getFaction().getId(), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            TowerTools.createPillar(p,Claiming.startpositions.get(player.getFaction().getId()));
            p.sendMessage(Messages.claim_pos_start.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());

            if (Claiming.calcMoneyOfArea(e.getPlayer()) != -1) {

                e.getPlayer().sendMessage(Messages.faction_claim_price
                        .language(p)
                        .setPrice(Claiming.calcMoneyOfArea(e.getPlayer()))
                        .replace("%blocks%", String.valueOf(Claiming.calcBlocks(e.getPlayer())))
                        .queue()
                );

                //e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: "+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
            }
        }
        // Elvetés
        if(!e.getPlayer().isSneaking()) return;
        if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            TowerTools.removePillar(p,Claiming.startpositions.get(player.getFaction().getId()));
            TowerTools.removePillar(p,Claiming.endpositions.get(player.getFaction().getId()));
            Claiming.removeClaiming(player.getFaction().getId());
            e.getPlayer().getInventory().remove(e.getItem());
            e.getPlayer().sendMessage(Messages.faction_claim_decline.language(p).queue());
        }
        // Elfogadás
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (Claiming.FinishClaiming(player.getFaction().getId(), e.getPlayer(), Claiming.ClaimAttributes.NORMAL)) {
                e.getPlayer().sendMessage(Messages.faction_claim_accept.language(p).queue());
                e.getPlayer().getInventory().remove(e.getItem());
                TowerTools.removePillar(p,Claiming.startpositions.get(player.getFaction().getId()));
                TowerTools.removePillar(p,Claiming.endpositions.get(player.getFaction().getId()));
            } else {
                e.getPlayer().sendMessage(Messages.faction_claim_invalid_zone.language(p).queue());
            }
        }
    }
}
