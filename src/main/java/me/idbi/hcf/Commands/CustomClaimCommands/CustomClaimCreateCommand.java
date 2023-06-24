package me.idbi.hcf.Commands.CustomClaimCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class CustomClaimCreateCommand extends SubCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates a new Faction, with no leader. You can give color code to the faction name, or '_' to use space";
    }

    @Override
    public String getSyntax() {
        return "/customclaim create <name>";
    }


    @Override
    public String getPermission() {
        return "factions.commands.customclaim." +getName();
    }
    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        String name = args[1].replaceAll("_", " ");
        if(Playertools.getFactionByName(name) == null) {
            Faction faction = Playertools.createCustomFaction(name, null);
            p.sendMessage(Messages.faction_create_success.language(p).setFaction(faction).queue());
        } else {
            p.sendMessage(Messages.exists_faction_name.language(p).queue());
        }
    }
}
