package me.idbi.hcf.classes;

import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;


public class ClassSelector {
    public static void addClassToPlayer(Player player) {
        try {
            if (Archer.CheckArmor(player)) {
                if (!playertools.getMetadata(player, "class").equalsIgnoreCase("Archer")) {
                    if (Main.debug)
                        Main.sendCmdMessage("Equiping Archerclass to " + player.getDisplayName());
                    Archer.setEffect(player);
                    Scoreboards.refresh(player);
                }
            } else if (playertools.getMetadata(player, "class").equals("Archer")) {
                if (Main.debug)
                    Main.sendCmdMessage("Removing Archerclass from " + player.getDisplayName());
                Archer.removeEffects(player);
                playertools.setMetadata(player, "class", "None");
                Scoreboards.refresh(player);
            }

            if (Bard.CheckArmor(player)) {
                if (!playertools.getMetadata(player, "class").equalsIgnoreCase("Bard")) {
                    if (Main.debug)
                        Main.sendCmdMessage("Equiping Bard class to " + player.getDisplayName());
                    Bard.setEffect(player);
                    Scoreboards.refresh(player);
                }
            } else if (playertools.getMetadata(player, "class").equalsIgnoreCase("Bard")) {
                if (Main.debug)
                    Main.sendCmdMessage("Removing Bard class from " + player.getDisplayName());
                Bard.removeEffects(player);
                playertools.setMetadata(player, "class", "None");
                Scoreboards.refresh(player);
            }

            if (Miner.CheckArmor(player)) {
                if (!playertools.getMetadata(player, "class").equalsIgnoreCase("Miner")) {
                    if (Main.debug)
                        Main.sendCmdMessage("Equiping Miner class to " + player.getDisplayName());
                    Miner.setEffect(player);
                    Scoreboards.refresh(player);
                }
            } else if (playertools.getMetadata(player, "class").equalsIgnoreCase("Miner")) {
                if (Main.debug)
                    Main.sendCmdMessage("Removing Miner class from " + player.getDisplayName());
                Miner.removeEffects(player);
                playertools.setMetadata(player, "class", "None");
                Scoreboards.refresh(player);
            }

            if (Assassin.CheckArmor(player)) {
                if (!playertools.getMetadata(player, "class").equalsIgnoreCase("Assassin")) {
                    if (Main.debug)
                        Main.sendCmdMessage("Equiping Assassin class to " + player.getDisplayName());
                    Assassin.setEffect(player);
                    Scoreboards.refresh(player);
                }
            } else if (playertools.getMetadata(player, "class").equalsIgnoreCase("Assassin")) {
                if (Main.debug)
                    Main.sendCmdMessage("Removing Assassin class from " + player.getDisplayName());
                Assassin.removeEffects(player);
                playertools.setMetadata(player, "class", "None");
                Scoreboards.refresh(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

