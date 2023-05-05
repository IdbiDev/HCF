package me.idbi.hcf.Commands.CustomClaimCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class CustomClaimClaimCommand extends SubCommand {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String getDescription() {
        return "Valid claim types: 'Protected': PvP and building is denied, 'Special': PvP is allowed but build is denied";
    }

    @Override
    public String getPermission() {
        return "factions.commands.customclaim." + getName();
    }

    @Override
    public String getSyntax() {
        return "/customclaim claim <name> <claim type>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        Faction faction = Playertools.getFactionByName(args[1].replaceAll("_", " "));

        if(faction == null || faction.getLeader() != null) {
            hcfPlayer.sendMessage(Messages.not_found_faction);
            return;
        }

        Claiming.ClaimTypes type = Claiming.ClaimTypes.getByName(args[2]);
        if(type == null) {
            hcfPlayer.sendMessage(Messages.invalid_type);
        }

        if(hcfPlayer.giveWand(p)) {
            hcfPlayer.setClaimType(type);
            hcfPlayer.setClaimID(faction.getId());
        } else {
            hcfPlayer.sendMessage(Messages.not_enough_slot);
        }
    }
}
