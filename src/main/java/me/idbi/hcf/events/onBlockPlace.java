package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if(HCF_Claiming.checkEnemyClaimAction(block.getX(),block.getZ(),Integer.parseInt( playertools.getMetadata(p,"factionid")))  && playertools.getDTR(Integer.parseInt(playertools.getMetadata(p,"factionid"))) > 0){
            p.sendMessage(Main.servername+"§4Ezt nem teheted meg itt!");
            e.setCancelled(true);
        }

    }
}