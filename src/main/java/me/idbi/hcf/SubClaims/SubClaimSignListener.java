package me.idbi.hcf.SubClaims;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Directional;

import java.util.Objects;

public class SubClaimSignListener implements Listener {

    @EventHandler
    public void onCreateElevator(SignChangeEvent e) {
        String line0 = e.getLine(0);
        String line1 = e.getLine(1);

        if(!Objects.equals(e.getLine(2), "") || !(Objects.equals(e.getLine(3), ""))) return;

        HCFPlayer player = HCFPlayer.getPlayer(e.getPlayer());
        Faction faction = player.getFaction();
        if(faction == null) return;
        if(player.getCurrentArea() == null) return;
        boolean onOwnClaim = player.getCurrentArea().getFaction() == faction;
        if(!onOwnClaim) return;
        String configValue = ChatColor.stripColor(Config.SubClaimTitle.asStr());
        if (line0 != null) {
            if (line0.equalsIgnoreCase(configValue) || ChatColor.stripColor(line0.replace("&", "§")).equalsIgnoreCase(configValue)) {
                if(e.getBlock() == null) return;
                if(e.getBlock().getType() == Material.WALL_SIGN) {

                    Block currentSign = e.getBlock();
                    Directional signData = (Directional) currentSign.getState().getData();
                    BlockFace frontDirection = signData.getFacing();

                    Block inFront = currentSign.getRelative(frontDirection.getOppositeFace());
                    if (inFront.getType() != Material.CHEST) {
                        e.getPlayer().sendMessage(Messages.subclaim_cant_create.language(e.getPlayer()).queue());
                        e.setCancelled(true);
                        return;
                    } else {
                        if(SubClaimTool.hasSubClaim(inFront)) {
                            e.getPlayer().sendMessage(Messages.subclaim_cant_create.language(e.getPlayer()).queue());
                            e.setCancelled(true);
                            return;
                        }
                    }
                } else {
                    e.setCancelled(true);
                    return;
                }


                e.setLine(0, Config.SubClaimTitle.asStr());
                if(line1 != null && !(line1=="")) {
                    FactionRankManager.Rank signRank = faction.getRank(line1);
                    if(signRank.getPriority() <= player.getRank().getPriority()) {
                        e.setLine(1, signRank.getName());
                    } else {
                        e.getPlayer().sendMessage(Messages.subclaim_lower_rank.language(e.getPlayer()).queue());
                        e.setCancelled(true);
                        return;
                    }
                } else {
                    e.setLine(1, player.getRank().getName());
                }
                player.sendMessage(Messages.subclaim_created);
            }
        }
    }
}

