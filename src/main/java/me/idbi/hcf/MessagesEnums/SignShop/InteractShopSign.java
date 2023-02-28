package me.idbi.hcf.MessagesEnums.SignShop;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
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
    public static boolean isFull(Player p, ItemStack is) {
        return !p.getInventory().addItem(is).isEmpty();
    }

    private static int remainingSpace(Player p, ItemStack item) {
        int amount = 0;
        for (ItemStack is : p.getInventory().getContents()) {
            if (is == null) continue;
            if (is.isSimilar(item)) {
                if (is.getAmount() < 64) {
                    amount += 64 - is.getAmount();
                }
            }
        }
        return amount;
    }

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
                if (line1.matches("^[0-9]+$")) {

                    int amount = Integer.parseInt(line1);
                    String line2 = sign.getLine(2);
                    String line3 = sign.getLine(3);

                    if (line3.matches("^[0-9$]+$")) {
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

                            int remainingSpace = remainingSpace(p, new ItemStack(material, amount, Short));
                            int signPrice = Integer.parseInt(line3.replace("$", ""));
                            int fertigPreise = (signPrice / amount) * remainingSpace;
                            int playerBalance = Playertools.getPlayerBalance(p);

                            Scoreboards.refresh(p);
                            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
                            if (remainingSpace < amount && remainingSpace > 0) {
                                if (isFull(p, new ItemStack(material, remainingSpace, Short))) {
                                    p.sendMessage(Messages.not_enough_slot.language(p).queue());
                                    return;
                                }
                                if (playerBalance < fertigPreise) {
                                    p.sendMessage(Messages.not_enough_money.language(p).queue());
                                    return;
                                }

                                Playertools.setPlayerBalance(p, playerBalance - fertigPreise);
                                PlayerStatistic stat = hcfPlayer.playerStatistic;
                                stat.MoneySpend += fertigPreise;
                                p.sendMessage(Messages.sign_shop_bought.language(p).setItem(new ItemStack(material, remainingSpace, Short))
                                        .setPrice(fertigPreise)
                                        .setAmount(String.valueOf(remainingSpace))
                                        .queue());
                                Scoreboards.refresh(p);
                                return;
                            }

                            if (isFull(p, new ItemStack(material, amount, Short))) {
                                p.sendMessage(Messages.not_enough_slot.language(p).queue());
                                return;
                            }

                            if (playerBalance >= signPrice) {
                                p.sendMessage(Messages.sign_shop_bought.language(p)
                                        .setItem(new ItemStack(material, amount, Short))
                                        .setPrice(signPrice)
                                        .setAmount(String.valueOf(amount))
                                        .queue());
                                Playertools.setPlayerBalance(p, playerBalance - signPrice);
                                PlayerStatistic stat = hcfPlayer.playerStatistic;
                                stat.MoneySpend += signPrice;
                            }

                            Scoreboards.refresh(p);
                        } catch (NullPointerException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            //
            else if (line0.equals("§c[Sell]")) {
                String line1 = sign.getLine(1); // amount
                if (line1.matches("^[0-9]+$")) {

                    int amount = Integer.parseInt(line1);
                    String line2 = sign.getLine(2);
                    String line3 = sign.getLine(3);

                    if (line3.matches("^[0-9$]+$")) {
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

                            int price = Integer.parseInt(line3.replace("$", ""));

                            if (p.getInventory().contains(material)) {
                                p.getInventory().removeItem(new ItemStack(material, amount, Short));
                                p.sendMessage(Messages.sign_shop_sold.language(p)
                                        .setPrice(price)
                                        .setAmount(line1)
                                        .setItem(new ItemStack(material, amount, Short))
                                        .queue()
                                );

                                HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
                                Scoreboards.refresh(p);
                                hcfPlayer.addMoney(price);
                                PlayerStatistic stat = hcfPlayer.playerStatistic;
                                stat.MoneyEarned += price;
                            } else {
                                p.sendMessage(Messages.dont_have_item.language(p).queue());
                            }
                            Scoreboards.refresh(p);
                        } catch (NullPointerException | IllegalArgumentException ex) {
                        }
                    }
                }
            }
        }
    }
}
