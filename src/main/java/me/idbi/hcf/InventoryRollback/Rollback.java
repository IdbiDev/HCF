package me.idbi.hcf.InventoryRollback;

import com.avaje.ebeaninternal.server.transaction.log.LogTime;
import lombok.Getter;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Rollback {

    @Getter private int id;
    private HCFPlayer hcfPlayer;
    private UUID uuid;
    @Getter private float exp;
    @Getter private EntityDamageEvent.DamageCause damageCause;
    @Getter private ItemStack[] contents;
    @Getter private ItemStack[] armorContents;
    @Getter private RollbackLogType logType;
    @Getter private Date date;
    @Getter private boolean rolled;

    public Rollback(int id, Player player, EntityDamageEvent.DamageCause damageCause, RollbackLogType logType, Date date) {
        this.uuid = player.getUniqueId();
        this.exp = player.getExp();
        this.contents = player.getInventory().getContents().clone();
        this.armorContents = player.getInventory().getArmorContents().clone();
        this.damageCause = damageCause;
        this.logType = logType;
        this.rolled = false;
        this.date = date;
        this.id = id;
        this.hcfPlayer = HCFPlayer.getPlayer(player);
    }

    /**
     *
     * @return rollback was successfully
     */
    public boolean rollback() {
        Player target = Bukkit.getPlayer(this.uuid);
        if(target == null) return false;

        target.getInventory().setContents(this.contents);
        target.getInventory().setArmorContents(this.armorContents);
        target.setExp(this.exp);
        rolled = true;

        return true;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public HCFPlayer getHCFPlayer() {
        return this.hcfPlayer;
    }

    public static enum RollbackLogType {
        JOIN, QUIT, DEATH, WORLD_CHANGE, FORCE, UNKNOWN;
    }
}
