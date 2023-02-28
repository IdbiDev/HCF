package me.idbi.hcf.Events.Claim;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
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
        if (e.getItem().isSimilar(HCF_Claiming.Wands.claimWand())) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.FACTION) {

            HCF_Claiming.setEndPosition(player.faction.id, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            e.setCancelled(true);
            p.sendMessage(Messages.claim_pos_end.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());

                if (HCF_Claiming.calcMoneyOfArea(e.getPlayer()) != -1) {

                    e.getPlayer().sendMessage(Messages.faction_claim_price
                            .language(p)
                            .setPrice(HCF_Claiming.calcMoneyOfArea(e.getPlayer()))
                            .replace("%blocks%", String.valueOf(HCF_Claiming.calcBlocks(e.getPlayer())))
                            .queue()
                    );
                    //e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: $"+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
                }
            }
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.FACTION) {

            e.setCancelled(true);
            HCF_Claiming.setStartPosition(player.faction.id, e.getClickedBlock().getX(), e.getClickedBlock().getZ());
            p.sendMessage(Messages.claim_pos_start.language(p).setLoc(e.getClickedBlock().getX(), e.getClickedBlock().getZ()).queue());
            if (HCF_Claiming.calcMoneyOfArea(e.getPlayer()) != -1) {

                    e.getPlayer().sendMessage(Messages.faction_claim_price
                            .language(p)
                            .setPrice(HCF_Claiming.calcMoneyOfArea(e.getPlayer()))
                            .replace("%blocks%", String.valueOf(HCF_Claiming.calcBlocks(e.getPlayer())))
                            .queue()
                    );

                    //e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: "+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
                }
            }
        }
        // Elvetés
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking() && e.getItem() != null && player.claimType == HCF_Claiming.ClaimTypes.FACTION) {
            HCF_Claiming.removeClaiming(player.faction.id);
            e.getPlayer().getInventory().remove(e.getItem());
            e.getPlayer().sendMessage(Messages.faction_claim_decline.language(p).queue());
        }
        // Elfogadás
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null && e.getPlayer().isSneaking() && player.claimType == HCF_Claiming.ClaimTypes.FACTION) {
            if (HCF_Claiming.FinishClaiming(player.faction.id, e.getPlayer(), HCF_Claiming.ClaimAttributes.NORMAL)) {
                e.getPlayer().sendMessage(Messages.faction_claim_accept.language(p).queue());
                e.getPlayer().getInventory().remove(e.getItem());
            } else {
                e.getPlayer().sendMessage(Messages.faction_claim_invalid_zone.language(p).queue());
            }
        }
    }
}
