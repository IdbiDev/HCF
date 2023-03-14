package me.idbi.hcf.InventoryRollback.GUI;

import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.IItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

public class PlayerRollbackItems {
    /*
                "&eDate &8» &6%date%",
            "&eDamage Cause &8» &6%damage_cause%",
            "&eEXP &8» &6%exp%",
            "&eType &8» &6%type%",
            "&eRolled Back &8» &6%rolled%",
            "&5",
            "&aLEFT + CLICK here to view!",
            "&aRIGHT + CLICK here to rollback!"
     */
    public static ItemStack getPlayerRollbackInfo(Player owner, Rollback rollback) {

        ItemStack is = IItemBuilder.create(rollback.isRolled() ? Material.WOOL : Material.STICK, 1,(short) 0, (byte) (rollback.isRolled() ? 5 : 0))
                .setName(GUIMessages.rollback_information.language(owner).setPlayerName(rollback.getHCFPlayer().getName()).setId(rollback.getId()).getName())
                .setLore(GUIMessages.rollback_information.language(owner).setPlayerName(rollback.getHCFPlayer().getName()).setId(rollback.getId())
                        .setDate(rollback.getDate())
                        .setRolled(rollback.isRolled())
                        .setDamageCause(rollback.getDamageCause())
                        .setLogType(rollback.getLogType())
                        .setEXPLevel(getLevelFromExp((long) rollback.getExp()) + "").getLore())
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .addEnchantment(Enchantment.DURABILITY, rollback.getId())
                .finish();
        return is;
    }

    public static ItemStack getPlayerRollbackInfoActive(Player owner, Rollback rollback) {
        ItemStack is = IItemBuilder.create(rollback.isRolled() ? Material.WOOL : Material.BLAZE_ROD, 1,(short) 0, (byte) (rollback.isRolled() ? 5 : 0))
                .setName(GUIMessages.rollback_information.language(owner).setPlayerName(rollback.getHCFPlayer().getName()).setId(rollback.getId()).getName())
                .setLore(GUIMessages.rollback_information.language(owner).setPlayerName(rollback.getHCFPlayer().getName()).setId(rollback.getId())
                        .setDate(rollback.getDate())
                        .setRolled(rollback.isRolled())
                        .setDamageCause(rollback.getDamageCause())
                        .setLogType(rollback.getLogType())
                        .setEXPLevel(getLevelFromExp((long) rollback.getExp()) + "").getLore())
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .addEnchantment(Enchantment.DURABILITY, rollback.getId())
                .finish();
        return is;
    }

    public static double getLevelFromExp(long exp) {
        if (exp > 1395) {
            return (Math.sqrt(72 * exp - 54215) + 325) / 18;
        }
        if (exp > 315) {
            return Math.sqrt(40 * exp - 7839) / 10 + 8.1;
        }
        if (exp > 0) {
            return Math.sqrt(exp + 9) - 3;
        }
        return 0;
    }
}
