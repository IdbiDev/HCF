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

import java.util.HashMap;

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
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        duty(p);
    }

    public static void duty(Player p) {
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        boolean state = !hcf.isInDuty();
        if (state) {
            p.setGameMode(GameMode.CREATIVE);
            hcf.setInDuty(true);

            NameChanger.refresh(p);
            AdminTools.InvisibleManager.hidePlayer(p);

            //ScoreboardBuilder.getOrCreate(p).removeLines();
            AdminScoreboard.refresh(p);
            p.sendMessage(Messages.admin_duty_on.language(p).queue());
        } else {

            hcf.setInDuty(false);
            NameChanger.refresh(p);
            AdminTools.InvisibleManager.showPlayer(p);

            // ScoreboardBuilder.getOrCreate(p).removeLines();
            Scoreboards.refresh(p);

            p.sendMessage(Messages.admin_duty_off.language(p).queue());
        }
    }
}
