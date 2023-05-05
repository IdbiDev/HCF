package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

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
        return "factions.commands.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (!player.getClaimType().equals(Claiming.ClaimTypes.SPAWN)) {
            for (String lines : Messages.claim_info_admin.queueList()) {

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
            }
            player.setClaimType(Claiming.ClaimTypes.SPAWN);
            Claiming.SpawnPrepare(p);
        } else {
            if (Claiming.ForceFinishClaim(1, p, Claiming.ClaimAttributes.PROTECTED)) {
                //Todo: Kurvva sikerült
                player.setClaimType(Claiming.ClaimTypes.NONE);
                p.sendMessage(Messages.spawn_claim_success.language(p).queue());
                p.getInventory().remove(Claiming.Wands.claimWand());
            } else {
                //Todo: nem sikerült, balfasz vagy és nem raktad le
                player.setClaimType(Claiming.ClaimTypes.NONE);
                p.sendMessage(Messages.faction_claim_invalid_zone.language(p).queue());
                p.getInventory().remove(Claiming.Wands.claimWand());
            }
        }
    }
}
