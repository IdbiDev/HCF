package me.idbi.hcf.Tools;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.idbi.hcf.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BungeeChanneling implements PluginMessageListener {

    private static final Main m = Main.getInstance();
    private static BungeeChanneling bungeeChanneling;

    public BungeeChanneling() {
        bungeeChanneling = this;
    }

    public static BungeeChanneling getInstance() {
        return bungeeChanneling;
    }

    public void sendToLobby(Player p) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(p.getName());
        out.writeUTF(Main.lobbyName);

        p.getServer().sendPluginMessage(m, "BungeeCord", out.toByteArray());
    }

    public void getLobby() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");

        m.getServer().sendPluginMessage(m, "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("GetServers")) {
            String[] serverList = in.readUTF().split(", ");
            List<String> servers = new ArrayList<>(Arrays.asList(serverList));
            if(servers.size() > 0)
                Main.lobbyName = servers.get(0);
        }
    }

}
