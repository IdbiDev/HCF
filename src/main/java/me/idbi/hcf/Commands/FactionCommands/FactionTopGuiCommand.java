package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.FactionListGUI.FactionToplistInventory;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;


public class FactionTopGuiCommand extends SubCommand {
    @Override
    public String getName() {
    return "topgui";
    }

    @Override
    public String getDescription() {
        return "Opens a GUI with top factions listed!";
    }

    @Override
    public String getSyntax() {
        return "/faction " + getName();
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        p.openInventory(FactionToplistInventory.topInv(p, Playertools.sortByPoints(), "Points", 1));
    }
}
