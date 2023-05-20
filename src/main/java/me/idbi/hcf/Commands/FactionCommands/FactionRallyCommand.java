package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FactionRallyCommand extends SubCommand {

    @Override
    public String getName() {
        return "rally";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Enables the rally-mode.";
    }

    @Override
    public String getSyntax() {
        return "/faction " + getName();
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getName());
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
        if(hcfPlayer.inFaction()) {
            hcfPlayer.getFaction().setRallyPosition(p.getLocation());

            for (Player members : hcfPlayer.getFaction().getOnlineMembers()) {
                members.sendMessage(Messages.faction_rally.language(members).setPlayer(p).setWorld(p.getWorld()).setLoc(p.getLocation()).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }

}
