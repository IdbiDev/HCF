package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.*;
import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFRules;
import me.idbi.hcf.HCFServer;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.BoardManager;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.TabManager.TabManager;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Tasks;
import me.idbi.hcf.Tools.WaypointPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName();
    }

    @Override
    public String getPermission() {
        return "factions.commands.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        Main.getInstance().getTabManager().reload();
        LimitsFile.reload();
        BoardFile.reload();
        TabFile.reload();
        ReclaimFile.reload();
        ConfigManager.getConfigManager().setup();
        TabManager.getManager().reload();
        HCFServer.getServer().reload();
        HCFRules.getRules().reload();
        BoardManager.get().setup();
        Scoreboards.refreshAll();
        p.sendMessage(Messages.reload.language(p).queue());
        Tasks.executeLater(5, () -> {
            for (HCFPlayer value : Main.playerCache.values()) {
                // if(value.isOnline())
                value.getWaypointPlayer().reload();
            }
        });
    }
}
