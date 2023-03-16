package me.idbi.hcf.TabManager.TabAPIv2;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class TabObject47 {
    HashMap<Integer, TabHolder47> tabs = new HashMap<>();

    public void setPriority(Plugin p, int pri) {
        for (int a = -1; a < 4; a++) {
            if (this.tabs.get(Integer.valueOf(a)) != null &&
                    ((TabHolder47)this.tabs.get(Integer.valueOf(a))).p == p)
                this.tabs.put(Integer.valueOf(a), null);
        }
        if (pri > -2) {
            TabHolder47 t = new TabHolder47(p);
            this.tabs.put(Integer.valueOf(pri), t);
        }
    }

    public TabHolder47 getTab() {
        int a = 3;
        while (this.tabs.get(Integer.valueOf(a)) == null && a > -3)
            a--;
        if (a == -2)
            return new TabHolder47(null);
        return this.tabs.get(Integer.valueOf(a));
    }

    public void setTab(Plugin plugin, int x, int y, String msg, int ping, WrappedGameProfile gameProfile) {
        int a = -1;
        while ((this.tabs.get(Integer.valueOf(a)) == null || ((TabHolder47)this.tabs
                .get(Integer.valueOf(a))).p != plugin) && a < 3)
            a++;
        if (a == 3 && (
                this.tabs.get(Integer.valueOf(a)) == null || ((TabHolder47)this.tabs
                        .get(Integer.valueOf(a))).p != plugin)) {
            setPriority(plugin, 0);
            a = 0;
        }
        TabHolder47 t = this.tabs.get(Integer.valueOf(a));
        t.tabs[y][x] = msg;
        t.tabPings[y][x] = ping;
        t.tabGameProfiles[y][x] = gameProfile;
        t.maxh = TabAPI.getHorizSize(47);
        t.maxv = Math.max(x + 1, t.maxv);
    }
}
