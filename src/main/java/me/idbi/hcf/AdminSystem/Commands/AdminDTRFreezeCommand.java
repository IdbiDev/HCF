package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminDTRFreezeCommand extends SubCommand {
    @Override
    public String getName() {
        return "freezedtr";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " <faction> <true | false>";
    }

    @Override
    public String getPermission() {
        return "factions.command.admin." + getName();
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        Faction faction = Playertools.getFactionByName(args[1]);
        if(faction != null) {
            try {
                faction.setDTRRegenEnabled(!Boolean.parseBoolean(args[2]));
            } catch (Exception e) {
                p.sendMessage(Messages.not_a_number.language(p).queue());
            }
            //Todo: Messages
        }else {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
        }


    }
}