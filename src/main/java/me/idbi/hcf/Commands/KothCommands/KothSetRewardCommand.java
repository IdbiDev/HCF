package me.idbi.hcf.Commands.KothCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.Koth.GUI.KOTHInventory;
import me.idbi.hcf.Main;
import org.bukkit.entity.Player;

public class KothSetRewardCommand extends SubCommand {
    @Override
    public String getName() {
        return "setreward";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("setrewards");
    }

    @Override
    public String getDescription() {
        return "Opens the KoTH reward window. It can be used to set the rewards. (Wow)";
    }

    @Override
    public String getSyntax() {
        return "/koth " + getName();
    }

    @Override
    public String getPermission() {
        return "factions.commands.koth." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        return false;
    }

    @Override
    public void addCooldown(Player p) {

    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (Main.kothRewardsGUI.contains(p.getUniqueId())) return;
        p.openInventory(KOTHInventory.kothInventory());
    }
}
