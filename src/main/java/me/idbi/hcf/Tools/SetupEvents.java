package me.idbi.hcf.Tools;

import me.idbi.hcf.Classes.SubClasses.Rogue;
import me.idbi.hcf.Commands.FactionCommands.FactionHomeCommand;
import me.idbi.hcf.Commands.FactionCommands.FactionSetHomeCommand;
import me.idbi.hcf.Commands.FactionCommands.FactionStatsCommand;
import me.idbi.hcf.Commands.FactionCommands.FactionStuckCommand;
import me.idbi.hcf.BukkitCommands.UtilCommands.BackCommand;
import me.idbi.hcf.BukkitCommands.UtilCommands.GodCommand;
import me.idbi.hcf.Commands.LogoutCommand;
import me.idbi.hcf.Elevator.ElevatorCreate;
import me.idbi.hcf.Elevator.ElevatorInteract;
import me.idbi.hcf.Events.Claim.*;
import me.idbi.hcf.Events.*;
import me.idbi.hcf.Events.Enchants.EnchantInventory;
import me.idbi.hcf.Events.Enchants.TableEvent;
import me.idbi.hcf.Events.Enchants.TableInteract;
import me.idbi.hcf.FrakcioGUI.GUIEvents.*;
import me.idbi.hcf.FrakcioGUI.KickConfirm.DeleteRankConfirm;
import me.idbi.hcf.FrakcioGUI.KickConfirm.KickConfirm;
import me.idbi.hcf.HistoryGUI.Events.HistoryEvent;
import me.idbi.hcf.HistoryGUI.Events.Stats_Inventory;
import me.idbi.hcf.Koth.GUI.KOTHCloseEvent;
import me.idbi.hcf.Koth.GUI.KOTHInvClickEvent;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.SignShop.CreateShopSign;
import me.idbi.hcf.MessagesEnums.SignShop.InteractShopSign;

import static org.bukkit.Bukkit.getServer;

public class SetupEvents {
    public static void setupEvents() {
        //xd
        //getServer().getPluginManager().registerEvents(new Assassin(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerJoin(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerLeft(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onDamage(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onBlockBreak(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onBlockPlace(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onDeath(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerInteract(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerMove(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerRespawn(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerPreJoin(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onBowShoot(), Main.getPlugin(Main.class));

        pm.registerEvents(new FactionSetHomeCommand(), Main.getPlugin(Main.class));
        pm.registerEvents(new FactionHomeCommand(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new InteractShopSign(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new CreateShopSign(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new ElevatorCreate(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new ElevatorInteract(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onPlayerChat(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onChunkLoaded(), Main.getPlugin(Main.class));

        // Enchant
        getServer().getPluginManager().registerEvents(new TableEvent(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new EnchantInventory(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new TableInteract(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new PluginLoad(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new PearlFixer(), Main.getPlugin(Main.class));

        // KOTH
        getServer().getPluginManager().registerEvents(new Koth(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new KOTHCloseEvent(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new KOTHInvClickEvent(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onSignPlace(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onConsumeItem(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new BasicClaim(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new SpawnClaim(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new KothClaim(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new ProtectedClaim(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new SpecialClaim(), Main.getPlugin(Main.class));

        //getServer().getPluginManager().registerEvents(new PlacePVPTag(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new ProjectileDamage(), Main.getPlugin(Main.class));

        // Faction GUI
        getServer().getPluginManager().registerEvents(new Click_MainInventory(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_PlayerManage(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_MemberList(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new KickConfirm(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_RankManager(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_RankMenu(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_PermissionManager(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_PlayerRankManager(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new DeleteRankConfirm(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_InviteManager(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_InvitedPlayers(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_RankPriority(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Stats_Inventory(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new HistoryEvent(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_AllyMain(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_AllyRequests(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_AllyManageList(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_AllyUnRequest(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_AllyPermissions(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Click_ManageAlly(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new Rogue(), Main.getPlugin(Main.class));
    }
}
