package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class onBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block block = e.getBlock();
        int bx = Math.round(block.getX());
        int bz = Math.round(block.getZ());
        if(HCF_Claiming.checkEnemyClaimAction(bx,bz ,Integer.parseInt( playertools.getMetadata(p,"factionid")))  && playertools.getDTR(Integer.parseInt( playertools.getMetadata(p,"factionid"))) > 0){
            //p.sendMessage(Main.servername+"§4Ezt nem teheted meg itt!");
            p.sendMessage(Messages.YOU_CANT_DO.setFaction(HCF_Claiming.sendFactionTerretoryByXZ(bx,bz)).queue());
            e.setCancelled(true);
        }

    }
}
