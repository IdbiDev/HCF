package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminSetDTRCommand extends SubCommand {
    @Override
    public String getName() {
        return "setdtr";
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
        return "/admin " + getName() + " <faction> <dtr>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.admin." + getName();
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
        addCooldown(p);
        Faction faction = Playertools.getFactionByName(args[1]);
        if(faction != null) {
            try {
                double dtr = Double.parseDouble(args[2]);
                if(dtr > faction.getDTR_MAX())
                    dtr = faction.getDTR_MAX();
                faction.setDTR(dtr);
                p.sendMessage(Messages.admin_set_dtr.language(p).queue());
            }catch (Exception e) {
                p.sendMessage(Messages.not_a_number.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
        }
    }
}
