package me.idbi.hcf.tools;



import me.idbi.hcf.Main;
import me.idbi.hcf.commands.faction;

import java.util.Objects;

public class setupCommands {
    public static void setupCommands() {
        Objects.requireNonNull(Main.getPlugin(Main.class).getCommand("faction")).setExecutor(new faction());
    }
}
