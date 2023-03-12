package me.idbi.hcf.SubClaims;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Directional;

public class SubClaimSignListener implements Listener {

    @EventHandler
    public void onCreateElevator(SignChangeEvent e) {
        String line0 = e.getLine(0);
        String line1 = e.getLine(1);

        if(!e.getLine(2).isBlank() || !e.getLine(3).isBlank()) return;

        HCFPlayer player = HCFPlayer.getPlayer(e.getPlayer());
        Faction faction = player.faction;
        if(faction == null) return;
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
                if(line1 != null && !line1.isBlank()) {
                    FactionRankManager.Rank signRank = faction.getRank(line1);
                    if(signRank.priority <= player.rank.priority) {
                        e.setLine(1, signRank.name);
                    } else {
                        e.getPlayer().sendMessage(Messages.subclaim_lower_rank.language(e.getPlayer()).queue());
                        e.setCancelled(true);
                    }
                } else {
                    e.setLine(1, player.rank.name);
                }
            }
        }
    }
}

