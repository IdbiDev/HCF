package me.idbi.hcf.classes;

import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Objects;

import static me.idbi.hcf.tools.playertools.getFactionMembersInDistance;


public class Bard {
    public static HashMap<Material, PotionEffectType> barditems = new HashMap<Material, PotionEffectType>();

    public static void setBardItems(){
        barditems.put(Material.SUGAR,PotionEffectType.SPEED);
        barditems.put(Material.IRON_INGOT,PotionEffectType.DAMAGE_RESISTANCE);
        barditems.put(Material.BLAZE_POWDER,PotionEffectType.INCREASE_DAMAGE);
        barditems.put(Material.FEATHER,PotionEffectType.JUMP);

        barditems.put(Material.REDSTONE,PotionEffectType.REGENERATION);
        barditems.put(Material.GHAST_TEAR,PotionEffectType.ABSORPTION);
    }
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
            return helmetType == Material.GOLD_HELMET && chestplateType == Material.GOLD_CHESTPLATE && leggingsType == Material.GOLD_LEGGINGS && bootsType == Material.GOLD_BOOTS;
        }catch(NullPointerException e){
            return false;
        }
    }

    public static void setEffect(Player p){
        // Remove Effects;
        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        p.removePotionEffect(PotionEffectType.SPEED);
        //Adding Effects
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,0,false,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,0,false,false));
        playertools.setMetadata(p,"class","bard");
    }
    public static void removeEffects(Player p){

        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        p.removePotionEffect(PotionEffectType.SPEED);

    }


    public static void ApplyBardEffectOnActionBar(Player bardplayer) {
        //Hotbar loop
        for(int i = 0;i<=8;i++){
            //Ha semmi nincs a kezébe, skippeljük
            if (bardplayer.getInventory().getItem(i) == null) continue;
            //Ha van, és bard item akkor
            if(barditems.containsKey(bardplayer.getInventory().getItem(i).getType())){
                //Végig megyünk az összes frakciótársan a közelbe
                for (Player p:getFactionMembersInDistance(bardplayer,10)) {
                    PotionEffectType potion = barditems.get(Objects.requireNonNull(bardplayer.getInventory().getItem(i)).getType());
                    p.addPotionEffect(new PotionEffect(potion,180,0,false,false));
                    System.out.println(p.getDisplayName()+" got "+potion.getName()+" effect from "+bardplayer.getDisplayName());
                }
            }
        }
    }



}
