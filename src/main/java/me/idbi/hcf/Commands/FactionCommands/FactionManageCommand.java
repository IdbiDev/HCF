package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.Menus.MainInventory;
import me.idbi.hcf.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class FactionManageCommand  extends SubCommand {
    private static final Connection con = Main.getConnection();

    @Override
    public String getName() {
        return "manage";
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
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        p.openInventory(MainInventory.mainInv(p));
        addCooldown(p);
    }
}
