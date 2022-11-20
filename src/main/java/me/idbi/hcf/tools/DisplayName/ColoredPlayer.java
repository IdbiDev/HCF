package me.idbi.hcf.tools.DisplayName;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.ChatColor;

public class ColoredPlayer extends EntityPlayer {

    public ColoredPlayer (MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);

    }

    @Override
    public GameProfile getProfile() {
        return new GameProfile(this.uniqueID, ChatColor.RED + this.displayName);
    }
}
