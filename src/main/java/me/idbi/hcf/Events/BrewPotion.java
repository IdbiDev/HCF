package me.idbi.hcf.Events;

import me.idbi.hcf.HCFRules;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.potion.Potion;

public class BrewPotion implements Listener {
    @EventHandler
    public void BrewPotion(BrewEvent e) {
        HCFRules rules = HCFRules.getRules();
        for (int i = 0; i <= 2; i++) {
            try {
                if (e.getContents().getContents()[i] != null) {
                    Potion potion = Potion.fromItemStack(e.getContents().getContents()[i]);

                    int maxLevel = rules.getMaxLevel(potion.getType().getEffectType());
                    int currentLevel = potion.getLevel();
                    boolean extendable = rules.isExtendable(potion.getType().getEffectType());
                    if (maxLevel == 0)
                        e.setCancelled(true);

                    if (potion.hasExtendedDuration() && !extendable)
                        e.setCancelled(true);

                    if (currentLevel > maxLevel)
                        e.setCancelled(true);

                }
            }catch (Exception ignored){}
        }

        //Bukkit.broadcastMessage(Arrays.toString(e.getContents().getContents()));
    }
}
