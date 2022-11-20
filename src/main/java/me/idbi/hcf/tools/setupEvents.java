package me.idbi.hcf.tools;

import me.idbi.hcf.Elevator.ElevatorCreate;
import me.idbi.hcf.Elevator.ElevatorInteract;
import me.idbi.hcf.FrakcioGUI.GUIEvents.*;
import me.idbi.hcf.FrakcioGUI.KickConfirm.DeleteRankConfirm;
import me.idbi.hcf.FrakcioGUI.KickConfirm.KickConfirm;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.SignShop.CreateShopSign;
import me.idbi.hcf.MessagesEnums.SignShop.InteractShopSign;
import me.idbi.hcf.TabManager.TabManager;
import me.idbi.hcf.commands.cmdFunctions.Faction_Home;
import me.idbi.hcf.events.Enchants.EnchantInventory;
import me.idbi.hcf.events.Enchants.TableEvent;
import me.idbi.hcf.events.Enchants.TableInteract;
import me.idbi.hcf.events.*;
import me.idbi.hcf.events.claim.Basic_Claim;
import me.idbi.hcf.events.claim.Koth_Claim;
import me.idbi.hcf.events.claim.Spawn_Claim;
import me.idbi.hcf.koth.GUI.KOTHCloseEvent;
import me.idbi.hcf.koth.GUI.KOTHInvClickEvent;
import me.idbi.hcf.koth.KOTH;
import me.idbi.hcf.tools.DisplayName.displayTeams;

import static org.bukkit.Bukkit.getServer;

public class setupEvents {
    public static void SetupEvents() {
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

        getServer().getPluginManager().registerEvents(new Faction_Home(), Main.getPlugin(Main.class));

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
        //getServer().getPluginManager().registerEvents(new AnvilEvent(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new PearlFixer(), Main.getPlugin(Main.class));

        // KOTH
        getServer().getPluginManager().registerEvents(new KOTH(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new KOTHCloseEvent(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new KOTHInvClickEvent(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onSignPlace(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new onConsumeItem(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Basic_Claim(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Spawn_Claim(), Main.getPlugin(Main.class));
        getServer().getPluginManager().registerEvents(new Koth_Claim(), Main.getPlugin(Main.class));

        getServer().getPluginManager().registerEvents(new PlacePVPTag(), Main.getPlugin(Main.class));
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

        getServer().getPluginManager().registerEvents(new TabManager(), Main.getPlugin(Main.class));

    }
}
