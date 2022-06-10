package me.idbi.hcf.SignShop;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class
CreateShopSign implements Listener {

    private static final ArrayList<String> list = new ArrayList<String>() {{
        add("end portal frame");
        add("end portal fram");
        add("end_portal_frame");
        add("end_portal_fram");
    }};

    private static boolean successfullySign(String line2) {
        Material material;
        short Short = 0;
        try {
            if (line2.matches("^[0-9]+$"))
                material = Material.getMaterial(Integer.parseInt(line2));
            else if (line2.matches("^[0-9:]+$") && line2.contains(":")) {
                material = Material.matchMaterial(line2.split(":")[0]);
                Short = java.lang.Short.parseShort(line2.split(":")[1]);
            } else
                material = Material.matchMaterial(line2);

            ItemStack is = new ItemStack(material, 1, Short);
            return true;
        } catch (NullPointerException | IllegalArgumentException ex) {
            return false;
        }
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent e) {
        String line0 = e.getLine(0);

        if (line0 != null) {
            if (line0.equalsIgnoreCase("[buy]")) {
                if (!Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "adminDuty"))) {
                    e.getPlayer().sendMessage(Messages.NO_PERMISSION.queue());
                    return;
                }
                if (!successfullySign(e.getLine(1))) {
                    e.setCancelled(true);
                    return;
                }

                if (!e.getLine(1).matches("^[0-9]+$")) {
                    e.setCancelled(true);
                    return;
                }
                if (list.contains(e.getLine(2).toLowerCase())) {
                    e.setLine(2, "120");
                }
                if (!e.getLine(3).startsWith("$") && e.getLine(3).matches("^[0-9]+$")) {
                    e.setLine(3, "$" + e.getLine(3));
                    return;
                }
                e.setLine(0, "§a[Buy]");
            }
            if (line0.equalsIgnoreCase("[sell]")) {
                if (!Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "adminDuty"))) {
                    e.getPlayer().sendMessage(Messages.NO_PERMISSION.queue());
                    return;
                }
                e.setLine(0, "§c[Sell]");
                if (!e.getLine(1).matches("^[0-9]+$")) {
                    e.setCancelled(true);
                    return;
                }

                if (list.contains(e.getLine(2).toLowerCase())) {
                    e.setLine(2, "120");
                }

                if (!e.getLine(3).startsWith("$") && e.getLine(3).matches("^[0-9]+$")) {
                    e.setLine(3, "$" + e.getLine(3));
                    return;
                }
            }
        }
    }
}
