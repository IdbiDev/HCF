package me.idbi.hcf.Tools;

import me.idbi.hcf.Classes.SubClasses.Rogue;
import me.idbi.hcf.Commands.FactionCommands.FactionHomeCommand;
import me.idbi.hcf.Commands.FactionCommands.FactionSetHomeCommand;
import me.idbi.hcf.Commands.FactionCommands.FactionStuckCommand;
import me.idbi.hcf.Commands.SingleCommands.LogoutCommand;
import me.idbi.hcf.Elevator.ElevatorCreate;
import me.idbi.hcf.Elevator.ElevatorInteract;
import me.idbi.hcf.Events.*;
import me.idbi.hcf.Events.Claim.*;
import me.idbi.hcf.Events.Enchants.Anvil.AnvilEvent;
import me.idbi.hcf.Events.Enchants.EnchantInventory;
import me.idbi.hcf.Events.Enchants.TableEvent;
import me.idbi.hcf.Events.Enchants.TableInteract;
import me.idbi.hcf.FactionGUI.GUIEvents.*;
import me.idbi.hcf.FactionGUI.KickConfirm.DeleteRankConfirm;
import me.idbi.hcf.FactionGUI.KickConfirm.KickConfirm;
import me.idbi.hcf.FactionListGUI.listeners.FactionTopListener;
import me.idbi.hcf.HistoryGUI.Events.HistoryEvent;
import me.idbi.hcf.HistoryGUI.Events.Stats_Inventory;
import me.idbi.hcf.InventoryRollback.GUI.RollbackGUIEvent.RollbackInventoryClick;
import me.idbi.hcf.Koth.GUI.KOTHCloseEvent;
import me.idbi.hcf.Koth.GUI.KOTHInvClickEvent;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.SignShop.CreateShopSign;
import me.idbi.hcf.SignShop.InteractShopSign;
import me.idbi.hcf.SubClaims.SubClaimBreakListener;
import me.idbi.hcf.SubClaims.SubClaimListener;
import me.idbi.hcf.SubClaims.SubClaimSignListener;
import org.bukkit.plugin.PluginManager;

import static org.bukkit.Bukkit.getServer;

public class SetupEvents {
    private static Main m = Main.getInstance();
    public static void setupEvents() {
        //xd
        //getServer().getPluginManager().registerEvents(new Assassin(), m);
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new AnvilEvent(), m);
        
        pm.registerEvents(new PlayerJoin(), m);

        pm.registerEvents(new PlayerLeft(), m);

        pm.registerEvents(new EntDamageByEnt(), m);

        pm.registerEvents(new BlockBreak(), m);

        pm.registerEvents(new BlockPlace(), m);

        pm.registerEvents(new PlayerDeath(), m);

        pm.registerEvents(new PlayerInteract(), m);

        pm.registerEvents(new PlayerMove(), m);

        pm.registerEvents(new PlayerRespawn(), m);

        pm.registerEvents(new PlayerPreJoin(), m);

        pm.registerEvents(new BowShoot(), m);

        pm.registerEvents(new FactionSetHomeCommand(), m);
        pm.registerEvents(new FactionHomeCommand(), m);

        pm.registerEvents(new InteractShopSign(), m);

        pm.registerEvents(new CreateShopSign(), m);

        pm.registerEvents(new ElevatorCreate(), m);

        pm.registerEvents(new ElevatorInteract(), m);

        pm.registerEvents(new PlayerChat(), m);

        pm.registerEvents(new ChunkLoad(), m);

        // Enchant
        pm.registerEvents(new TableEvent(), m);
        pm.registerEvents(new EnchantInventory(), m);
        pm.registerEvents(new TableInteract(), m);
        pm.registerEvents(new PluginLoad(), m);

        pm.registerEvents(new PearlFixer(), m);

        // KOTH
        pm.registerEvents(new Koth(), m);
        pm.registerEvents(new KOTHCloseEvent(), m);
        pm.registerEvents(new KOTHInvClickEvent(), m);

        pm.registerEvents(new SignPlace(), m);

        pm.registerEvents(new ConsumeItem(), m);
        pm.registerEvents(new BasicClaim(), m);
        pm.registerEvents(new SpawnClaim(), m);
        pm.registerEvents(new KothClaim(), m);
        pm.registerEvents(new ProtectedClaim(), m);
        pm.registerEvents(new SpecialClaim(), m);

        //pm.registerEvents(new PlacePVPTag(), m);
        pm.registerEvents(new ProjectileDamage(), m);

        // Faction GUI
        pm.registerEvents(new Click_MainInventory(), m);
        pm.registerEvents(new Click_PlayerManage(), m);
        pm.registerEvents(new Click_MemberList(), m);
        pm.registerEvents(new KickConfirm(), m);
        pm.registerEvents(new Click_RankManager(), m);
        pm.registerEvents(new Click_RankMenu(), m);
        pm.registerEvents(new Click_PermissionManager(), m);
        pm.registerEvents(new Click_PlayerRankManager(), m);
        pm.registerEvents(new DeleteRankConfirm(), m);
        pm.registerEvents(new Click_InviteManager(), m);
        pm.registerEvents(new Click_InvitedPlayers(), m);
        pm.registerEvents(new Click_RankPriority(), m);
        pm.registerEvents(new Stats_Inventory(), m);
        pm.registerEvents(new HistoryEvent(), m);
        pm.registerEvents(new Click_AllyMain(), m);
        pm.registerEvents(new Click_AllyRequests(), m);
        pm.registerEvents(new Click_AllyManageList(), m);
        pm.registerEvents(new Click_AllyUnrequest(), m);
        pm.registerEvents(new Click_AllyPermissions(), m);
        pm.registerEvents(new Click_ManageAlly(), m);

        pm.registerEvents(new Rogue(), m);
        pm.registerEvents(new BrewPotion(), m);
        pm.registerEvents(new PlayerPortal(), m);

        pm.registerEvents(new CrowbarInteraction(), m);

        pm.registerEvents(new SubClaimSignListener(), m);
        pm.registerEvents(new SubClaimListener(), m);
        pm.registerEvents(new SubClaimBreakListener(), m);
        pm.registerEvents(new LogoutCommand(), m);
        pm.registerEvents(new FactionHomeCommand(), m);
        pm.registerEvents(new FactionStuckCommand(), m);
        pm.registerEvents(new BlockToBlock(), m);
        pm.registerEvents(new PlayerLogin(), m);

        // Command events
        pm.registerEvents(new RollbackInventoryClick(), m);
        pm.registerEvents(new PlayerWorldChange(), m);

        pm.registerEvents(new FactionTopListener(), m);
        pm.registerEvents(new PlacePVPTag(), m);
        pm.registerEvents(new EntitySpawn(), m);


    }
}
