package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.FactionHistory;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;

public class AdminDeleteFactionCommand extends SubCommand {
    @Override
    public String getName() {
        return "deletefaction";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Deletes the selected faction.";
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " <faction> <reason>";
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
        addCooldown(p);
        Faction selectedFaction = Playertools.getFactionByName(args[1]);
        if(selectedFaction != null) {
            for (HCFPlayer player : selectedFaction.getMembers()) {
                Player bukkitPlayer = Bukkit.getPlayer(player.getUUID());
                player.setFactionWithoutRank(null);
                final String rankName = player.getRank().getName();
                player.setRank(null);

                //player.removeFaction();


                if (bukkitPlayer != null) {
                    Scoreboards.refresh(bukkitPlayer);
                    NameChanger.refresh(bukkitPlayer);
                }


                PlayerStatistic stat = player.getPlayerStatistic();
                for (FactionHistory f : stat.factionHistory) {
                    if (f.id == selectedFaction.getId()) {
                        f.left = new Date();
                        f.cause = args[2];
                        f.lastRole = rankName;
                        f.name = selectedFaction.getName();
                    }
                }
            }

            addCooldown(p);


            // LogLibrary.sendFactionDisband(p, selectedFaction.name);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(Messages.delete_faction_by_admin
                        .language(onlinePlayer)
                        .setExecutor(p)
                        .setFaction(selectedFaction.getName()).queue());
            }
            Scoreboards.refresh(p);
            GUISound.playSound(p, GUISound.HCFSounds.SUCCESS);
            NameChanger.refreshAll();

            selectedFaction.selfDestruct();
        } else {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
        }
    }
}
