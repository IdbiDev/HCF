package me.idbi.hcf.classes;

import me.idbi.hcf.Main;
import me.idbi.hcf.events.PlacePVPTag;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Assassin implements Listener {

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
            return helmetType == Material.CHAINMAIL_HELMET
                    && chestplateType == Material.CHAINMAIL_CHESTPLATE
                    && leggingsType == Material.CHAINMAIL_LEGGINGS
                    && bootsType == Material.CHAINMAIL_BOOTS;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void setEffect(Player p) {
        // Remove Effects;
        removeEffects(p);
        //Adding Effects
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        //p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        playertools.setMetadata(p, "class", "Assassin");
        playertools.setMetadata(p, "Assassin_state", 0);
    }

    public static void removeEffects(Player p) {
        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.REGENERATION);
        p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

        p.removePotionEffect(PotionEffectType.WEAKNESS);
        p.removePotionEffect(PotionEffectType.SLOW);
    }

    public static void PlaceBuff(Player p,Boolean state) {
        removeEffects(p);
        int level = Integer.parseInt(playertools.getMetadata(p,"Assassin_state"));
        if(state && level+1 <= 16)
            level++;//-15
        else if(!state && level-1 >= -16)
            level--;
        //Alap helyzet
        if(level >= 0){
            addEffect(p, PotionEffectType.SPEED, 0);
            addEffect(p, PotionEffectType.REGENERATION, 0);
            addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 0);
        }
        //+ STAGE 1
        if(level > 5){
            addEffect(p, PotionEffectType.SPEED, 1);
            addEffect(p, PotionEffectType.REGENERATION, 1);
            addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 1);
        }
        //+ STAGE 2
        if(level > 10){
            addEffect(p, PotionEffectType.SPEED, 2);
            addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 2);
            addEffect(p, PotionEffectType.INCREASE_DAMAGE, 0);
        }
        //+ STAGE 3
        if (level > 15) {
            addEffect(p, PotionEffectType.INCREASE_DAMAGE, 1);
        }
        //- Stage 1
        // -1-től -4-ig (4)
        if(level < 0) {
            addEffect(p, PotionEffectType.REGENERATION, 0);
            addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 0);
        }
        //- Stage 2
        if(level < -5) {
            addEffect(p, PotionEffectType.SLOW, 0);
        //- Stage 3
        }
        if (level < -15) {
            addEffect(p, PotionEffectType.SLOW, 1);
            addEffect(p, PotionEffectType.WEAKNESS, 0);
        }
        playertools.setMetadata(p,"Assassin_state",level);
        Main.sendCmdMessage(p.getDisplayName() + "level >>" + level);
    }

    public static void addEffect(Player p, PotionEffectType type, int amplifier) {
        p.removePotionEffect(type);
        p.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, amplifier, false, false));
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.isCancelled()) return;
        if(e.getDamager() instanceof Player damager) {
            if(e.getEntity() instanceof Player victim) {
                if(playertools.getMetadata(victim, "class").equalsIgnoreCase("Assassin")) {
                    PlaceBuff(victim, false);
                }
                if(playertools.getMetadata(damager, "class").equalsIgnoreCase("Assassin")) {
                    PlaceBuff(damager, true);
                }
            }
        }
    }

}
