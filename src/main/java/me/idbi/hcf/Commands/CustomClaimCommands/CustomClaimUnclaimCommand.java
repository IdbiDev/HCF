package me.idbi.hcf.Commands.CustomClaimCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class CustomClaimUnclaimCommand extends SubCommand {
    @Override
    public String getName() {
        return "unclaim";
    }

    @Override
    public String getDescription() {
        return "Delete all claims of faction";
    }

    @Override
    public String getSyntax() {
        return "/customclaim unclaim <faction>";
    }


    @Override
    public String getPermission() {
        return "factions.commands.customclaim." + getName();
    }
    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction f = Playertools.getFactionByName(args[1].replaceAll("_", " "));
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(f == null || f.getLeader() != null) {
            hcfPlayer.sendMessage(Messages.not_found_faction);
            return;
        }
        f.clearClaims();
        hcfPlayer.sendMessage(Messages.success_unclaim);
    }
}
