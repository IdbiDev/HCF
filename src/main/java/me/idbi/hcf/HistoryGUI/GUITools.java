package me.idbi.hcf.HistoryGUI;

import me.idbi.hcf.tools.CenterTools;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUITools {

    public static List<String> setupPageLore(int currentPage, List<String> message, int maxPage) {
        try {
            message = message.subList(PageSub.getSubs(currentPage)[0], PageSub.getSubs(currentPage)[1]);
        } catch (IndexOutOfBoundsException e) {
            message = message.subList(PageSub.getSubs(currentPage)[0], message.size());
        }

        message.add("§5");
        message.add(getProgress((currentPage), maxPage));
        return message;
    }

    public static String getPageText(String message, int CENTER_PX){
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()) {
            if(c == '§') {
                previousCode = true;
                continue;
            } else if(previousCode == true) {
                previousCode = false;
                if(c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                CenterTools.DefaultFontInfo dFI = CenterTools.DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = CenterTools.DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb.toString() + message;
    }

    private static final String cube = "■";
    private static final String arrowLeft = "«";
    private static final String arrowRight = "»";

    public static String getProgress(int currentPage, int maxPage) {
        String str = "";

        for (int i = 1; i <= maxPage; i++) {
            if(i > 5) break;
            if(currentPage == i) {
                str += "§a" + cube;
                continue;
            }
            str += "§7" + cube;
        }

        return "§7- §fPage " + currentPage + " §7§l➸ "+ str;
    }

    public static int getCenterPX(String highestLengthString) {
        int finalInt = 0;
        for (char c : highestLengthString.toCharArray()) {
            finalInt += CenterTools.DefaultFontInfo.getDefaultFontInfo(c).getLength();
        }

        return finalInt;
    }

    public static int getPage(ItemStack is) {
        if(is == null) return 1;
        if(!is.hasItemMeta()) return 1;
        if(!is.getItemMeta().hasLore()) return 1;

        String c = is.getItemMeta().getLore().get(is.getItemMeta().getLore().size() - 1).substring(11, 12);
        if(c.matches("^[0-9]+$")) {
            return Integer.parseInt(c);
        }
        return 1;
    }

    public static enum PageSub {

        NUM_0(0, 0),
        NUM_1(0, 10),
        NUM_2(10, 20),
        NUM_3(20, 30),
        NUM_4(30, 40),
        NUM_5(40, 50);

        public int min, max;

        PageSub(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public static int[] getSubs(int currentPage) {
            try {
                return new int[]{
                        valueOf("NUM_" + currentPage).min,
                        valueOf("NUM_" + currentPage).max
                };
            } catch (IllegalArgumentException e) {
                return new int[]{NUM_5.min, NUM_5.max};
            }
        }
    }
}
