package me.idbi.hcf.classes.subClasses;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.classes.HCF_Class;
import me.idbi.hcf.particles.Shapes;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static me.idbi.hcf.tools.playertools.getFactionMembersInDistance;

public class Bard implements HCF_Class {
    private final HashMap<PotionEffectType,Integer> effect = new HashMap<>(){{
        put(PotionEffectType.SPEED,0);
        put(PotionEffectType.REGENERATION,0);
        put(PotionEffectType.DAMAGE_RESISTANCE,0);
    }};
    private static final Main m = Main.getPlugin(Main.class);
    private static final ArrayList<Bard_Item> bard_items = new ArrayList<>(){{
        add(new Bard_Item(Material.BLAZE_POWDER, PotionEffectType.INCREASE_DAMAGE, m.getConfig().getInt("strength")));
        add(new Bard_Item(Material.SUGAR, PotionEffectType.SPEED, m.getConfig().getInt("speed")));
        add(new Bard_Item(Material.FEATHER, PotionEffectType.JUMP, m.getConfig().getInt("jump_boost")));
        add(new Bard_Item(Material.IRON_INGOT, PotionEffectType.DAMAGE_RESISTANCE, m.getConfig().getInt("resistance")));
        add(new Bard_Item(Material.MAGMA_CREAM, PotionEffectType.FIRE_RESISTANCE, m.getConfig().getInt("fire_resistance")));
        add(new Bard_Item(Material.REDSTONE, PotionEffectType.REGENERATION, m.getConfig().getInt("regeneration")));
        add(new Bard_Item(Material.GHAST_TEAR, PotionEffectType.ABSORPTION, m.getConfig().getInt("absorption")));
        add(new Bard_Item(Material.GOLD_INGOT, PotionEffectType.FAST_DIGGING, m.getConfig().getInt("haste")));

    }};
    @Override
    public boolean CheckArmor(Player p) {
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

    @Override
    public void setEffect(Player p) {
        for (Map.Entry<PotionEffectType, Integer> potionEffectTypeIntegerEntry : effect.entrySet()) {
            addEffect(p,potionEffectTypeIntegerEntry.getKey(),potionEffectTypeIntegerEntry.getValue());
        }
        playertools.setMetadata(p, "class", "Bard");
        playertools.setMetadata(p, "bardenergy",0);
    }

    @Override
    public void addEffect(Player p, PotionEffectType type, int amplifier) {
        p.removePotionEffect(type);
        p.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, amplifier, false, false));
    }

    @Override
    public void removeEffects(Player p) {
        for (Map.Entry<PotionEffectType, Integer> potionEffectTypeIntegerEntry : effect.entrySet()) {
            p.removePotionEffect(potionEffectTypeIntegerEntry.getKey());
        }
        playertools.setMetadata(p, "class", "None");
        playertools.setMetadata(p, "bardenergy",0);
    }

     private static class Bard_Item {
        Material mat;
        PotionEffectType effect;
        int cost;

        public Bard_Item(Material mat, PotionEffectType type, int cost) {
            this.mat = mat;
            this.effect = type;
            this.cost = cost;
        }
    }
    public static void useSimpleBardEffect(Player bardplayer) {
        //Hotbar loop
        for (int i = 0; i <= 8; i++) {
            //Ha semmi nincs a kezébe, skippeljük
            if (bardplayer.getInventory().getItem(i) == null) continue;
            //Ha van, és bard item akkor
            Bard_Item item = findBardItem(bardplayer.getInventory().getItem(i));
            if (item != null) {
                //Végig megyünk az összes frakciótárson a közelbe
                for (Player p : getFactionMembersInDistance(bardplayer, 10)) {
                    PotionEffectType potion = item.effect;
                    p.addPotionEffect(new PotionEffect(potion, 180, 0, false, false));
                    Shapes.DrawCircle(bardplayer,10,2, Effect.HAPPY_VILLAGER);
                }
                //bardplayer.getWorld().playEffect(new Location(bardplayer.getWorld(),bardplayer.getLocation().getBlockX(),bardplayer.getLocation().getBlockY(),bardplayer.getLocation().getBlockZ()), Effect.HAPPY_VILLAGER,Effect.HAPPY_VILLAGER.getId());
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
                    if(!bardplayer.isSneaking())
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                    p.getActivePotionEffects().forEach(potionEffect -> {
                        if (potionEffect.getType().equals(potion)) {
                            p.removePotionEffect(potion);
                            p.addPotionEffect(new PotionEffect(potion, 180, potionEffect.getAmplifier() + 1, false, false));
                        }
                    });
                }
                new BukkitRunnable(){
                    private int counts = 0;
                    private final Location loc = bardplayer.getLocation();
                    @Override
                    public void run() {
                        if(bardplayer.isSneaking()) {
                            cancel();
                            return;
                        }
                        if(counts+1 > 15)
                            cancel();
                        counts++;
                        for(Player teammate : getFactionMembersInDistance(bardplayer,15))
                            Shapes.DrawCircle(teammate,counts,2, Effect.HAPPY_VILLAGER);
                    }
                }.runTaskTimer(m,0L,0L);
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
}
