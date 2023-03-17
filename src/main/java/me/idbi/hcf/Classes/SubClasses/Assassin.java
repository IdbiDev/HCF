package me.idbi.hcf.Classes.SubClasses;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.HCF_Class;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Assassin implements HCF_Class, Listener {
    static final int teleportDistance = 10;
    private final HashMap<PotionEffectType, Integer> effect = new HashMap<PotionEffectType, Integer>() {{
        put(PotionEffectType.SPEED, 0);
        put(PotionEffectType.REGENERATION, 0);
        put(PotionEffectType.DAMAGE_RESISTANCE, 0);
    }};

    public static void TeleportBehindPlayer(Player damager) {
        List<Block> sight = damager.getLineOfSight((Set) null, teleportDistance); //Get the blocks in the player's line of sight (the Set is null to not ignore any blocks)
        Player Victim = null;
        double record = 999;
        for (Block block : sight) { //For each block in the list
            damager.getWorld().playEffect(block.getLocation(), Effect.NOTE, 1);
            if (block.getType() != Material.AIR && !(block.getType().isTransparent() || block.getType() == Material.SIGN)) { //If the block is not air -> obstruction reached, exit loop/seach
                break;
            }
            Location low = block.getLocation(); //Lower corner of the block
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p == damager) continue;
                //System.out.println(low.distance(p.getLocation()));
                //3 > 2
                if (record > low.distance(p.getLocation().clone().add(0, 1, 0))) {
                    Victim = p;
                    record = low.distance(p.getLocation().clone().add(0, 1, 0));
                }
            }
        }
        if (Victim != null && record < 1.17f) {
            Vector v = new Vector(1, 0, 1);
            damager.teleport(Victim.getLocation().subtract(v));
            Location temp = lookAt(damager.getLocation(), Victim.getLocation());
            damager.teleport(temp);
        }
        //System.out.println(record);
        //TODO: Cooldown + ITEM


    }

    private static Location lookAt(Location loc, Location lookat) {
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();
        if (dx != 0) {
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
        loc.setPitch((float) -Math.atan(dy / dxz));
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);
        return loc;
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
        hcf.setPlayerClass(Classes.ASSASSIN);
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
    }

    private void PlaceBuff(Player p, Boolean state) {
        removeEffects(p);
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        int level = hcf.getAssassinState();
        if (state && level + 1 <= 16)
            level++;//-15
        else if (!state && level - 1 >= -16)
            level--;
        //Alap helyzet
        if (level >= 0) {
            addEffect(p, PotionEffectType.SPEED, 0);
            addEffect(p, PotionEffectType.REGENERATION, 0);
            addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 0);
        }
        //+ STAGE 1
        if (level > 5) {
            addEffect(p, PotionEffectType.SPEED, 1);
            addEffect(p, PotionEffectType.REGENERATION, 1);
            addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 1);
        }
        //+ STAGE 2
        if (level > 10) {
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
        if (level < 0) {
            addEffect(p, PotionEffectType.REGENERATION, 0);
            addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 0);
        }
        //- Stage 2
        if (level < -5) {
            addEffect(p, PotionEffectType.SLOW, 0);
            //- Stage 3
        }

        if (level < -15) {
            addEffect(p, PotionEffectType.SLOW, 1);
            addEffect(p, PotionEffectType.WEAKNESS, 0);
        }
        hcf.setAssassinState(level);
        Main.sendCmdMessage(p.getDisplayName() + "level >>" + level);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player damager) {
            if (e.getEntity() instanceof Player victim) {
                HCFPlayer hcfVictim = HCFPlayer.getPlayer(victim);
                HCFPlayer hcfDamager = HCFPlayer.getPlayer(damager);

                if (hcfVictim.getPlayerClass() == Classes.ASSASSIN) {
                    PlaceBuff(victim, false);
                }
                if (hcfDamager.getPlayerClass() == Classes.ASSASSIN) {
                    PlaceBuff(damager, true);
                }
            }
        }
    }
}
