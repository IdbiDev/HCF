package me.idbi.hcf.Elevator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class ElevatorInteract implements Listener {

    private static Location getUpperLoc(Block block) {
        Location blockLoc = block.getLocation();
        for (int i = 0; i < 256; i++) {
            if (blockLoc.add(0, 1, 0).getBlock() != null) {
                Block signLoc = blockLoc.getBlock();
                if (signLoc.getType().name().contains("SIGN")) {
                    boolean wallsign = signLoc.getType().equals(Material.WALL_SIGN);

                    Sign sign = (Sign) signLoc.getState();
                    org.bukkit.material.Sign signMaterial = (org.bukkit.material.Sign) sign.getData();
                    BlockFace blockFace = signMaterial.getFacing();

                    if (sign.getLine(0) == null) continue;
                    if (sign.getLine(1) == null) continue;

                    if (sign.getLine(0).equalsIgnoreCase("§9[elevator]")) {

                        Location returnLoc = null;
                        if (sign.getLine(1).equalsIgnoreCase("down")) {
                            if (wallsign) {
                                if (signLoc.getLocation().add(0, -2, 0).getBlock() != null) {
                                    if (signLoc.getLocation().add(0, -2, 0).getBlock().getType() != Material.AIR) {
                                        returnLoc = signLoc.getLocation().add(0, -1, 0);

                                    } else returnLoc = signLoc.getLocation();
                                } else returnLoc = signLoc.getLocation();
                            } else
                                returnLoc = signLoc.getLocation();

                            returnLoc.setPitch(0);
                            switch (blockFace) {
                                case NORTH:
                                    returnLoc.setYaw(0);
                                    break;
                                case EAST:
                                    returnLoc.setYaw(90F);
                                    break;
                                case WEST:
                                    returnLoc.setYaw(-90F);
                                    break;
                                case SOUTH:
                                    returnLoc.setYaw(180F);
                                    break;
                            }
                            return returnLoc;
                        }
                    }
                }
            }
        }

        return null;
    }

    private static Location getLowerLocation(Block block) {
        Location blockLoc = block.getLocation();
        for (int i = 0; i < 256; i++) {
            if (blockLoc.add(0, -1, 0).getBlock() != null) {
                Block signLoc = blockLoc.getBlock();
                if (signLoc.getType().name().contains("SIGN")) {
                    boolean wallsign = signLoc.getType().equals(Material.WALL_SIGN);

                    Sign sign = (Sign) signLoc.getState();
                    org.bukkit.material.Sign signMaterial = (org.bukkit.material.Sign) sign.getData();
                    BlockFace blockFace = signMaterial.getFacing();

                    if (sign.getLine(0) == null) continue;
                    if (sign.getLine(1) == null) continue;

                    if (sign.getLine(0).equalsIgnoreCase("§9[elevator]")) {
                        if (sign.getLine(1).equalsIgnoreCase("up")) {
                            Location returnLoc = null;
                            if (wallsign) {
                                if (signLoc.getLocation().add(0, -2, 0).getBlock() != null) {
                                    if (signLoc.getLocation().add(0, -2, 0).getBlock().getType() != Material.AIR) {
                                        returnLoc = signLoc.getLocation().add(0, -1, 0);

                                    } else returnLoc = signLoc.getLocation();
                                } else returnLoc = signLoc.getLocation();
                            } else
                                returnLoc = signLoc.getLocation();

                            returnLoc.setPitch(0);
                            switch (blockFace) {
                                case NORTH:
                                    returnLoc.setYaw(0);
                                    break;
                                case EAST:
                                    returnLoc.setYaw(90F);
                                    break;
                                case WEST:
                                    returnLoc.setYaw(-90F);
                                    break;
                                case SOUTH:
                                    returnLoc.setYaw(180F);
                                    break;
                            }
                            return returnLoc;
                        }
                    }
                }
            }
        }
        return null;
    }

    @EventHandler
    public void onInteractElevator(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && b != null) {
            if (!b.getType().name().contains("SIGN")) return;

            Sign sign = (Sign) b.getState();

            String line0 = sign.getLine(0);
            String line1 = sign.getLine(1);

            if (line0.equals("§9[Elevator]")) {
                try {
                    if (line1.equalsIgnoreCase("up")) {
                        p.teleport(getUpperLoc(b).add(0.5, 0, 0.5));
                    } else if (line1.equalsIgnoreCase("down")) {
                        p.teleport(getLowerLocation(b).add(0.5, 0, 0.5));
                    }
                } catch (NullPointerException ex) {
                }
            }
        }
    }
}
