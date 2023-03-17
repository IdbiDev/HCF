package me.idbi.hcf.Classes;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;


public interface HCF_Class {


    boolean checkArmor(Player p);

    void setEffect(Player p);

    void addEffect(Player p, PotionEffectType type, int amplifier);

    void removeEffects(Player p);
}
