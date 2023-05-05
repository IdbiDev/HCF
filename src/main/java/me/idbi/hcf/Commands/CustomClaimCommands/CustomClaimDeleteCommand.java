package me.idbi.hcf.Commands.CustomClaimCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class CustomClaimDeleteCommand extends SubCommand {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Deletes the custom faction.";
    }

    @Override
    public String getSyntax() {
        return "/customclaim delete <faction>";
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
        if(f == null || f.getLeader() != null) {
            HCFPlayer.getPlayer(p).sendMessage(Messages.not_found_faction);
            return;
        }
        p.sendMessage(Messages.faction_delete_success.language(p).setFaction(f).queue());
        f.selfDestruct();
    }
}
