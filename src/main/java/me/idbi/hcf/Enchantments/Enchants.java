package me.idbi.hcf.Enchantments;

import me.idbi.hcf.Enchantments.Enchantments.Armors.*;
import me.idbi.hcf.Enchantments.Enchantments.Bow.*;
import me.idbi.hcf.Enchantments.Enchantments.Others.Luck_of_The_Sea;
import me.idbi.hcf.Enchantments.Enchantments.Others.Lure;
import me.idbi.hcf.Enchantments.Enchantments.Others.Unbreaking;
import me.idbi.hcf.Enchantments.Enchantments.Swords.*;
import me.idbi.hcf.Enchantments.Enchantments.Tools.Efficiency;
import me.idbi.hcf.Enchantments.Enchantments.Tools.Fortune;
import me.idbi.hcf.Enchantments.Enchantments.Tools.Silk_Touch;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.persistence.Persistence;
import java.util.ArrayList;

public abstract class Enchants {

    private static ArrayList<Material> swords = EnchantTools.getSwords();
    private static ArrayList<Material> armors = EnchantTools.getArmors();
    private static ArrayList<Material> bow = EnchantTools.getBow();
    private static ArrayList<Material> fishingRod = EnchantTools.getFishingRod();
    private static ArrayList<Material> tools = EnchantTools.getTools();
    private static ArrayList<Material> unbreaking = new ArrayList<>() {{
        addAll(swords);
        addAll(armors);
        addAll(bow);
        addAll(fishingRod);
        addAll(tools);
    }};

    public abstract int getId();
    public abstract String getName();
    public abstract String getDisplayName(int level);
    public abstract int getMaxLevel();
    public abstract EnchantmentType getType();
    public abstract boolean canEnchant(Material material);
    public abstract boolean canEnchant(ItemStack is);
    public abstract ArrayList<Material> getMaterials();

    public abstract boolean conflictsWith(EnchantmentType... enchants);
    public abstract void cast(Player p, int level);
    public abstract void cast(Player p, Player target, int level);

    public enum EnchantmentType {
        // sword enchantments
        SHARPNESS(1, 6, swords, new Sharpness()),
        // SMITE,
        FIRE_ASPECT(2,2, swords, new Fire_Aspect()),
        LOOTING(3,3, swords, new Looting()),
        KNOCKBACK(4,2, swords, new Knockback()),

        // armor
        PROTECTION(5, 6, armors, new Projectile_Protection()),
        FIRE_PROTECTION(6, 6, armors, new Fire_Protection()),
        PROJECTILE_PROJECTION(7, 6, armors, new Projectile_Protection()),
        FEATHER_FALLING(8,6, armors, new Feather_Falling()),

        // tools
        EFFICIENCY(9,5, tools, new Efficiency()),
        SILK_TOUCH(10,1, tools, new Silk_Touch()),
        FORTUNE(11,3, tools, new Fortune()),

        // bow
        POWER(12,6, bow, new Power()),
        PUNCH(13,2, bow, new Punch()),
        FLAME(14,1, bow, new Flame()),

        // fishing rod
        LURE(15,3, fishingRod, new Lure()),
        LUCK_OF_THE_SEA(16,3, fishingRod, new Luck_of_The_Sea()),

        // others
        UNBREAKING(17,3, unbreaking, new Unbreaking()),

        // customs
        PULL(18,6, swords, new Pull()), // sword
        COMBO(19,6, swords, new Combo()), // sword
        CURSE(20,6, swords, new Curse()), // sword
        THUNDER(21,6, swords, new Thunder()), // sword
        HIGHWAY(22,6, armors, new Highway()), // armor
        VAMPIRE(23,6, armors, new Vampire()), // armor
        RECOVER(24,6, bow, new Recover()), // bow
        PIERCING(25,6, bow, new Piercing()), // bow
        POISON_ARROW(26,6, bow, new Poison_Arrow()); // bow

        public int id;
        public int maxLvl;
        public ArrayList<Material> materials;
        public boolean enabled;
        public Enchants enchants;

        EnchantmentType(int id, int maxLvl, ArrayList<Material> materials, Enchants enchants) {
            this.id = id;
            this.maxLvl = maxLvl;
            this.materials = materials;
            this.enabled = EnchantTools.isEnabled(this.name().toLowerCase());
            this.enchants = enchants;
        }

        public int getMaxLvL() {
            return this.maxLvl;
        }

        public int getId() {
            return this.id;
        }

        public ArrayList<Material> getMaterials() {
            return materials;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public Enchants asEnchant() {
            return this.enchants;
        }

        public String getName() {
            return this.name().toLowerCase();
        }

        public static EnchantmentType getById(int id) {
            for (EnchantmentType value : EnchantmentType.values()) {
                if(value.getId() == id) {
                    return value;
                }
            }

            return null;
        }

        public static EnchantmentType getByName(String name) {
            for (EnchantmentType value : EnchantmentType.values()) {
                if(value.getName().equals(name)) {
                    return value;
                }
            }

            return null;
        }
    }
}
