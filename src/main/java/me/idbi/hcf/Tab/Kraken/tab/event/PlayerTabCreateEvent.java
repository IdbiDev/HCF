package me.idbi.hcf.Tab.Kraken.tab.event;


import lombok.Getter;
import me.idbi.hcf.Tab.Kraken.tab.PlayerTab;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerTabCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final PlayerTab playerTab;
    private final Player player;

    public PlayerTabCreateEvent(PlayerTab playerTab) {
        this.player = playerTab.getPlayer();
        this.playerTab = playerTab;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}