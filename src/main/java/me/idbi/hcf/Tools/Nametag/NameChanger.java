package me.idbi.hcf.Tools.Nametag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.*;


public class NameChanger implements Listener {
    private final static Map<UUID, String> fakeNames = new HashMap<UUID, String>();
    private final Plugin plugin;

    public NameChanger(Main plugin) {
        this.plugin = plugin;
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getPlayerInfoAction().read(0) != PlayerInfoAction.ADD_PLAYER) return;
                List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<PlayerInfoData>();
                List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().read(0);
                for (PlayerInfoData playerInfoData : playerInfoDataList) {
                    if (playerInfoData == null || playerInfoData.getProfile() == null || Bukkit.getPlayer(playerInfoData.getProfile().getUUID()) == null) { //Unknown Player
                        newPlayerInfoDataList.add(playerInfoData);
                        continue;
                    }
                    WrappedGameProfile profile = playerInfoData.getProfile();
                    profile = profile.withName(getNameToSend(profile.getUUID()));
                    PlayerInfoData newPlayerInfoData = new PlayerInfoData(
                            profile, playerInfoData.getPing(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());
                    newPlayerInfoDataList.add(newPlayerInfoData);
                }
                event.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
            }
        });
    }

    //Szemszög                      koba
    public static void refresh(final Player player) {
        //World player
        //refreshTeams(player);
        for (final Player forWhom : Bukkit.getOnlinePlayers()) {
            if(forWhom == player) continue;
            if(!forWhom.getWorld().equals(player.getWorld())) continue;
            if (forWhom.canSee(player)) {
                String fakeName = "";

                if (Playertools.isTeammate(player, forWhom))
                    fakeName = Config.TeammateColor.asStr() + player.getName();
                else {
                    if (Playertools.isAlly(player, forWhom)) {
                        fakeName = Config.AllyColor.asStr() + player.getName();
                    } else {
                        if (Timers.ARCHER_TAG.has(player)) {
                            fakeName =  Config.ArcherTagColor.asStr() + player.getName();
                        } else {
                            fakeName = Config.EnemyColor.asStr() + player.getName();
                        }
                    }
                }

                if (Playertools.isInStaffDuty(player)) {
                    fakeName = Config.StaffModeColor.asStr() + player.getName();
                }

                if(!fakeNames.containsKey(player.getUniqueId())) {
                    forWhom.hidePlayer(player);
                    fakeNames.put(player.getUniqueId(), fakeName);
                    forWhom.showPlayer(player);
                } else {
                    if (!fakeNames.get(player.getUniqueId()).equalsIgnoreCase(fakeName)) {
                        forWhom.hidePlayer(player);
                        fakeNames.put(player.getUniqueId(), fakeName);
                        forWhom.showPlayer(player);
                    }
                }
            }
        }
        /*if(AdminTools.InvisibleManager.invisedAdmins.contains(player.getUniqueId())) {
            AdminTools.InvisibleManager.hidePlayer(player);
        }*/
    }

    public static void refreshAll() {
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            refresh(player);
        }
    }

    public static void refreshTeams(Player p) {
        Faction playerF = Playertools.getPlayerFaction(p);
        if (playerF == null) {
            return;
        }
        if (!Playertools.isFactionOnline(playerF)) return;
        for (final Player player : Playertools.getFactionOnlineMembers(playerF)) {
            for (final Player forWhom : Bukkit.getOnlinePlayers()) {

                forWhom.hidePlayer(player);
                if (Playertools.isTeammate(player, forWhom))
                    fakeNames.put(player.getUniqueId(), Config.TeammateColor.asStr() + player.getName());
                else {
                    if (Playertools.isAlly(player, forWhom)) {
                        fakeNames.put(player.getUniqueId(), Config.AllyColor.asStr() + player.getName());
                    } else {
                        fakeNames.put(player.getUniqueId(), Config.EnemyColor.asStr() + player.getName());
                    }
                }

                forWhom.showPlayer(player);
            }
        }
    }

    private String getNameToSend(UUID id) {
        Player player = Bukkit.getPlayer(id);
        if (!fakeNames.containsKey(player.getUniqueId())) return player.getName();
        String name = fakeNames.get(player.getUniqueId());
        return name.substring(0, Math.min(16, name.length()));
    }

    public void changeName(final Player player, String fakeName) {
        fakeNames.put(player.getUniqueId(), fakeName);
        refresh(player);
    }
}