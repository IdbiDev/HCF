package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
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
        return null;
    }

    @Override
    public String getSyntax() {
        return "/f claim";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
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
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (Playertools.getPlayerFaction(p) != null) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            /*if(Config.MaxClaims.asInt() > hcfPlayer.getFaction().getClaims().size()) {
                p.sendMessage("Igen?"); // ToDo: Not working
                return;
            }*/
            if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().setItem(p.getInventory().firstEmpty(), HCF_Claiming.Wands.claimWand());

                //ListMessages.CLAIM_INFO.queue().forEach(p::sendMessage);
                hcfPlayer.setClaimType(HCF_Claiming.ClaimTypes.FACTION);
                addCooldown(p);
            } else {
                p.sendMessage(Messages.not_enough_slot.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());

        }
    }
}
