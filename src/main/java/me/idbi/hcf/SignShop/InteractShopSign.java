package me.idbi.hcf.SignShop;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
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
        int amount = 0;
        for (ItemStack is2 : p.getInventory().getContents()) {
            if (is2 == null) return false;
            if (is2.isSimilar(is)) {
                ///      56 +16          < 64
                if (is2.getAmount() >= 64) continue;
                //if (is2.getAmount() + is.getAmount() <= 64) {
                amount += is2.getAmount();
            }
        }
        return amount % 64 == 0;
        //return !p.getInventory().addItem(is).isEmpty();
    }

    private static int remainingSpace(Player p, ItemStack item) {
        int amount = 0;
        for (ItemStack is : p.getInventory().getContents()) {
            if (is == null) return 0;
            if (is.isSimilar(item)) {
                if (is.getAmount() < 64) {
                    amount += 64 - is.getAmount();
                }
            }
        }
        return amount;
    }

    private static int remainingAmount(Player p, ItemStack item) {
        int amount = 0;
        for (ItemStack is : p.getInventory().getContents()) {
            if (is == null) continue;
            if (is.isSimilar(item)) {
                amount += is.getAmount();
            }
        }
        return amount;
    }

    private String sellName = Config.SignShopTitle_Sell.asStr();
    private String buyName = Config.SignShopTitle_Buy.asStr();

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && b != null) {
            if (!b.getType().name().contains("SIGN")) return;

            Sign sign = (Sign) b.getState();

            String line0 = sign.getLine(0); // [buy]

            if(!Config.SignShopEnabled.asBoolean()) return;
            if (line0.equals(buyName)) {
                if(!HCFPermissions.signshop_use.check(e.getPlayer())) return;
                String line1 = sign.getLine(1); // darab
                if (line1.matches("^[0-9]+$")) {

                    String line2 = sign.getLine(2); // item
                    int amount = Integer.parseInt(line1);
                    String line3 = sign.getLine(3); // ára

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

                            // System.out.println(material);
                            int remainingSpace = remainingSpace(p, new ItemStack(material, amount, Short));
                            int signPrice = Integer.parseInt(line3.replace("$", ""));
                            int price = signPrice;
                            int playerBalance = Playertools.getPlayerBalance(p);

                            Scoreboards.refresh(p);
                            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
                            ItemStack is = new ItemStack(material, amount, Short);
                            if (remainingSpace < amount && remainingSpace > 0) {
                                is = new ItemStack(material, remainingSpace, Short);
                                if (isFull(p, is)) {
                                    System.out.println("1");
                                    p.sendMessage(Messages.not_enough_slot.language(p).queue());
                                    return;
                                }
                                price = (signPrice / amount) * remainingSpace;
                                amount = remainingSpace;
                                if (playerBalance < price) {
                                    p.sendMessage(Messages.not_enough_money.language(p).queue());
                                    return;
                                }
                            } else {
                                if (isFull(p, is)) {
                                    p.sendMessage(Messages.not_enough_slot.language(p).queue());
                                    return;
                                }
                                if (playerBalance < signPrice) {
                                    p.sendMessage(Messages.not_enough_money.language(p).queue());
                                    return;
                                }
                            }

                            p.getInventory().addItem(is);
                            p.sendMessage(Messages.sign_shop_bought.language(p)
                                    .setItem(is)
                                    .setPrice(price)
                                    .setAmount(String.valueOf(amount))
                                    .queue());

                            hcfPlayer.removeMoney(price);
                            PlayerStatistic stat = hcfPlayer.getPlayerStatistic();
                            stat.MoneySpend += price;

                            /*if (playerBalance >= signPrice) {
                            } else {

                            }*/

                            Scoreboards.refresh(p);
                        } catch (NullPointerException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            //
            else if (line0.equals(sellName)) {
                if(!HCFPermissions.signshop_use.check(e.getPlayer())) return;
                String line1 = sign.getLine(1);
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


                            if (p.getInventory().contains(material)) {
                                int signPrice = Integer.parseInt(line3.replace("$", ""));
                                int price = signPrice;
                                int remainingAmount = remainingAmount(p, new ItemStack(material, amount, Short));

                                ItemStack is = new ItemStack(material, amount, Short);
                                if(remainingAmount < amount) {
                                    price = (int) Math.round(((double) remainingAmount / (double) amount) * signPrice);
                                    is = new ItemStack(material, remainingAmount, Short);
                                    amount = remainingAmount;
                                }
                                p.getInventory().removeItem(is);

                                p.sendMessage(Messages.sign_shop_sold.language(p)
                                        .setPrice(price)
                                        .setAmount(amount + "")
                                        .setItem(is)
                                        .queue()
                                );


                                HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
                                hcfPlayer.addMoney(price);
                                Scoreboards.refresh(p);
                                PlayerStatistic stat = hcfPlayer.getPlayerStatistic();
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
