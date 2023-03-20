package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.LimitConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.potion.Potion;

import java.util.Arrays;

public class BrewPotion implements Listener {
    @EventHandler
    public void BrewPotion(BrewEvent e) {
        for (int i = 0; i <= 2; i++) {
            if (e.getContents().getContents()[i] != null) {
                Potion potion = Potion.fromItemStack(e.getContents().getContents()[0]);
                switch (potion.getType()) {
                    case SPEED:
                        if (potion.getLevel() > LimitConfig.Speed.asInt())
                            e.setCancelled(true);
                        break;
                    case JUMP:
                        if (potion.getLevel() > LimitConfig.Jump.asInt())
                            e.setCancelled(true);
                        break;
                    case REGEN:
                        if (potion.getLevel() > LimitConfig.Regen.asInt())
                            e.setCancelled(true);
                        break;
                    case POISON:
                        if (potion.getLevel() > LimitConfig.Poison.asInt())
                            e.setCancelled(true);
                        break;
                    case SLOWNESS:
                        if (potion.getLevel() > LimitConfig.Slowness.asInt())
                            e.setCancelled(true);
                        break;
                    case STRENGTH:
                        if (potion.getLevel() > LimitConfig.Strength.asInt())
                            e.setCancelled(true);
                        break;
                    case WEAKNESS:
                        if (potion.getLevel() > LimitConfig.Weakness.asInt())
                            e.setCancelled(true);
                        break;
                    case INSTANT_HEAL:
                        // 3                             0
                        if (potion.getLevel() > LimitConfig.InstantHeal.asInt())
                            e.setCancelled(true);
                        break;
                    case INSTANT_DAMAGE:
                        if (potion.getLevel() > LimitConfig.EmotionalDamage.asInt())
                            e.setCancelled(true);
                        break;
                    case FIRE_RESISTANCE:
                        if (potion.getLevel() > LimitConfig.FireResistance.asInt())
                            e.setCancelled(true);
                        break;
                    case WATER_BREATHING:
                        if (potion.getLevel() > LimitConfig.WaterBreathing.asInt())
                            e.setCancelled(true);
                        break;
                    default:
                        break;
                }
            }
        }

        Bukkit.broadcastMessage(Arrays.toString(e.getContents().getContents()));
    }
}
