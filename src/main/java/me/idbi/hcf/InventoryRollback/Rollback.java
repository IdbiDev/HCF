package me.idbi.hcf.InventoryRollback;

import com.avaje.ebeaninternal.server.transaction.log.LogTime;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Rollback {

    private int id;
    private UUID uuid;
    private HCFPlayer hcfPlayer;
    private float level;
    private EntityDamageEvent.DamageCause damageCause;
    private PlayerInventory inventory;
    private RollbackLogType logType;
    private Date date;
    private boolean rolledBack;

    public Rollback(int id, Player player, EntityDamageEvent.DamageCause damageCause, RollbackLogType logType, Date date) {
        this.uuid = player.getUniqueId();
        this.level = player.getExp();
        this.hcfPlayer = HCFPlayer.getPlayer(player);
        this.inventory = player.getInventory();
        this.damageCause = damageCause;
        this.logType = logType;
        this.rolledBack = false;
        this.date = date;
        this.id = id;
    }

    /**
     *
     * @return rollback was successfully
     */
    public boolean rollback() {
        Player target = Bukkit.getPlayer(this.uuid);
        if(target == null) return false;

        target.getInventory().setContents(this.inventory.getContents());
        target.getInventory().setArmorContents(this.inventory.getArmorContents());
        target.setExp(this.level);
        rolledBack = true;

        return true;
    }

    public UUID getUUID() {
        return this.uuid;
    }
    public int getId() {
        return this.id;
    }

    public HCFPlayer getHCFPlayer() {
        return this.hcfPlayer;
    }

    public boolean isRolled() {
        return this.rolledBack;
    }

    public RollbackLogType getLogType() {
        return this.logType;
    }

    public EntityDamageEvent.DamageCause getDamageCause() {
        return this.damageCause;
    }
    public float getExp() {
        return this.level;
    }

    public PlayerInventory getInventory() {
        return this.inventory;
    }

    public Date getDate() {
        return this.date;
    }

    public static enum RollbackLogType {
        JOIN, QUIT, DEATH, WORLD_CHANGE, FORCE, UNKNOWN;
    }
}
