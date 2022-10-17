package me.idbi.hcf.classes.subClasses;

import me.idbi.hcf.classes.HCF_Class;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Archer implements HCF_Class {
    private final HashMap<PotionEffectType,Integer> effect = new HashMap<>(){{
        put(PotionEffectType.SPEED,2);
        put(PotionEffectType.REGENERATION,0);
        put(PotionEffectType.DAMAGE_RESISTANCE,2);
        put(PotionEffectType.JUMP,0);
    }};
    public static final double ArcherTagDamageAmplifier = 100f;
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
            return helmetType == Material.LEATHER_HELMET
                    && chestplateType == Material.LEATHER_CHESTPLATE
                    && leggingsType == Material.LEATHER_LEGGINGS
                    && bootsType == Material.LEATHER_BOOTS;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void setEffect(Player p) {
        for (Map.Entry<PotionEffectType, Integer> potionEffectTypeIntegerEntry : effect.entrySet()) {
            addEffect(p,potionEffectTypeIntegerEntry.getKey(),potionEffectTypeIntegerEntry.getValue());
        }
        playertools.setMetadata(p, "class", "Archer");
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
    }
}
