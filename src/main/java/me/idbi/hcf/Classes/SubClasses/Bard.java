package me.idbi.hcf.Classes.SubClasses;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.HCF_Class;
import me.idbi.hcf.CustomFiles.Configs.ClassConfig;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Particles.Shapes;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static me.idbi.hcf.Tools.Playertools.getFactionMembersInDistance;

public class Bard implements HCF_Class {
    private final Main m = Main.getPlugin(Main.class);
    public final boolean bardEnabled = ClassConfig.BardEnabled.asBoolean();
    public final boolean boostEnabled = ClassConfig.BoostEnabled.asBoolean();
    public final boolean simpleBoostEnabled = ClassConfig.SimpleBoostEnabled.asBoolean();
    public final int maxBardEnergy = ClassConfig.MaxBardEnergy.asInt();
    public final double bardEnergyMultiplier = ClassConfig.BardEnergyMultiplier.asDouble();
    public final int maxBardInFaction = ClassConfig.MaxBardInFaction.asInt();
    public final boolean enableNewBard = ClassConfig.UseNewBardSystem.asBoolean();
    public final boolean enableEffects = ClassConfig.UseEffects.asBoolean();


    private final ArrayList<Bard_Item> bard_items = new ArrayList<>() {{
        //todo: Effects enable - disable using config
        add(new Bard_Item(Material.BLAZE_POWDER, PotionEffectType.INCREASE_DAMAGE, 2));
        add(new Bard_Item(Material.SUGAR, PotionEffectType.SPEED, 2));
        add(new Bard_Item(Material.FEATHER, PotionEffectType.JUMP, 2));
        add(new Bard_Item(Material.IRON_INGOT, PotionEffectType.DAMAGE_RESISTANCE, 10));
        add(new Bard_Item(Material.MAGMA_CREAM, PotionEffectType.FIRE_RESISTANCE, 10));
        add(new Bard_Item(Material.REDSTONE, PotionEffectType.REGENERATION, 10));
        add(new Bard_Item(Material.GHAST_TEAR, PotionEffectType.ABSORPTION, 2));
        add(new Bard_Item(Material.GOLD_INGOT, PotionEffectType.FAST_DIGGING, 2));
    }};
    private final HashMap<PotionEffectType, Integer> effect = new HashMap<>() {{
        put(PotionEffectType.SPEED, 0);
        put(PotionEffectType.REGENERATION, 0);
        put(PotionEffectType.DAMAGE_RESISTANCE, 0);
    }};

    public void useSimpleBardEffect(Player bardplayer) {
        if(simpleBoostEnabled) {
            //Hotbar loop
            if(enableNewBard) {
                for (int i = 0; i <= 8; i++) {
                    //Ha semmi nincs a kezébe, skippeljük
                    if (bardplayer.getInventory().getItem(i) == null) continue;
                    //Ha van, és bard item akkor
                    Bard_Item item = findBardItem(bardplayer.getInventory().getItem(i));
                    if (item != null) {
                        //Végig megyünk az összes frakciótárson a közelbe
                        for (Player p : getFactionMembersInDistance(bardplayer, 10)) {
                            PotionEffectType potion = item.effect;
                            p.addPotionEffect(new PotionEffect(potion, 180, 0, false, false));
                            if(enableEffects && !bardplayer.isSneaking())
                                Shapes.DrawCircle(p, bardplayer.getLocation(), 10, 2, Effect.HAPPY_VILLAGER);
                        }
                        //bardplayer.getWorld().playEffect(new Location(bardplayer.getWorld(),bardplayer.getLocation().getBlockX(),bardplayer.getLocation().getBlockY(),bardplayer.getLocation().getBlockZ()), Effect.HAPPY_VILLAGER,Effect.HAPPY_VILLAGER.getId());
                    }
                }
            }else {
                //Ha van, és bard item akkor
                ItemStack main = bardplayer.getItemInHand();
                Bard_Item item = findBardItem(main);
                if (item != null) {
                    //Végig megyünk az összes frakciótárson a közelbe
                    for (Player p : getFactionMembersInDistance(bardplayer, 10)) {
                        PotionEffectType potion = item.effect;
                        p.addPotionEffect(new PotionEffect(potion, 180, 0, false, false));
                        if(enableEffects && !bardplayer.isSneaking())
                            Shapes.DrawCircle(p, bardplayer.getLocation(), 10, 2, Effect.HAPPY_VILLAGER);
                    }
                    //bardplayer.getWorld().playEffect(new Location(bardplayer.getWorld(),bardplayer.getLocation().getBlockX(),bardplayer.getLocation().getBlockY(),bardplayer.getLocation().getBlockZ()), Effect.HAPPY_VILLAGER,Effect.HAPPY_VILLAGER.getId());
                }
            }
        }
    }

