package me.idbi.hcf.Classes.SubClasses;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.HCF_Class;
import me.idbi.hcf.CustomFiles.Configs.ClassConfig;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Miner implements HCF_Class {
    public static final int invisibleLevel = ClassConfig.MinerInvisibleYLevel.asInt();
    public final boolean minerEnabled = ClassConfig.MinerEnabled.asBoolean();
    public final int maxMinerInFaction = ClassConfig.MaxMinerInFaction.asInt();
    public final boolean invisibleEnable = ClassConfig.MinerInvisibleEnabled.asBoolean();

    private final HashMap<PotionEffectType, Integer> effect = new HashMap<>() {{
        put(PotionEffectType.SPEED, 1);
        put(PotionEffectType.DAMAGE_RESISTANCE, 1);
        put(PotionEffectType.NIGHT_VISION, 0);
        put(PotionEffectType.FAST_DIGGING, 2);
        put(PotionEffectType.FIRE_RESISTANCE, 0);
    }};

    public static void setInvisMode(Player p, boolean state) {
        if (state) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1), true);
        } else {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @Override
    public boolean checkArmor(Player p) {
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
            return helmetType == Material.IRON_HELMET && chestplateType == Material.IRON_CHESTPLATE && leggingsType == Material.IRON_LEGGINGS && bootsType == Material.IRON_BOOTS;
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
        hcf.setPlayerClass(Classes.MINER);
        Scoreboards.refresh(p);
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
        hcf.setPlayerClass(Classes.NONE);
        Scoreboards.refresh(p);
    }
}
