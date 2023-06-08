package me.idbi.hcf.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.awt.*;

public class HCFNameChangeEvent extends Event implements Cancellable {
    private boolean cancelled;

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    private Player player;
    private String name;
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public HCFNameChangeEvent(Player p, String name) {
        this.cancelled = false;
        this.player = p;
        this.name = name;
    }
}
