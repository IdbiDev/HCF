package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.ClaimTypes;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Wand;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FactionClaimCommand extends SubCommand {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Gives a claiming wand, and enables the claiming pillars.";
    }

    @Override
    public String getSyntax() {
        return "/faction claim";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (Playertools.getPlayerFaction(p) != null) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            if(Config.MaxClaims.asInt() < hcfPlayer.getFaction().getClaims().size()+1) {
                p.sendMessage(Messages.faction_reached_max_claims.language(p).queue());
                return;
            }
            if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().setItem(p.getInventory().firstEmpty(), Wand.claimWand());
                for(String l : Messages.claim_info.queueList())
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', l));
                //ListMessages.CLAIM_INFO.queue().forEach(p::sendMessage);
                hcfPlayer.setClaimType(ClaimTypes.NORMAL);
                addCooldown(p);
            } else {
                p.sendMessage(Messages.not_enough_slot.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());

        }
    }
}
