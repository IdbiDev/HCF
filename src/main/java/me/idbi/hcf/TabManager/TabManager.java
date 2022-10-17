package me.idbi.hcf.TabManager;

import codecrafter47.bungeetablistplus.api.bungee.BungeeTabListPlusAPI;
import codecrafter47.bungeetablistplus.api.bungee.CustomTablist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TabManager implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerChatEvent e) {
        if(PlayerList.hasTab(e.getPlayer())) {
            PlayerList.getPlayerList(e.getPlayer()).clearCustomTabs();
            return;
        }

        PlayerList playerList = new PlayerList(e.getPlayer(), PlayerList.SIZE_FOUR);

        playerList.initTable();
        playerList.updateSlot(0,"Top left", e.getPlayer().getUniqueId());
        playerList.updateSlot(19,"Bottom left", e.getPlayer().getUniqueId());
        playerList.updateSlot(60,"Top right", e.getPlayer().getUniqueId());
        playerList.updateSlot(79,"Bottom right", e.getPlayer().getUniqueId());
        // playerList.

/*        CustomTablist customTablist = BungeeTabListPlusAPI.createCustomTablist();
        customTablist.setSize(80);
        // BungeeTabListPlusAPI.setCustomTabList(e.getPlayer(), customTablist);*/

    }
}
