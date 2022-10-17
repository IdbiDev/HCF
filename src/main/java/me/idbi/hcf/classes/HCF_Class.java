package me.idbi.hcf.classes;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;


public interface HCF_Class {


    boolean CheckArmor(Player p);
    void setEffect(Player p);
    void addEffect(Player p, PotionEffectType type, int amplifier);
    void removeEffects(Player p);
}
