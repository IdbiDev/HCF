package me.idbi.hcf.classes;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Miner {
    public static final double min_y_value = 35;

    public static boolean CheckArmor(Player p){
        try{
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
        } catch(NullPointerException e) {
            return false;
        }
    }

    public static void setEffect(Player p){
        // Remove Effects;
        removeEffects(p);
        //Adding Effects
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1,false,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,2,false,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1,false,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,Integer.MAX_VALUE,2,false,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,1,false,false));
        playertools.setMetadata(p,"class","Miner");
    }

    public static void removeEffects(Player p){
        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        p.removePotionEffect(PotionEffectType.FAST_DIGGING);
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    public static void setInvisMode(Player p,boolean state) {
        Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
        Player[] teammates = playertools.getFactionOnlineMembers(faction.factioname);
        Player[] randomPlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        for(Player player : randomPlayers){
            for(Player teammate : teammates){
                if(player == teammate){
                    continue;
                }
                if(state){
                    p.hidePlayer(player);
                }else{
                    p.showPlayer(player);
                }
            }
        }

    }
}
