package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {
    @EventHandler
    public void PlayerLoginListener(PlayerLoginEvent e) {
        if (Main.EOTWENABLED && !e.getPlayer().hasPermission("factions.admin.eotwjoin")) {
            e.disallow(PlayerLoginEvent.Result.KICK_BANNED, Messages.eotw_already_started.language(HCFPlayer.getPlayer(e.getPlayer())).queue());
        }
    }
}
