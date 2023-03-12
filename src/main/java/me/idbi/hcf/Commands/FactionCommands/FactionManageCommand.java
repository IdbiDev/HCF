package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;

public class FactionManageCommand  extends SubCommand {
    private static final Connection con = Main.getConnection();

    @Override
    public String getName() {
        return "manage";
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
        return "/faction manage";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
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
        p.openInventory(MainInventory.mainInv(p));
        addCooldown(p);
    }
}
