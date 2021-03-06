package me.idbi.hcf.classes;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static me.idbi.hcf.tools.playertools.getFactionMembersInDistance;


public class Bard {
    public static ArrayList<Bard_Item> bard_items = new ArrayList<>();
    private static final Main m = Main.getPlugin(Main.class);

    public static void setBardItems() {
        bard_items.add(new Bard_Item(Material.BLAZE_POWDER, PotionEffectType.INCREASE_DAMAGE, m.getConfig().getInt("strength")));
        bard_items.add(new Bard_Item(Material.SUGAR, PotionEffectType.SPEED, m.getConfig().getInt("speed")));
        bard_items.add(new Bard_Item(Material.FEATHER, PotionEffectType.JUMP, m.getConfig().getInt("jump_boost")));
        bard_items.add(new Bard_Item(Material.IRON_INGOT, PotionEffectType.DAMAGE_RESISTANCE, m.getConfig().getInt("resistance")));
        bard_items.add(new Bard_Item(Material.MAGMA_CREAM, PotionEffectType.FIRE_RESISTANCE, m.getConfig().getInt("fire_resistance")));
        bard_items.add(new Bard_Item(Material.REDSTONE, PotionEffectType.REGENERATION, m.getConfig().getInt("regeneration")));
        bard_items.add(new Bard_Item(Material.GHAST_TEAR, PotionEffectType.ABSORPTION, m.getConfig().getInt("absorption")));
        bard_items.add(new Bard_Item(Material.GOLD_INGOT, PotionEffectType.FAST_DIGGING, m.getConfig().getInt("haste")));
    }

    public static boolean CheckArmor(Player p) {
        try {
            //Get Archer armor
            ItemStack helmet = p.getInventory().getHelmet();
            ItemStack chestplate = p.getInventory().getChestplate();
            ItemStack leggings = p.getInventory().getLeggings();
            ItemStack boots = p.getInventory().getBoots();
            //Get Type
            Material helmetType = helmet.getType();
            Material chestplateType = chestplate.getType();
            Material leggingsType = leggings.getType();
            Material bootsType = boots.getType();
            //Check it
            return helmetType == Material.GOLD_HELMET && chestplateType == Material.GOLD_CHESTPLATE && leggingsType == Material.GOLD_LEGGINGS && bootsType == Material.GOLD_BOOTS;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void setEffect(Player p) {
        // Remove Effects;
        removeEffects(p);
        //Adding Effects
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        playertools.setMetadata(p, "class", "Bard");
    }

    public static void removeEffects(Player p) {

        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        p.removePotionEffect(PotionEffectType.SPEED);
        playertools.setMetadata(p, "bardenergy",0);

    }

    public static void ApplyBardEffectOnActionBar(Player bardplayer) {
        //Hotbar loop
        for (int i = 0; i <= 8; i++) {
            //Ha semmi nincs a kez??be, skippelj??k
            if (bardplayer.getInventory().getItem(i) == null) continue;
            //Ha van, ??s bard item akkor
            Bard_Item item = findBardItem(bardplayer.getInventory().getItem(i));
            if (item != null) {
                //V??gig megy??nk az ??sszes frakci??t??rson a k??zelbe
                for (Player p : getFactionMembersInDistance(bardplayer, 10)) {
                    PotionEffectType potion = item.effect;
                    p.addPotionEffect(new PotionEffect(potion, 180, 0, false, false));
                }
            }
        }
    }

    public static void OhLetsBreakItDown(Player bardplayer) {
        ItemStack main = bardplayer.getItemInHand();
        Bard_Item item;
        if (main != null) {
            item = findBardItem(main);
            if (item != null) {
                double currentEnergy = Double.parseDouble(playertools.getMetadata(bardplayer, "bardenergy"));
                if ((currentEnergy - item.cost) < 0) {
                    bardplayer.sendMessage(Messages.BARD_DONT_HAVE_ENOUGH_ENERGY.setAmount(String.valueOf(item.cost)).queue());
                    return;
                }
                main.setAmount(main.getAmount() - 1);
                bardplayer.getInventory().setItemInHand(main);
                playertools.setMetadata(bardplayer, "bardenergy", currentEnergy - item.cost);
                for (Player p : getFactionMembersInDistance(bardplayer, 15)) {
                    PotionEffectType potion = item.effect;
                    p.getActivePotionEffects().forEach(potionEffect -> {
                        if (potionEffect.getType().equals(potion)) {
                            p.removePotionEffect(potion);
                            p.addPotionEffect(new PotionEffect(potion, 180, potionEffect.getAmplifier() + 1, false, false));
                        }
                    });
                }
                bardplayer.sendMessage(Messages.BARD_USED_POWERUP
                        .setAmount(String.valueOf(item.cost))
                        .repBardEffects(
                                bardplayer,
                                getPotionName(item.effect.getName()),
                                String.valueOf(getFactionMembersInDistance(bardplayer, 15).size())
                        )
                        .queue()
                );
            }
        }
    }

    private static String getPotionName(String name) {
        name = name.replace("_", " ");
        return name.substring(0, 1).toUpperCase()
                + name.substring(1).toLowerCase();
    }

    private static Bard_Item findBardItem(ItemStack stack) {
        for (Bard_Item item : bard_items) {
            if (item.mat.equals(stack.getType())) {
                return item;
            }
        }
        return null;
    }

    static class Bard_Item {
        Material mat;
        PotionEffectType effect;
        int cost;

        public Bard_Item(Material mat, PotionEffectType type, int cost) {
            this.mat = mat;
            this.effect = type;
            this.cost = cost;
        }
    }
}
