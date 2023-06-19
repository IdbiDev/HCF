package me.idbi.hcf;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Getter;
import me.idbi.hcf.CustomFiles.LimitsFile;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HCFRules {
    @Getter
    private static HCFRules rules;

    @Getter private ArrayList<Material> blacklistedBlocks;
    @Getter private ArrayList<Material> allyBlocks;
    @Getter private HashMap<Enchantment, Integer> enchantLevels;
    @Getter private HashMap<PotionEffectType, Integer> potionLimit;
    @Getter private HashMap<PotionEffectType, Boolean> potionExtendable;
    //@Getter private HashMap<EntityType, Boolean> entityLimiter;

    public HCFRules() {
        rules = this;
        this.blacklistedBlocks = new ArrayList<>();
        this.allyBlocks = new ArrayList<>();
        this.enchantLevels = new HashMap<>();
        this.potionLimit = new HashMap<>();
        this.potionExtendable = new HashMap<>();
        //this.entityLimiter = new HashMap<>();
        setupAllowedLevels();
        setupPotions();
        setupBlacklistedBlocks();
        setupAlly();
        //setupEntities();
    }

    public void reload() {
        this.blacklistedBlocks.clear();
        this.allyBlocks.clear();
        this.enchantLevels.clear();
        this.potionLimit.clear();
        this.potionExtendable.clear();
        //this.entityLimiter = new HashMap<>();
        setupAllowedLevels();
        setupPotions();
        setupBlacklistedBlocks();
        setupAlly();
    }

    public void clearLists(){
        blacklistedBlocks.clear();
        allyBlocks.clear();
        enchantLevels.clear();
        potionLimit.clear();
        potionExtendable.clear();
        //entityLimiter.clear();
    }
    private void setupAllowedLevels() {
        for (String key : LimitsFile.get().getConfigurationSection("ENCHANTMENT_LIMITER").getKeys(false)) {
            int level = LimitsFile.get().getInt(key);
            Enchantment enc = Enchantment.getByName(key.toUpperCase());
            if(enc == null) continue;
            enchantLevels.put(enc, level);
        }
    }

   /* private void setupEntities() {
        for (String key : LimitsFile.get().getConfigurationSection("ENTITY_LIMITER").getKeys(false)) {
            boolean limited = LimitsFile.get().getBoolean(key);
            EntityType type = null;
            try {
                type = EntityType.valueOf(key.toUpperCase());
            } catch (Exception e) {
                continue;
            }
            if(type == null) continue;
            entityLimiter.put(type, limited);
        }
    }
*/
    private void setupBlacklistedBlocks() {
        for (String block : LimitsFile.get().getStringList("BLACKLISTED_BLOCKS")) {
            Material material = Material.getMaterial(block.toUpperCase());
            if(material == null) continue;
            blacklistedBlocks.add(material);
        }
    }

    private void setupAlly() {
        for (String block : LimitsFile.get().getStringList("ALLY")) {
            Material material = Material.getMaterial(block.toUpperCase());
            if(material == null) continue;
            allyBlocks.add(material);
        }
    }

    private void setupPotions() {
        for (String key : LimitsFile.get().getConfigurationSection("POTION_LIMITER").getKeys(false)) {
            int level = LimitsFile.get().getInt("POTION_LIMITER." + key + ".LEVEL");
            boolean extendable = LimitsFile.get().getBoolean("POTION_LIMITER." + key + ".EXTENDED");
            PotionEffectType type = PotionEffectType.getByName(key.toUpperCase());
            if(type == null) continue;
            potionLimit.put(type, level);
            potionExtendable.put(type, extendable);
        }
    }

    public boolean isAlly(Block block) {
        if(this.allyBlocks.contains(block.getType())) return true;
        return false;
    }

    public boolean isAlly(Material material) {
        if(this.allyBlocks.contains(material)) return true;
        return false;
    }

//    public boolean isEntityLimited(Entity ent) {
//        if(!this.entityLimiter.containsKey(ent.getType())) return false;
//        return this.entityLimiter.get(ent.getType());
//    }
    public boolean  isEntityLimited(Entity ent, World world) {
        if(ent instanceof Player)
            return false;
        HCFMap map = HCFServer.getServer().getMap(world);
        if(map == null)
            return false;
        return map.getEntityLimiter().get(ent.getType()) != null && map.getEntityLimiter().get(ent.getType());
    }
    public boolean isBlacklistedBlock(Block block) {
        return this.blacklistedBlocks.contains(block.getType());
    }

    public boolean isBlacklistedBlock(Material material) {
        return this.blacklistedBlocks.contains(material);
    }

    public int getMaxLevel(PotionEffectType type) {
        if(!this.potionLimit.containsKey(type)) return -1;
        return this.potionLimit.get(type);
    }

    public int getMaxLevel(Enchantment enchantment) {
        if(!this.enchantLevels.containsKey(enchantment)) return -1;
        return this.enchantLevels.get(enchantment);
    }

    public boolean isExtendable(PotionEffectType type) {
        if(!this.potionExtendable.containsKey(type)) return false;
        return this.potionExtendable.get(type);
    }

    public static final String startMessage = " _    _   _____  ______        _                        _            _ \n" +
            "| |  | | / ____||  ____| _    | |                      | |          | |\n" +
            "| |__| || |     | |__  _| |_  | |      ___    __ _   __| |  ___   __| |\n" +
            "|  __  || |     |  __||_   _| | |     / _ \\  / _` | / _` | / _ \\ / _` |\n" +
            "| |  | || |____ | |     |_|   | |____| (_) || (_| || (_| ||  __/| (_| |\n" +
            "|_|  |_| \\_____||_|           |______|\\___/  \\__,_| \\__,_| \\___| \\__,_|\n" +
            "                                                                       ";
    public static final String startMessage2 = "  _    _    _    _    _    _    _     _    _    _     _    _    _    _     _    _    _     _    _    _    _  \n" +
            " / \\  / \\  / \\  / \\  / \\  / \\  / \\   / \\  / \\  / \\   / \\  / \\  / \\  / \\   / \\  / \\  / \\   / \\  / \\  / \\  / \\ \n" +
            "( C )( r )( e )( a )( t )( e )( d ) ( b )( y )( : ) ( K )( o )( b )( a ) ( a )( n )( d ) ( I )( d )( b )( i )\n" +
            " \\_/  \\_/  \\_/  \\_/  \\_/  \\_/  \\_/   \\_/  \\_/  \\_/   \\_/  \\_/  \\_/  \\_/   \\_/  \\_/  \\_/   \\_/  \\_/  \\_/  \\_/";
    @Getter public static final int maxInvitesPerFaction = 14; // >> GUI limit
    @Getter public static final int maxMembersPerFaction = 14; // >> GUI limit
    @Getter public static final int maxRanksPerFaction = 28; // >> GUI limit
    @Getter public static final int maxAlliesPerFaction = 14; // >> GUI limit
    @Getter public static final String version = "1.1";
    public static final String authors = "Idbi & Koba";
    public static final String discord = "https://discord.gg/aaswGgjJgU";
    public static final String website = "None";
    public static final String startMessageInfo = "\n" +
            "-------------------------------------------\n" +
            "HCF Plus | Version " + version + "\n" +
            ">>Authors: " + authors + "\n" +
            ">>Discord: " + discord + "\n" +
            ">>Website: " + website + "\n" +
            "-------------------------------------------";
    public static ImmutableSet blockedPearlTypes;

    /*
        Protection
        Sharpness
        FireAspect
        Feather Falling
        Durability
        Flame
        Infinity
        Punch
        Knockback
        Power
        Efficiency
        Silk Touch
     */
    public static EnchantClass randomEnchant() {
        double a = Math.random();
        int c;//Enchantment
        EnchantClass enchant_obj = null;
        if (a <= 0.1) { // 10%
            //Infinity 1
            //Fire Aspect 2
            //Silk Touch 3
            //Flame 4
            c = new Random().nextInt(5);
            switch (c) {
                case 0:
                    enchant_obj = new EnchantClass(Enchantment.ARROW_INFINITE, 1);
                    break;
                case 1:
                    enchant_obj = new EnchantClass(Enchantment.FIRE_ASPECT, 1);
                    break;
                case 2:
                    enchant_obj = new EnchantClass(Enchantment.SILK_TOUCH, 1);
                    break;
                case 3:
                    enchant_obj = new EnchantClass(Enchantment.ARROW_FIRE, 1);
                    break;
                case 4:
                    enchant_obj = new EnchantClass(Enchantment.getById(100), 1);
                    break;
            }
        } else if (a <= 0.5) {
            //Efficiency
            //Punch
            //Knockback
            c = new Random().nextInt(6);
            int lvl = (int) (Math.random() * (2 - 1 + 1) + 1);
            switch (c) {
                case 0: enchant_obj = new EnchantClass(Enchantment.DIG_SPEED, lvl); break;
                case 1: enchant_obj = new EnchantClass(Enchantment.ARROW_DAMAGE, lvl); break;
                case 2: enchant_obj = new EnchantClass(Enchantment.KNOCKBACK, lvl); break;
                case 3: enchant_obj = new EnchantClass(Enchantment.ARROW_KNOCKBACK, lvl); break;
                case 4: enchant_obj = new EnchantClass(Enchantment.DURABILITY, lvl); break;
                case 5: enchant_obj = new EnchantClass(Enchantment.getById(100), lvl); break;
            }
        } else {
            //Protection
            //Sharpness
            //Feather falling
            c = new Random().nextInt(4);
            //Math.random() * (max - min + 1) + min
            int lvl = (int) (Math.random() * (2 - 1 + 1) + 1);
            switch (c) {
                case 0: enchant_obj = new EnchantClass(Enchantment.PROTECTION_ENVIRONMENTAL, lvl); break;
                case 1: enchant_obj = new EnchantClass(Enchantment.DAMAGE_ALL, lvl); break;
                case 2: enchant_obj = new EnchantClass(Enchantment.PROTECTION_FALL, lvl); break;
                case 3: enchant_obj = new EnchantClass(Enchantment.getById(100), lvl); break;
            }
        }
        return enchant_obj;
    }

    public static void setupPearlGlitch() {
        blockedPearlTypes = Sets.immutableEnumSet((Enum) Material.THIN_GLASS,
                (Enum[]) new Material[]{
                        Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE,
                        Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS,
                        Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS,
                        Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS,
                        Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS});
    }

    public static class EnchantClass {
        private final Enchantment enchantment;
        private final int level;

        EnchantClass(Enchantment enc, int level) {
            this.enchantment = enc;
            this.level = level;
        }

        public Enchantment getEnchantment() {
            return this.enchantment;
        }

        public int getLevel() {
            return this.level;
        }
    }


}