    private String getPotionName(String name) {
        name = name.replace("_", " ");
        return name.substring(0, 1).toUpperCase()
                + name.substring(1).toLowerCase();
    }

    private Bard_Item findBardItem(ItemStack stack) {
        for (Bard_Item item : bard_items) {
            if (item.mat.equals(stack.getType())) {
                return item;
            }
        }
        return null;
    }

    public void OhLetsBreakItDown(Player bardplayer) {
        if (boostEnabled) {

            ItemStack main = bardplayer.getItemInHand();
            Bard_Item item;
            if (main != null) {
                item = findBardItem(main);
                if (item != null) {
                    HCFPlayer hcf = HCFPlayer.getPlayer(bardplayer);
                    double currentEnergy = hcf.getBardEnergy();
                    if ((currentEnergy - item.cost) < 0) {
                        bardplayer.sendMessage(Messages.bard_dont_have_enough_energy.language(bardplayer).setAmount(String.valueOf(item.cost)).queue());
                        return;
                    }
                    if (Timers.BARD_COOLDOWN.has(bardplayer)) {
                        return;
                    }
                    main.setAmount(main.getAmount() - 1);
                    bardplayer.getInventory().setItemInHand(main);
                    hcf.setBardEnergy(currentEnergy - item.cost);
                    for (Player p : getFactionMembersInDistance(bardplayer, 15)) {
                        PotionEffectType potion = item.effect;
                        if (!bardplayer.isSneaking())
                            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                        p.getActivePotionEffects().forEach(potionEffect -> {
                            if (potionEffect.getType().equals(potion)) {
                                p.removePotionEffect(potion);
                                p.addPotionEffect(new PotionEffect(potion, 180, potionEffect.getAmplifier() + 1, false, false));
                            }
                        });
                    }
                    Timers.BARD_COOLDOWN.add(bardplayer);
                    new BukkitRunnable() {
                        private final Location loc = bardplayer.getLocation();
                        private int counts = 0;

                        @Override
                        public void run() {

                            if (bardplayer.isSneaking() || !enableEffects) {
                                cancel();
                                return;
                            }
                            if (counts + 1 > 15)
                                cancel();
                            counts++;
                            for (Player teammate : getFactionMembersInDistance(bardplayer, 15))
                                Shapes.DrawCircle(teammate, loc, counts, 2, Effect.HAPPY_VILLAGER);
                        }
                    }.runTaskTimer(m, 0L, 0L);

                    bardplayer.sendMessage(Messages.bard_used_powerup
                            .setAmount(String.valueOf(item.cost))
                            .setBardEffects(
                                    bardplayer,
                                    getPotionName(item.effect.getName()),
                                    String.valueOf(getFactionMembersInDistance(bardplayer, 15).size())
                            )
                            .queue()
                    );
                }
            }
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
            return helmetType == Material.GOLD_HELMET && chestplateType == Material.GOLD_CHESTPLATE && leggingsType == Material.GOLD_LEGGINGS && bootsType == Material.GOLD_BOOTS;
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
        hcf.setPlayerClass(Classes.BARD);
        hcf.setBardEnergy(0D);
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
        hcf.setBardEnergy(0D);
        Scoreboards.refresh(p);
    }

    private static class Bard_Item {
        Material mat;
        PotionEffectType effect;
        int cost;

        public Bard_Item(Material mat, PotionEffectType type, int cost) {
            this.mat = mat;
            this.effect = type;
            this.cost = cost;
        }
    }
}
