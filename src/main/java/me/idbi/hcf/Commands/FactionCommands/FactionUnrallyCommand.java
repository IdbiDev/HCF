package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

public class FactionUnrallyCommand extends SubCommand {


    @Override
    public String getName() {
        return "unrally";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction " + getName();
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(hcfPlayer.inFaction()) {
            hcfPlayer.getFaction().setRallyPosition(null);

            for (Player members : hcfPlayer.getFaction().getOnlineMembers()) {
                members.sendMessage(Messages.faction_unrally.language(members).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
