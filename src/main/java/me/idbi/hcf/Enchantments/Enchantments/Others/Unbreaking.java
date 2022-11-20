package me.idbi.hcf.Enchantments.Enchantments.Others;

import me.idbi.hcf.Enchantments.Enchants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Unbreaking extends Enchants {
    @Override
    public int getId() {
        return this.getType().getId();
    }

    @Override
    public String getName() {
        return this.getType().getName();
    }

    @Override
    public String getDisplayName(int level) {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return this.getType().getMaxLvL();
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.UNBREAKING;
    }

    @Override
    public ArrayList<Material> getMaterials() {
        return this.getType().getMaterials();
    }

    @Override
    public boolean canEnchant(Material material) {
        return false;
    }

    @Override
    public boolean canEnchant(ItemStack is) {
        return false;
    }

    @Override
    public boolean conflictsWith(EnchantmentType... enchants) {
        return false;
    }

    @Override
    public void cast(Player p, int level) {

    }

    @Override
    public void cast(Player p, Player target, int level) {

    }
}