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

public class Basic_Claim implements Listener {
    @EventHandler
    public void Basic_Claim(PlayerInteractEvent e){
        Player p = e.getPlayer();
        //SIMA CLAIM
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && !Boolean.parseBoolean(playertools.getMetadata(p, "spawnclaiming"))) {
            if (e.getItem().getType() != Material.DIAMOND_HOE) return;
            if (e.getItem().getItemMeta().hasLore()) {

                HCF_Claiming.setEndPosition(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
                e.setCancelled(true);
                p.sendMessage(Messages.CLAIM_POS_END.repLoc(e.getClickedBlock().getX(),e.getClickedBlock().getZ()).queue());

                if (HCF_Claiming.calcMoneyOfArea(e.getPlayer()) != -1) {

                    e.getPlayer().sendMessage(Messages.FACTION_CLAIM_PRICE
                            .setPrice(HCF_Claiming.calcMoneyOfArea(e.getPlayer()))
                            .queue()
                            .replace("%blocks%", String.valueOf(HCF_Claiming.calcBlocks(e.getPlayer())))
                    );
                    //e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: $"+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
                }
            }
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getItem() != null && !Boolean.parseBoolean(playertools.getMetadata(p, "spawnclaiming"))) {
            if (e.getItem().getType() != Material.DIAMOND_HOE) return;
            if (e.getItem().getItemMeta().hasLore()) {

                e.setCancelled(true);
                HCF_Claiming.setStartPosition(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")), e.getClickedBlock().getX(), e.getClickedBlock().getZ());
                p.sendMessage(Messages.CLAIM_POS_START.repLoc(e.getClickedBlock().getX(),e.getClickedBlock().getZ()).queue());
                if (HCF_Claiming.calcMoneyOfArea(e.getPlayer()) != -1) {

                    e.getPlayer().sendMessage(Messages.FACTION_CLAIM_PRICE
                            .setPrice(HCF_Claiming.calcMoneyOfArea(e.getPlayer()))
                            .queue()
                            .replace("%blocks%", String.valueOf(HCF_Claiming.calcBlocks(e.getPlayer())))
                    );

                    //e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: "+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
                }
            }
        }
        // Elvetés
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking() && e.getItem() != null && !Boolean.parseBoolean(playertools.getMetadata(p, "spawnclaiming"))) {
            if (e.getItem().getType() != Material.DIAMOND_HOE) return;
            if (e.getItem().getItemMeta().hasLore()) {
                HCF_Claiming.removeClaiming(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")));
                e.getPlayer().getInventory().remove(e.getItem());
                e.getPlayer().sendMessage(Messages.FACTION_CLAIM_DECLINE.queue());
            }
        }
        // Elfogadás
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem() != null && e.getPlayer().isSneaking() && !Boolean.parseBoolean(playertools.getMetadata(p, "spawnclaiming"))) {
            if (e.getItem().getType() != Material.DIAMOND_HOE) return;
            if (e.getItem().getItemMeta().hasLore()) {
                if (HCF_Claiming.FinishClaiming(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")),e.getPlayer(), HCF_Claiming.ClaimAttributes.NORMAL)) {
                    e.getPlayer().sendMessage(Messages.FACTION_CLAIM_ACCEPT.queue());
                    e.getPlayer().getInventory().remove(e.getItem());
                } else {
                    e.getPlayer().sendMessage(Messages.FACTION_CLAIM_INVALID_ZONE.queue());
                }
            }
        }
    }
}
