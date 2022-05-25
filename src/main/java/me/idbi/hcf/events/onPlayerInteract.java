package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class onPlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(HCF_Claiming.checkEnemyClaimAction(e.getClickedBlock().getX(),e.getClickedBlock().getZ(), Integer.parseInt( playertools.getMetadata(p,"factionid")) )){
                if(Main.blacklistedBlocks.contains(e.getClickedBlock().getType()) && playertools.getDTR(Integer.parseInt( playertools.getMetadata(p,"factionid")) ) > 0){
                    e.setCancelled(true);
                    p.sendMessage(Main.servername+"§4Ezt nem teheted meg itt!");
                }
            }
        }

        // Magic wand UwU [[Jobb klikk]]
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK )&& e.getItem() != null){
            if(e.getItem().getType() != Material.DIAMOND_HOE) return;
            if(e.getItem().getItemMeta().hasLore()){
                HCF_Claiming.setEndPosition(Integer.parseInt( playertools.getMetadata(e.getPlayer(),"factionid")),e.getClickedBlock().getX(),e.getClickedBlock().getZ());
                e.setCancelled(true);
                HCF_Claiming.CreateNewFakeTower(e.getPlayer(),e.getClickedBlock().getLocation());
                if(HCF_Claiming.calcMoneyOfArea(e.getPlayer()) != -1){
                    e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: $"+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
                }
            }
        }
        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)&& e.getItem() != null){
            if(e.getItem().getType() != Material.DIAMOND_HOE) return;
            if(e.getItem().getItemMeta().hasLore()){

                e.setCancelled(true);
                HCF_Claiming.CreateNewFakeTower(e.getPlayer(),e.getClickedBlock().getLocation());
                HCF_Claiming.setStartPosition(Integer.parseInt( playertools.getMetadata(e.getPlayer(),"factionid")),e.getClickedBlock().getX(),e.getClickedBlock().getZ());
                if(HCF_Claiming.calcMoneyOfArea(e.getPlayer()) != -1){
                    e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Fizetendő: "+HCF_Claiming.calcMoneyOfArea(e.getPlayer()));
                }
            }
        }
        // Elvetés
        if(e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking()&& e.getItem() != null){
            if(e.getItem().getType() != Material.DIAMOND_HOE) return;
            if(e.getItem().getItemMeta().hasLore()){
                HCF_Claiming.removeClaiming(Integer.parseInt( playertools.getMetadata(e.getPlayer(),"factionid")));
                e.getPlayer().getInventory().remove(e.getItem());
                HCF_Claiming.DeletFakeTowers(e.getPlayer());
                e.getPlayer().sendMessage(Main.servername+ ChatColor.RED+"Befejezted a claimelést!");
            }
        }
        // Elfogadás
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)&& e.getItem() != null){
            if(e.getItem().getType() != Material.DIAMOND_HOE) return;
            if(e.getItem().getItemMeta().hasLore()){
                if(HCF_Claiming.FinishClaiming(Integer.parseInt( playertools.getMetadata(e.getPlayer(),"factionid")))){
                    e.getPlayer().sendMessage(Main.servername+ ChatColor.GREEN+"Sikeresen claimelted a zónát!");
                    e.getPlayer().getInventory().remove(e.getItem());
                    HCF_Claiming.DeletFakeTowers(e.getPlayer());
                }else {
                    e.getPlayer().sendMessage(Main.servername+ ChatColor.RED+"Érvénytelen claimelési zóna!");
                }
            }
        }
    }
}
