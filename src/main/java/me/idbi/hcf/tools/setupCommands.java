package me.idbi.hcf.tools;


import me.idbi.hcf.Main;
import me.idbi.hcf.commands.admin;
import me.idbi.hcf.commands.faction;
import me.idbi.hcf.commands.fc_position;
import me.idbi.hcf.commands.koth;

import java.util.Objects;

public class setupCommands {
    public static void setupCommands() {
        Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("faction")).setExecutor(new faction());
        Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("admin")).setExecutor(new admin());
        Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("fc")).setExecutor(new fc_position());
        Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("koth")).setExecutor(new koth());
        //Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("enchantment")).setExecutor(new EnchantmentCommands());
        //Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("addenchant")).setExecutor(new ECommand_AddEnchant());
        //Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("removeenchant")).setExecutor(new ECommand_RemoveEnchant());
        //Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("enchantbook")).setExecutor(new ECommand_EnchantBook());
        //Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("enchantinfo")).setExecutor(new ECommand_EnchantInfo());
        //Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("changelore")).setExecutor(new EnchantmentCommands());
    }
}
