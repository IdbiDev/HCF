package me.idbi.hcf.tools.DisplayName;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collections;


public class ScoreboardTeams implements Listener {
    private Main m = Main.getPlugin(Main.class);
    public void teszt() {
        Main.protocolManager.addPacketListener(new PacketAdapter(m, PacketType.Play.Server.PLAYER_INFO) {

            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
                    return;
                }

                com.comphenix.protocol.wrappers.PlayerInfoData pid = event.getPacket().getPlayerInfoDataLists().read(0).get(0); //get the original packet to set the skin later

                String prefix = "[32CharacterPrefixCharacter] ";
                String name = prefix + pid.getProfile().getName();

                int maxLength = 48;
                int length = name.substring(0, Math.min(maxLength, name.length())).length();

                String[] slots = {"", "", ""};
                for (int i = 0; i < length; i++) {
                    slots[i / 16] += name.charAt(i);
                }
                //Since you can only have 16 characters for the prefix, 16 for the nametag, and 16 for the stuffix, every 16 characters has to be split up

                String finalName = slots[1]; //Name to change player's name to
                com.comphenix.protocol.wrappers.PlayerInfoData newPid = new com.comphenix.protocol.wrappers.PlayerInfoData(pid.getProfile().withName(finalName), pid.getLatency(),
                        pid.getGameMode(), WrappedChatComponent.fromText(name));

                newPid.getProfile().getProperties().putAll(pid.getProfile().getProperties()); //Set the skin of the new PlayerInfoData to the original skin from the original PlayerInfoData

                event.getPacket().getPlayerInfoDataLists().write(0, Collections.singletonList(newPid)); //Overwrite the packet

                Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard(); //get the main scoreboard
                if (score == null) return;

                //create a new team and set the prefix and suffix
                Team team = score.getTeam(newPid.getProfile().getName());

                if (team == null) team = score.registerNewTeam(newPid.getProfile().getName());

                team.setPrefix(slots[1]);
                team.setSuffix(slots[2]);
                team.addEntry(newPid.getProfile().getName());
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        com.comphenix.protocol.wrappers.PlayerInfoData pid = new com.comphenix.protocol.wrappers.PlayerInfoData(WrappedGameProfile.fromPlayer(p), 1,
                EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText("whatever_string"));

        WrapperPlayServerPlayerInfo wpspi = new WrapperPlayServerPlayerInfo();
        wpspi.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        wpspi.setData(Collections.singletonList(pid));
        for (Player o : Bukkit.getOnlinePlayers()) {
            if (o.equals(p)) {
                continue;
            }
            o.hidePlayer(p);
            wpspi.sendPacket(o);
        }

        new BukkitRunnable() {

            @Override
            public void run() {

                p.setDisplayName(p.getName());

                for (Player o : Bukkit.getOnlinePlayers()) {
                    if (o.equals(p)) {
                        continue;
                    }
                    o.showPlayer(p);
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 1L);
    }
}
