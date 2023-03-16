package me.idbi.hcf.Tools;

import me.idbi.hcf.Classes.SubClasses.Rogue;
import me.idbi.hcf.Commands.FactionCommands.FactionHomeCommand;
import me.idbi.hcf.Commands.FactionCommands.FactionSetHomeCommand;
import me.idbi.hcf.Commands.FactionCommands.FactionStuckCommand;
import me.idbi.hcf.Commands.SingleCommands.LogoutCommand;
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
import me.idbi.hcf.InventoryRollback.GUI.RollbackGUIEvent.RollbackInventoryClick;
import me.idbi.hcf.Koth.GUI.KOTHCloseEvent;
import me.idbi.hcf.Koth.GUI.KOTHInvClickEvent;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.SignShop.CreateShopSign;
import me.idbi.hcf.MessagesEnums.SignShop.InteractShopSign;
import me.idbi.hcf.SubClaims.SubClaimBreakListener;
import me.idbi.hcf.SubClaims.SubClaimListener;
import me.idbi.hcf.SubClaims.SubClaimSignListener;
import org.bukkit.plugin.PluginManager;

import static org.bukkit.Bukkit.getServer;

public class SetupEvents {
    public static void setupEvents() {
        //xd
        //getServer().getPluginManager().registerEvents(new Assassin(), Main.getPlugin(Main.class));
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new onPlayerJoin(), Main.getPlugin(Main.class));

        pm.registerEvents(new onPlayerLeft(), Main.getPlugin(Main.class));

        pm.registerEvents(new onDamage(), Main.getPlugin(Main.class));

        pm.registerEvents(new onBlockBreak(), Main.getPlugin(Main.class));

        pm.registerEvents(new onBlockPlace(), Main.getPlugin(Main.class));

        pm.registerEvents(new onDeath(), Main.getPlugin(Main.class));

        pm.registerEvents(new onPlayerInteract(), Main.getPlugin(Main.class));

        pm.registerEvents(new onPlayerMove(), Main.getPlugin(Main.class));

        pm.registerEvents(new onPlayerRespawn(), Main.getPlugin(Main.class));

        pm.registerEvents(new onPlayerPreJoin(), Main.getPlugin(Main.class));

        pm.registerEvents(new onBowShoot(), Main.getPlugin(Main.class));

        pm.registerEvents(new FactionSetHomeCommand(), Main.getPlugin(Main.class));
        pm.registerEvents(new FactionHomeCommand(), Main.getPlugin(Main.class));

        pm.registerEvents(new InteractShopSign(), Main.getPlugin(Main.class));

        pm.registerEvents(new CreateShopSign(), Main.getPlugin(Main.class));

        pm.registerEvents(new ElevatorCreate(), Main.getPlugin(Main.class));

        pm.registerEvents(new ElevatorInteract(), Main.getPlugin(Main.class));

        pm.registerEvents(new onPlayerChat(), Main.getPlugin(Main.class));

        pm.registerEvents(new onChunkLoaded(), Main.getPlugin(Main.class));

        // Enchant
        pm.registerEvents(new TableEvent(), Main.getPlugin(Main.class));
        pm.registerEvents(new EnchantInventory(), Main.getPlugin(Main.class));
        pm.registerEvents(new TableInteract(), Main.getPlugin(Main.class));
        pm.registerEvents(new PluginLoad(), Main.getPlugin(Main.class));

        pm.registerEvents(new PearlFixer(), Main.getPlugin(Main.class));

        // KOTH
        pm.registerEvents(new Koth(), Main.getPlugin(Main.class));
        pm.registerEvents(new KOTHCloseEvent(), Main.getPlugin(Main.class));
        pm.registerEvents(new KOTHInvClickEvent(), Main.getPlugin(Main.class));

        pm.registerEvents(new onSignPlace(), Main.getPlugin(Main.class));

        pm.registerEvents(new onConsumeItem(), Main.getPlugin(Main.class));
        pm.registerEvents(new BasicClaim(), Main.getPlugin(Main.class));
        pm.registerEvents(new SpawnClaim(), Main.getPlugin(Main.class));
        pm.registerEvents(new KothClaim(), Main.getPlugin(Main.class));
        pm.registerEvents(new ProtectedClaim(), Main.getPlugin(Main.class));
        pm.registerEvents(new SpecialClaim(), Main.getPlugin(Main.class));

        //pm.registerEvents(new PlacePVPTag(), Main.getPlugin(Main.class));
        pm.registerEvents(new ProjectileDamage(), Main.getPlugin(Main.class));

        // Faction GUI
        pm.registerEvents(new Click_MainInventory(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_PlayerManage(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_MemberList(), Main.getPlugin(Main.class));
        pm.registerEvents(new KickConfirm(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_RankManager(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_RankMenu(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_PermissionManager(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_PlayerRankManager(), Main.getPlugin(Main.class));
        pm.registerEvents(new DeleteRankConfirm(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_InviteManager(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_InvitedPlayers(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_RankPriority(), Main.getPlugin(Main.class));
        pm.registerEvents(new Stats_Inventory(), Main.getPlugin(Main.class));
        pm.registerEvents(new HistoryEvent(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_AllyMain(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_AllyRequests(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_AllyManageList(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_AllyUnRequest(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_AllyPermissions(), Main.getPlugin(Main.class));
        pm.registerEvents(new Click_ManageAlly(), Main.getPlugin(Main.class));

        pm.registerEvents(new Rogue(), Main.getPlugin(Main.class));

        pm.registerEvents(new CrowbarInteractListener(), Main.getPlugin(Main.class));

        pm.registerEvents(new SubClaimSignListener(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new SubClaimListener(), Main.getPlugin(Main.class));
        pm.registerEvents(new SubClaimBreakListener(), Main.getPlugin(Main.class));
        pm.registerEvents(new LogoutCommand(), Main.getPlugin(Main.class));
        pm.registerEvents(new FactionHomeCommand(), Main.getPlugin(Main.class));
        pm.registerEvents(new FactionStuckCommand(), Main.getPlugin(Main.class));
        pm.registerEvents(new BlockToBlockListener(), Main.getPlugin(Main.class));

        // Command events
        pm.registerEvents(new RollbackInventoryClick(), Main.getPlugin(Main.class));

    }
}
