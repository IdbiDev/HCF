package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.Menus.MainInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class FactionManageCommand  extends SubCommand {
    private static final Connection con = Main.getCon();

    @Override
    public String getName() {
        return "manage";
    }

    @Override
    public String getDescription() {
        return "Opens a manage window. You can customize the faction is this mode.";
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
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(hcfPlayer.inFaction()) {
            p.openInventory(MainInventory.mainInv(p));
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
        addCooldown(p);
    }
}
