package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.entity.Player;

public class AdminSetFactionNameCommand extends SubCommand {
    @Override
    public String getName() {
        return "setfactionname";
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
        return "/admin " + getName() + " <faction> <new name>";
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
        if (!Main.nameToFaction.containsKey(args[1])) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }
        if (args[2].length() > 5) {
            p.sendMessage(Messages.not_a_number.language(p).queue());
            return;
        }
        Faction selectedFaction = Main.nameToFaction.get(args[1]);
        selectedFaction.name = args[2];
        //Todo: Broadcast the change

        for (Player member : selectedFaction.getMembers()) {
            member.sendMessage(Messages.set_faction_name.language(member).setExecutor(p).setFaction(args[2]).queue());
        }

        p.sendMessage(Messages.admin_set_faction_name.setFaction(selectedFaction.name).queue());

        Scoreboards.RefreshAll();
    }
}
