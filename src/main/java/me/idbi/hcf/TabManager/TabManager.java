package me.idbi.hcf.TabManager;

import com.keenant.tabbed.item.TabItem;
import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.TableTabList;
import me.idbi.hcf.CustomFiles.TabFile;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabManager {

    private Main plugin;
    private static TabManager instance;
    private boolean enabled;
    private int factionInfo_startSlot;
    private ArrayList<String> factionInfo_noFaction;
    private ArrayList<String> factionInfo_inFaction;

    private int playerInfo_startSlot;
    private String playerInfo_factionNameColor;
    private String playerInfo_playerColor;

    public TabManager(Main plugin) {
        this.plugin = plugin;
        instance = this;
        reload();
    }

    public ArrayList<String> getLeft() {
        return translate(TabFile.get().getStringList("left"));
    }

    public ArrayList<String> getCenter() {
        return translate(TabFile.get().getStringList("center"));
    }

    public ArrayList<String> getRight() {
        return translate(TabFile.get().getStringList("right"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getFactionInfoStartSlot() {
        return factionInfo_startSlot;
    }

    public ArrayList<String> getNoFactionInfo() {
        return factionInfo_noFaction;
    }

    public ArrayList<String> getInFactionInfo() {
        return factionInfo_inFaction;
    }

    public int getPlayerInfoStartSlot() {
        return playerInfo_startSlot;
    }

    public String getPlayerInfoFactionNameColor() {
        return playerInfo_factionNameColor;
    }

    public String getPlayerInfoPlayerColor() {
        return playerInfo_playerColor;
    }

    public void reload() {
        TabFile.reload();
        enabled = TabFile.get().getBoolean("enabled");
        factionInfo_startSlot = TabFile.get().getInt("faction_info.start_slot") - 1;
        factionInfo_noFaction = translate(TabFile.get().getStringList("faction_info.no_faction"));
        factionInfo_inFaction = translate(TabFile.get().getStringList("faction_info.in_faction"));

        playerInfo_startSlot = TabFile.get().getInt("player_list.start_slot") - 1;
        playerInfo_factionNameColor = translate(TabFile.get().getString("player_list.faction_name_color"));
        playerInfo_playerColor = translate(TabFile.get().getString("player_list.player_color"));
    }

    public static TabManager getManager() {
        return instance;
    }

    private ArrayList<String> translate(List<String> list) {
        ArrayList<String> lista = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            lista.add(ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return lista;
    }

    private String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void refresh(Player p) {
        if (p == null) return;
        TableTabList tab;
        tab = Main.getInstance().getTabbed().getTabList(p) == null
                ? Main.getInstance().getTabbed().newTableTabList(p, 4)
                : (TableTabList) Main.getInstance().getTabbed().getTabList(p);
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);

        TableTabList.TableBox left = new TableTabList.TableBox(new TableTabList.TableCell(0, 0), new TableTabList.TableCell(0, 19));
        tab.fill(left, formatColumn(p, getLeft()), TableTabList.TableCorner.TOP_LEFT, TableTabList.FillDirection.VERTICAL);

        TableTabList.TableBox center = new TableTabList.TableBox(new TableTabList.TableCell(1, 0), new TableTabList.TableCell(1, 19));
        tab.fill(center, formatColumn(p, getCenter()), TableTabList.TableCorner.TOP_LEFT, TableTabList.FillDirection.VERTICAL);

        TableTabList.TableBox right = new TableTabList.TableBox(new TableTabList.TableCell(2, 0), new TableTabList.TableCell(2, 19));
        tab.fill(right, formatColumn(p, getRight()), TableTabList.TableCorner.TOP_LEFT, TableTabList.FillDirection.VERTICAL);

        int c = 0;
        if (hcfPlayer.inFaction()) {
            for (String s : this.factionInfo_inFaction) {
                tab.set(this.factionInfo_startSlot + c, new TextTabItem(formatLine(p, s)));
                c++;
            }
        } else {
            for (String s : this.factionInfo_noFaction) {
                tab.set(this.factionInfo_startSlot + c, new TextTabItem(formatLine(p, s)));
                c++;
            }
        }

        c = 0;
        if (hcfPlayer.inFaction()) {
            tab.set(this.playerInfo_startSlot, new TextTabItem(translate(this.playerInfo_factionNameColor) + hcfPlayer.getFaction().getName()));

            for (HCFPlayer player : hcfPlayer.getFaction().getMembers()) {
                tab.set(this.playerInfo_startSlot + c, new TextTabItem(translate(this.playerInfo_playerColor) + player.getName()));
                c++;
            }
        }
    }

    private ArrayList<TabItem> formatColumn(Player p, ArrayList<String> list) {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            output.add(formatLine(p, list.get(i)));
        }

        ArrayList<TabItem> items = new ArrayList<>();
        for (int i = 0; i < output.size(); i++) {
            items.add(new TextTabItem(output.get(i)));
        }
        return items;
    }

    private String formatLine(Player p, String s) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(hcfPlayer.inFaction()) {
            s = s
                    .replace("%dtr%", hcfPlayer.getFaction().getDTR() + "")
                    .replace("%home%", hcfPlayer.getFaction().getFormattedHomeLocation());
        }
        s = s
                .replace("%player%", hcfPlayer.getName())
                .replace("%location%", hcfPlayer.getLocationFormatted())
                .replace("%coordinates%", Playertools.formatLocation(p.getLocation()))
                .replace("%direction%", getDirection(p))
                .replace("%online%", Bukkit.getOnlinePlayers().size() + "")
                .replace("%kills%", hcfPlayer.getPlayerStatistic().kills + "")
                .replace("%deaths%", hcfPlayer.getPlayerStatistic().deaths + "")
                .replace("%lives%", hcfPlayer.getLives() + "");
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String getDirection(Player player) {
        double rotation = (player.getLocation().getYaw()) % 360;
        if (rotation < 0) {
            rotation += 360.0;
            if (0 <= rotation && rotation < 22.5) {
                return "N";
            }
            if (22.5 <= rotation && rotation < 67.5) {
                return "NE";
            }
            if (67.5 <= rotation && rotation < 112.5) {
                //return BlockFace.EAST;
                return "E";
            }
            if (112.5 <= rotation && rotation < 157.5) {
                //return BlockFace.SOUTH_EAST;
                return "SE";
            }
            if (157.5 <= rotation && rotation < 202.5) {
                // return BlockFace.SOUTH;
                return "S";
            }
            if (202.5 <= rotation && rotation < 247.5) {
                //return BlockFace.SOUTH_WEST;
                return "SW";
            }
            if (247.5 <= rotation && rotation < 292.5) {
                //return BlockFace.WEST;
                return "W";
            }
            if (292.5 <= rotation && rotation < 337.5) {
                //return BlockFace.NORTH_WEST;
                return "NW";
            }
            if (337.5 <= rotation && rotation < 359) {
                //return BlockFace.NORTH;
                return "N";
            }
        }
        return "N";
    }
}
