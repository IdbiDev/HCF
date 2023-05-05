package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {

    @EventHandler
    public void PlayerLoginListener(PlayerLoginEvent e) {
        if (Main.EOTWENABLED && HCFPermissions.eotw_join.check(e.getPlayer())) {
            e.disallow(PlayerLoginEvent.Result.KICK_BANNED, Messages.eotw_already_started.language(HCFPlayer.getPlayer(e.getPlayer())).queue());
        }
        Player p = e.getPlayer();
        if(!HCFPermissions.slot_bypass.check(e.getPlayer()) && Bukkit.getOnlinePlayers().size() >= Main.serverSlot) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, Messages.server_is_full.language(p).queue());
        }
    }
}
