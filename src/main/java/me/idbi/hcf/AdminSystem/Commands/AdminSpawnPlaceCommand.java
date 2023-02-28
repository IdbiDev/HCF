package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AdminSpawnPlaceCommand extends SubCommand {
    @Override
    public String getName() {
        return "claimspawn";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName();
    }

    @Override
    public String getPermission() {
        return "factions.command.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (!player.claimType.equals(HCF_Claiming.ClaimTypes.SPAWN)) {
            for (String lines : Messages.claim_info_admin.queueList()) {

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
            }
            player.setClaimType(HCF_Claiming.ClaimTypes.SPAWN);
            HCF_Claiming.SpawnPrepare(p);
        } else {
            if (HCF_Claiming.ForceFinishClaim(1, p, HCF_Claiming.ClaimAttributes.PROTECTED)) {
                //Todo: Kurvva sikerült
                player.setClaimType(HCF_Claiming.ClaimTypes.NONE);
                p.sendMessage(Messages.spawn_claim_success.language(p).queue());
                p.getInventory().remove(HCF_Claiming.Wands.claimWand());
            } else {
                //Todo: nem sikerült, balfasz vagy és nem raktad le
                player.setClaimType(HCF_Claiming.ClaimTypes.NONE);
                p.sendMessage(Messages.faction_claim_invalid_zone.language(p).queue());
                p.getInventory().remove(HCF_Claiming.Wands.claimWand());
            }
        }
    }
}
