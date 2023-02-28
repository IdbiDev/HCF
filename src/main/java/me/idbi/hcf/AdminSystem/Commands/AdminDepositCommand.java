package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.entity.Player;

public class AdminDepositCommand extends SubCommand {
    @Override
    public String getName() {
        return "deposit";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("d");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " <faction> <amount>";
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
        if (!args[2].matches("^[0-9]+$")) {
            p.sendMessage(Messages.not_a_number.language(p).queue());
            return;
        }
        int amount = Integer.parseInt(args[2]);

        if (!Main.nameToFaction.containsKey(args[1])) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }

        if (amount <= 0) {
            p.sendMessage(Messages.not_a_number.language(p).queue());
            return;
        }

        Faction selectedFaction = Main.nameToFaction.get(args[1]);
        selectedFaction.balance += amount;

        for (Player member : selectedFaction.getMembers()) {
            member.sendMessage(Messages.faction_admin_deposit_bc.language(member).setExecutor(p).queue());
        }
    }
}
