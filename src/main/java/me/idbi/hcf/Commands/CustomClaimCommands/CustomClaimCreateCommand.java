package me.idbi.hcf.Commands.CustomClaimCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class CustomClaimCreateCommand extends SubCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return null;
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
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        Faction faction = Playertools.createCustomFaction(args[1].replaceAll("_", " "), null);
        p.sendMessage(Messages.faction_create_success.language(p).setFaction(faction).queue());
    }
}
