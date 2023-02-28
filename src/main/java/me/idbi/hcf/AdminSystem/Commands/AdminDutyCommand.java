package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class AdminDutyCommand extends SubCommand {
    @Override
    public String getName() {
        return "duty";
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
        return "faction.command.admin." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        boolean state = !hcf.inDuty;
        if (state) {
            p.setGameMode(GameMode.CREATIVE);
            hcf.setDuty(true);

            NameChanger.refresh(p);
            AdminTools.InvisibleManager.hidePlayer(p);

            AdminScoreboard.refresh(p);
            p.sendMessage(Messages.admin_duty_on.language(p).queue());
        } else {
            p.setGameMode(GameMode.SURVIVAL);

            hcf.setDuty(false);
            NameChanger.refresh(p);
            AdminTools.InvisibleManager.showPlayer(p);

            Scoreboards.refresh(p);

            p.sendMessage(Messages.admin_duty_off.language(p).queue());
        }
    }
}
