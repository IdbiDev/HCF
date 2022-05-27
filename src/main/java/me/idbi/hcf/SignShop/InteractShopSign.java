package me.idbi.hcf.SignShop;

import me.idbi.hcf.MessagesEnums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractShopSign implements Listener {

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && b != null) {
            if (!b.getType().name().contains("SIGN")) return;

            Sign sign = (Sign) b.getState();

            String line0 = sign.getLine(0);

            if (line0.equals("§a[Buy]")) {
                String line1 = sign.getLine(1); // amount
                if(line1.matches("^[0-9]+$")) {

                    int amount = Integer.parseInt(line1);
                    String line2 = sign.getLine(2);
                    String line3 = sign.getLine(3);

                    if(line3.matches("^[0-9$]+$")) {
                        Material material;
                        short Short = 0;
                        try {
                            if (line2.matches("^[0-9]+$"))
                                material = Material.getMaterial(Integer.parseInt(line2));

                            else if(line2.matches("^[0-9:]+$") && line2.contains(":")) {
                                material = Material.matchMaterial(line2.split(":")[0]);
                                Short = java.lang.Short.parseShort(line2.split(":")[1]);
                            }
                            else
                                material = Material.matchMaterial(line2);

                            Bukkit.broadcastMessage(String.valueOf(remainingSpace(p, new ItemStack(material, amount, Short), amount)));
                            if (SignShopLibrary.isFull(p, new ItemStack(material, amount, Short))) {
                                p.sendMessage(Messages.NOT_ENOUGH_SLOT.queue());
                            }
                        } catch (NullPointerException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            //
            else if (line0.equals("§c[Sell]")) {
                String line1 = sign.getLine(1); // amount
                if(line1.matches("^[0-9]+$")) {

                    int amount = Integer.parseInt(line1);
                    String line2 = sign.getLine(2);
                    String line3 = sign.getLine(3);

                    if (line3.matches("^[0-9$]+$")) {
                        Material material;
                        short Short = 0;
                        try {
                            if (line2.matches("^[0-9]+$"))
                                material = Material.getMaterial(Integer.parseInt(line2));
                            else if(line2.matches("^[0-9:]+$") && line2.contains(":")) {
                                material = Material.matchMaterial(line2.split(":")[0]);
                                Short = java.lang.Short.parseShort(line2.split(":")[1]);
                            }
                            else
                                material = Material.matchMaterial(line2);

                            if (p.getInventory().contains(material)) {
                                p.getInventory().removeItem(new ItemStack(material, amount, Short));
                            } else {
                                p.sendMessage(Messages.DONT_HAVE_ITEM.queue());
                            }
                        } catch (NullPointerException | IllegalArgumentException ex) {
                        }
                    }
                }
            }
        }
    }

    private static int remainingSpace(Player p, ItemStack item, int buyerAmount) {
        int amount = 0;
        for(ItemStack is : p.getInventory().getContents()) {
            if(is == null) continue;
            if(is.isSimilar(item)) {
                amount += is.getAmount();
            }
        }
        // ToDo: ki kell számolni, hogy mennyi a maradék helye a blocknak
        return 0;
    }
}
