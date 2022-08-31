package me.idbi.hcf.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class onSignPlace implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(event.getPlayer().getItemInHand().getType() == Material.SIGN) {
            if(event.getItem() == null) return;
            if(!event.getItem().hasItemMeta()) return;
            if(event.getItem().getItemMeta().hasDisplayName())
                if (!event.getItem().getItemMeta().getDisplayName().startsWith("§cDeath Sign"))
                    return;

            event.setCancelled(true);
            Block block = event.getClickedBlock().getRelative(event.getBlockFace());

            byte data = 0;
            if(event.getBlockFace() == BlockFace.NORTH) {
                data = 2;
            }
            else if(event.getBlockFace() == BlockFace.SOUTH) {
                data = 3;
            }
            else if(event.getBlockFace() == BlockFace.WEST) {
                data = 4;
            }
            else if(event.getBlockFace() == BlockFace.EAST) {
                data = 5;
            }

            //Bukkit.broadcastMessage(event.getBlockFace().name());
            if(event.getBlockFace() == BlockFace.UP || event.getBlockFace() == BlockFace.DOWN) {
                block.setType(Material.SIGN_POST);
                block.setData(getDirection(event.getPlayer()));
            } else {
                block.setType(Material.WALL_SIGN);
                block.setData(data);
            }


            BlockState state = block.getState();
            if(state instanceof Sign sign) {
                List<String> lore = event.getItem().getItemMeta().getLore();
                if(lore == null) return;
                if(lore.isEmpty()) return;

                int counter = 0;
                for (String s : lore) {
                    if(s.equals("§5")) continue;
                    if(counter > 3) break;
                    sign.setLine(counter, s.replace("§f", "§0"));
                    counter++;
                }
                sign.update(true);
                block.getState().update(true, true);

                removeItem(event.getPlayer(), 1);
            }
        }
    }

    public static void removeItem(Player p, int amount) {
        ItemStack is = p.getItemInHand();
        is.setAmount(Math.max((is.getAmount() - amount), 0));
        p.setItemInHand(is);
    }

    public static ItemStack deathSign(Player killer, Player victim) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        ItemStack is = new ItemStack(Material.SIGN);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cDeath Sign §f[§7" + victim.getName() + "§f]");
        im.setLore(Arrays.asList(
                "§5",
                "§c" + victim.getName(),
                "§fslain by",
                "§a" + killer.getName(),
                "§f" + formatter.format(new Date())
        ));
        is.setItemMeta(im);
        return is;
    }

    private byte getDirection(Player player) {
        double rotation = (player.getLocation().getYaw()) % 360;
        if (rotation < 0) {
            rotation += 360.0;
            if (0 <= rotation && rotation < 22.5) {
                //return BlockFace.NORTH;
                return 8;
            }
            if (22.5 <= rotation && rotation < 67.5) {
                //return BlockFace.NORTH_EAST;
                return 10;
            }
            if (67.5 <= rotation && rotation < 112.5) {
                //return BlockFace.EAST;
                return 12;
            }
            if (112.5 <= rotation && rotation < 157.5) {
                //return BlockFace.SOUTH_EAST;
                return 14;
            }
            if (157.5 <= rotation && rotation < 202.5) {
               // return BlockFace.SOUTH;
                return 0;
            }
            if (202.5 <= rotation && rotation < 247.5) {
                //return BlockFace.SOUTH_WEST;
                return 2;
            }
            if (247.5 <= rotation && rotation < 292.5) {
                //return BlockFace.WEST;
                return 4;
            }
            if (292.5 <= rotation && rotation < 337.5) {
                //return BlockFace.NORTH_WEST;
                return 6;
            }
            if (337.5 <= rotation && rotation < 359) {
                //return BlockFace.NORTH;
                return 8;
            }
        }
        return 8;
    }
}
