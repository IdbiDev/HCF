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
    }
}
