package me.idbi.hcf.TabManager.TabAPIv2;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.plugin.Plugin;

public class TabHolder47 {

    int maxv = 0;

    int maxh = 0;

    WrappedGameProfile[][] tabGameProfiles;

    int[][] tabPings;

    String[][] tabs;

    Plugin p;

    public TabHolder47(Plugin p) {
        this.p = p;
        this.tabs = new String[TabAPI.getHorizSize(47)][TabAPI.getVertSize(47)];
        this.tabPings = new int[TabAPI.getHorizSize(47)][TabAPI.getVertSize(47)];
        this.tabGameProfiles =
                new WrappedGameProfile[TabAPI.getHorizSize(47)][TabAPI.getVertSize(47)];
        this.maxh = TabAPI.getHorizSize(47);
        this.maxv = TabAPI.getVertSize(47);
        for (int b = 0; b < this.maxv; b++) {
            for (int a = 0; a < this.maxh; a++) {
                this.tabs[a][b] = " ";
                this.tabPings[a][b] = 9999;
                this.tabGameProfiles[a][b] = null;
            }
        }
    }

    public TabHolder47 getCopy() {
        TabHolder47 newCopy = new TabHolder47(this.p);
        newCopy.tabs = copyStringArray(this.tabs);
        newCopy.tabPings = copyIntArray(this.tabPings);
        return newCopy;
    }

    private static String[][] copyStringArray(String[][] tab) {
        int horzTabSize = TabAPI.getHorizSize(47);
        int vertTabSize = TabAPI.getVertSize(47);
        String[][] temp = new String[horzTabSize][vertTabSize];
        for (int b = 0; b < vertTabSize; b++) {
            for (int a = 0; a < horzTabSize; a++)
                temp[a][b] = tab[a][b];
        }
        return temp;
    }

    private static int[][] copyIntArray(int[][] tab) {
        int horzTabSize = TabAPI.getHorizSize(47);
        int vertTabSize = TabAPI.getVertSize(47);
        int[][] temp = new int[horzTabSize][vertTabSize];
        for (int b = 0; b < vertTabSize; b++) {
            for (int a = 0; a < horzTabSize; a++)
                temp[a][b] = tab[a][b];
        }
        return temp;
    }
}
