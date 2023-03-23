package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FactionTeamFocusCommand extends SubCommand {
    @Override
    public String getName() {
        return "focus";
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
        return "/faction " + getName() + " <faction>";
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
        if (!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
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
        if (hcfPlayer.inFaction()) {
            String name = args[1];
            Faction fac = hcfPlayer.getFaction();
            Faction focus = Playertools.getFactionByName(name);
            if(focus == null) {
                p.sendMessage(Messages.not_found_faction.language(p).queue());
                return;
            }

            if(fac.getId() == focus.getId()) {
                p.sendMessage(Messages.faction_own_team_focus.language(p).queue());
                return;
            }

            for (Player member : fac.getOnlineMembers()) {
                member.sendMessage(Messages.faction_team_focused.language(member).setFaction(focus).setPlayer(p).queue());
            }

            fac.setFocusedTeam(focus);
            Scoreboards.RefreshAll();
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
