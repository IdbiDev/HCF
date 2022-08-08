package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.WorldModes.EOTW;
import org.bukkit.entity.Player;

public class Faction_EOTW {

    public static void EOTW(Player p) {
        EOTW.EnableEOTW();
        p.sendMessage(Messages.ENABLE_EOTW.queue());
    }
}
