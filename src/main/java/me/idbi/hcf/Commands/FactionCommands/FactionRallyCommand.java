package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

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
