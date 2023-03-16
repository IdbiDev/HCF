package me.idbi.hcf.TabManager.TabAPIv2;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.base.Charsets;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TabAPI implements Listener {
    private static HashMap<String, TabObject> playerTab = new HashMap<>();

    private static HashMap<String, TabHolder> playerTabLast = new HashMap<>();

    private static HashMap<String, TabObject47> playerTab47 = new HashMap<>();

    private static HashMap<String, TabHolder47> playerTabLast47 = new HashMap<>();

    private static HashMap<Player, ArrayList<PacketContainer>> cachedPackets = new HashMap<>();

    private static HashMap<Player, Integer> updateSchedules = new HashMap<>();

    private static int horizTabSize = 3;

    private static int vertTabSize = 20;

    private static int horizTabSize47 = 4;

    private static int vertTabSize47 = 20;

    private static String[] colors = new String[] {
            "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "0",
            "a", "b", "c", "d", "f", "g", "h", "i", "j", "k",
            "l",
            "m", "n", "o", "p", "r", "s", "t", "u", "v",
            "w", "x", "y", "z" };

    private static int e = 0;

    private static int r = 0;

    private static long flickerPrevention = 5L;

    public static ProtocolManager protocolManager;

    private static boolean shuttingdown = true;

    private static Main plugin;

    public void setup() {
        plugin = Main.getInstance();
        protocolManager = ProtocolLibrary.getProtocolManager();
        for (Player p : Bukkit.getOnlinePlayers()) {
            setPriority(plugin, p, 2);
            resetTabList(p);
            setPriority(plugin, p, -2);
        }
        protocolManager.addPacketListener((PacketListener)new PacketAdapter(Main.getInstance(),
                ListenerPriority.NORMAL,
                new PacketType[] { PacketType.Play.Server.PLAYER_INFO }) {
            public void onPacketSending(PacketEvent event) {
                PacketContainer p = event.getPacket();
                List<PlayerInfoData> pinfodata = (List<PlayerInfoData>)p.getPlayerInfoDataLists()
                        .read(0);
                String s = ((PlayerInfoData)pinfodata.get(0)).getProfile()
                        .getName();
                if (s.startsWith("$")) {
                    List<PlayerInfoData> pinfodataReSend = new ArrayList<>();
                    PlayerInfoData pinfod = pinfodata.get(0);
                    pinfodataReSend.add(new PlayerInfoData(pinfod.getProfile()
                            .withName(s.substring(1)), pinfod.getPing(), pinfod
                            .getGameMode(),
                            WrappedChatComponent.fromText(pinfod.getProfile().getName()
                                    .substring(1))));
                    p.getPlayerInfoDataLists().write(0, pinfodataReSend);
                    event.setPacket(p);
                } else if (TabAPI.protocolManager.getProtocolVersion(event
                        .getPlayer()) < 47) {
                    event.setCancelled(true);
                }
            }
        });
    }

    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers())
            clearTab(p);
        shuttingdown = true;
        playerTab = null;
        playerTabLast = null;
        playerTab47 = null;
        playerTabLast47 = null;
    }

    private static String toColorString(int slotId) {
        return String.valueOf('§') + String.valueOf((char)slotId);
    }

    private static void addPacket(Player p, String msg, int slotId, WrappedGameProfile gameProfile, boolean b, int ping) {
        EnumWrappers.PlayerInfoAction action;
        PacketContainer message = protocolManager
                .createPacket(PacketType.Play.Server.PLAYER_INFO);
        String nameToShow = String.valueOf(!shuttingdown ? "$" : "") + msg;
        if (protocolManager.getProtocolVersion(p) >= 47)
            nameToShow = String.valueOf(!shuttingdown ? "$" : "") + toColorString(slotId) +
                    msg.substring(0, Math.min(msg.length(), 14));
        if (b) {
            action = EnumWrappers.PlayerInfoAction.ADD_PLAYER;
        } else {
            action = EnumWrappers.PlayerInfoAction.REMOVE_PLAYER;
        }
        message.getPlayerInfoAction().write(0, action);
        List<PlayerInfoData> pInfoData = new ArrayList<>();
        if (gameProfile != null) {
            pInfoData.add(new PlayerInfoData(gameProfile.withName(
                    nameToShow.substring(1)).withId(
                    UUID.nameUUIDFromBytes((
                            "OfflinePlayer:" + nameToShow.substring(1))
                            .getBytes(Charsets.UTF_8)).toString()),
                    ping, EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromText(nameToShow)));
        } else {
            pInfoData.add(new PlayerInfoData(new WrappedGameProfile(
                    UUID.nameUUIDFromBytes(("OfflinePlayer:" + nameToShow
                            .substring(1)).getBytes(Charsets.UTF_8)),
                    nameToShow.substring(1)), ping,
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromText(nameToShow)));
        }
        message.getPlayerInfoDataLists().write(0, pInfoData);
        ArrayList<PacketContainer> packetList = cachedPackets.get(p);
        if (packetList == null) {
            packetList = new ArrayList<>();
            cachedPackets.put(p, packetList);
        }
        packetList.add(message);
        System.out.println("added?");
    }

    private static void flushPackets() {
        Player[] packetPlayers = (Player[])cachedPackets.keySet().toArray(
                (Object[])new Player[0]);
        Player[] arrayOfPlayer1 = packetPlayers;
        int j = packetPlayers.length;
        for (int i = 0; i < j; i++) {
            Player p = arrayOfPlayer1[i];
            flushPackets(p, (Object)null);
        }
    }

    private static void flushPackets(final Player p, final Object tabCopy) {
        final PacketContainer[] packets = (PacketContainer[])((ArrayList)cachedPackets
                .get(p)).toArray((Object[])new PacketContainer[0]);
        Integer taskID = updateSchedules.get(p);
        if (taskID != null)
            Bukkit.getScheduler().cancelTask(taskID.intValue());
        taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(), new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            byte b;
                            int i;
                            PacketContainer[] arrayOfPacketContainer;
                            for (i = (arrayOfPacketContainer = packets).length, b = 0; b < i; ) {
                                PacketContainer packet = arrayOfPacketContainer[b];
                                try {
                                    TabAPI.protocolManager.sendServerPacket(p, packet);
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                    System.out
                                            .println("[TabAPI] Error sending packet to client");
                                }
                                b++;
                            }
                        }
                        if (tabCopy != null)
                            if (tabCopy instanceof TabHolder47) {
                                TabAPI.playerTabLast47.put(p.getName(),
                                        (TabHolder47) tabCopy);
                            } else if (tabCopy instanceof TabHolder) {
                                TabAPI.playerTabLast.put(p.getName(),
                                        (TabHolder) tabCopy);
                            }
                        TabAPI.updateSchedules.remove(p);
                    }
                }, flickerPrevention);
        updateSchedules.put(p, taskID);
        cachedPackets.remove(p);
    }

    private static TabObject getTab(Player p) {
        TabObject tabo = playerTab.get(p.getName());
        if (tabo == null) {
            tabo = new TabObject();
            playerTab.put(p.getName(), tabo);
        }
        return tabo;
    }

    private static TabObject47 getTab47(Player p) {
        TabObject47 tabo = playerTab47.get(p.getName());
        if (tabo == null) {
            tabo = new TabObject47();
            playerTab47.put(p.getName(), tabo);
        }
        return tabo;
    }

    public static void setPriority(Plugin plugin, Player player, int pri) {
        getTab(player).setPriority(plugin, pri);
    }

    public static void disableTabForPlayer(Player p) {
        playerTab.put(p.getName(), null);
        playerTab47.put(p.getName(), null);
        resetTabList(p);
    }

    public static void resetTabList(Player p) {
        int a = 0;
        int b = 0;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            setTabString(Bukkit.getPluginManager().getPlugin("TabAPI"), p, a,
                    b, pl.getPlayerListName());
            b++;
            if (b > getHorizSize(protocolManager.getProtocolVersion(pl))) {
                b = 0;
                a++;
            }
        }
    }

    public static void setTabString(Plugin plugin, Player p, int x, int y, String msg) {
        setTabString(plugin, p, x, y, msg, 0, (WrappedGameProfile)null);
    }

    public static void setTabString(Plugin plugin, Player p, int x, int y, String msg, int ping) {
        setTabString(plugin, p, x, y, msg, ping, (WrappedGameProfile)null);
    }

    public static void setTabString(Plugin plugin, Player p, int x, int y) {
        setTabString(plugin, p, x, y);
    }

    public static void setTabString(Plugin plugin, Player p, int x, int y, String msg, int ping, WrappedGameProfile gameProfile) {
        try {
            if (p != null) {
                if (protocolManager.getProtocolVersion(p) >= 47) {
                    TabObject47 tabo = getTab47(p);
                    tabo.setTab(plugin, x, y, msg, ping, gameProfile);
                    playerTab47.put(p.getName(), tabo);
                } else {
                    TabObject tabo = getTab(p);
                    tabo.setTab(plugin, x, y, msg, ping);
                    playerTab.put(p.getName(), tabo);
                }
            } else {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updatePlayer(Player p) {
        if (!p.isOnline())
            return;
        r = 0;
        e = 0;
        if (protocolManager.getProtocolVersion(p) >= 47) {
            TabObject47 tabo = playerTab47.get(p.getName());
            TabHolder47 tab = tabo.getTab();
            if (tab == null)
                return;
            clearTab(p);
            for (int b = 0; b < tab.maxv; b++) {
                for (int a = 0; a < tab.maxh; a++) {
                    if (tab.tabs[a][b] == null)
                        tab.tabs[a][b] = nextNull();
                    String msg = tab.tabs[a][b];
                    int ping = tab.tabPings[a][b];
                    WrappedGameProfile gameProfile = tab.tabGameProfiles[a][b];
                    addPacket(
                            p,
                            (msg == null) ? " " : msg.substring(0,
                                    Math.min(msg.length(), 16)),
                            getSlotId(b, a), gameProfile, true, ping);
                }
            }
            flushPackets(p, tab.getCopy());
        } else {
            TabObject tabo = playerTab.get(p.getName());
            TabHolder tab = tabo.getTab();
            if (tab == null)
                return;
            clearTab(p);
            for (int b = 0; b < tab.maxv; b++) {
                for (int a = 0; a < tab.maxh; a++) {
                    if (tab.tabs[a][b] == null)
                        tab.tabs[a][b] = nextNull();
                    String msg = tab.tabs[a][b];
                    int ping = tab.tabPings[a][b];
                    addPacket(
                            p,
                            (msg == null) ? " " : msg.substring(0,
                                    Math.min(msg.length(), 16)), 0, (WrappedGameProfile)null, true,
                            ping);
                }
            }
            flushPackets(p, tab.getCopy());
        }
    }

    public static void clearTab(Player p) {
        if (!p.isOnline())
            return;
        if (protocolManager.getProtocolVersion(p) >= 47) {
            TabHolder47 tabold = playerTabLast47.get(p.getName());
            if (tabold != null)
                for (int b = 0; b < tabold.maxv; b++) {
                    for (int a = 0; a < tabold.maxh; a++) {
                        String msg = tabold.tabs[a][b];
                        WrappedGameProfile gameProfile = tabold.tabGameProfiles[a][b];
                        addPacket(p,
                                msg.substring(0, Math.min(msg.length(), 16)),
                                getSlotId(b, a), gameProfile, false, 0);
                    }
                }
        } else {
            TabHolder tabold = playerTabLast.get(p.getName());
            if (tabold != null) {
                byte b;
                int i;
                String[][] arrayOfString;
                for (i = (arrayOfString = tabold.tabs).length, b = 0; b < i; ) {
                    String[] s = arrayOfString[b];
                    byte b1;
                    int j;
                    String[] arrayOfString1;
                    for (j = (arrayOfString1 = s).length, b1 = 0; b1 < j; ) {
                        String message = arrayOfString1[b1];
                        if (message != null)
                            addPacket(
                                    p,
                                    message.substring(0,
                                            Math.min(message.length(), 16)), 0,
                                    (WrappedGameProfile)null, false, 0);
                        b1++;
                    }
                    b++;
                }
            }
        }
    }

    public static void updateAll() {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
            updatePlayer(p);
    }

    public static String nextNull() {
        String s = "";
        for (int a = 0; a < r; a++)
            s = " " + s;
        s = String.valueOf(s) + colors[e];
        e++;
        if (e > 14) {
            e = 0;
            r++;
        }
        return s;
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent e) {
        playerTab.remove(e.getPlayer().getName());
        playerTabLast.remove(e.getPlayer().getName());
        playerTab47.remove(e.getPlayer().getName());
        playerTabLast47.remove(e.getPlayer().getName());
    }

    @EventHandler
    public void PlayerKick(PlayerKickEvent e) {
        playerTab.remove(e.getPlayer().getName());
        playerTabLast.remove(e.getPlayer().getName());
        playerTab47.remove(e.getPlayer().getName());
        playerTabLast47.remove(e.getPlayer().getName());
    }

    @Deprecated
    public static int getVertSize() {
        return vertTabSize;
    }

    @Deprecated
    public static int getHorizSize() {
        return horizTabSize;
    }

    public static int getVertSize(int protocol) {
        if (protocol >= 47)
            return vertTabSize47;
        return vertTabSize;
    }

    public static int getHorizSize(int protocol) {
        if (protocol >= 47)
            return horizTabSize47;
        return horizTabSize;
    }

    public static int getSlotId(int x, int y) {
        if (y == 0)
            return 11 + x;
        if (y == 1)
            return 31 + x;
        if (y == 2)
            return 51 + x;
        if (y == 3)
            return 71 + x;
        return 0;
    }
}