package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.entity.Player;

import java.util.HashMap;

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
        if (!Main.nameToFaction.containsKey(args[1])) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            return;
        }
//        if (args[2].length() > 5) {
//            p.sendMessage(Messages.not_a_number.language(p).queue());
//            return;
//        }
        Faction selectedFaction = Main.nameToFaction.get(args[1]);
        selectedFaction.setName(args[2]);
        //Todo: Broadcast the change
        selectedFaction.saveFactionData();

        for (Player member : selectedFaction.getOnlineMembers()) {
            member.sendMessage(Messages.set_faction_name.language(member).setExecutor(p).setFaction(args[2]).queue());
        }

        p.sendMessage(Messages.admin_set_faction_name.setFaction(selectedFaction.getName()).queue());
        Main.nameToFaction.remove(args[1])  ;
        Main.nameToFaction.put(args[2],selectedFaction);
        Scoreboards.RefreshAll();
    }
}
