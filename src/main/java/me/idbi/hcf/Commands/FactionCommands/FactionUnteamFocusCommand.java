package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

public class FactionUnteamFocusCommand extends SubCommand {
    @Override
    public String getName() {
        return "unfocus";
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
        if (hcfPlayer.inFaction()) {
            Faction fac = hcfPlayer.getFaction();
            fac.setFocusedTeam(null);

            for (Player member : fac.getOnlineMembers()) {
                member.sendMessage(Messages.faction_team_unfocused.language(member).queue());
            }

            Scoreboards.RefreshAll();
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
