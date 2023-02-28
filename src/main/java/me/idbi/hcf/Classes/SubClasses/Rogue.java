package me.idbi.hcf.Classes.SubClasses;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.HCF_Class;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Rogue implements HCF_Class, Listener {
    private final HashMap<PotionEffectType, Integer> effect = new HashMap<>() {{
        put(PotionEffectType.SPEED, 2);
        put(PotionEffectType.DAMAGE_RESISTANCE, 2);
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
            return helmetType == Material.CHAINMAIL_HELMET
                    && chestplateType == Material.CHAINMAIL_CHESTPLATE
                    && leggingsType == Material.CHAINMAIL_LEGGINGS
                    && bootsType == Material.CHAINMAIL_BOOTS;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void setEffect(Player p) {
        for (Map.Entry<PotionEffectType, Integer> potionEffectTypeIntegerEntry : effect.entrySet()) {
            addEffect(p, potionEffectTypeIntegerEntry.getKey(), potionEffectTypeIntegerEntry.getValue());
        }
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        hcf.setClass(Classes.ROGUE);
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
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        hcf.setClass(Classes.NONE);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player damager) {
            if (e.getEntity() instanceof Player victim) {
                HCFPlayer hcf = HCFPlayer.getPlayer(damager);
                if (hcf.playerClass == Classes.ROGUE && ((Player) e.getDamager()).getItemInHand().getType() == Material.GOLD_SWORD) {
                    Location playerLoc = damager.getLocation();
                    Location targetLoc = victim.getLocation();
                    double pvecy = -Math.sin(Math.toRadians(playerLoc.getPitch()));
                    double pvecx = -Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.sin(Math.toRadians(playerLoc.getYaw()));
                    double pvecz = Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.cos(Math.toRadians(playerLoc.getYaw()));
                    double tvecy = -Math.sin(Math.toRadians(targetLoc.getPitch()));
                    double tvecx = -Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.sin(Math.toRadians(targetLoc.getYaw()));
                    double tvecz = Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.cos(Math.toRadians(targetLoc.getYaw()));
                    double dot = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
                    if (dot > 0.6D) {
                        e.setDamage(e.getDamage() * 2);
                        damager.playSound(
                                damager.getLocation(),
                                Sound.ITEM_BREAK, 1F, 1F);
                        damager.setItemInHand(null);
                    }
                }
            }
        }
    }
}
