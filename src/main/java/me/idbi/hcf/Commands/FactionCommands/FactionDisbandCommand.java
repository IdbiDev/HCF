package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.*;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class FactionDisbandCommand extends SubCommand {
    @Override
    public String getName() {
        return "disband";
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
        return "/faction disband <faction>";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
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
        Faction selectedFaction = Playertools.getPlayerFaction(p);
        if (selectedFaction == null) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
            return;
        }
        if (Playertools.getFactionByName(args[1]) != selectedFaction) {
            p.sendMessage(Messages.not_found_faction.language(p).queue());
            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
            return;
        }


        if (!Objects.equals(selectedFaction.getLeader(), p.getUniqueId().toString())) {
            p.sendMessage(Messages.not_leader.language(p).queue());
            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
            return;
        }
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        hcfPlayer.setChatType(ChatTypes.PUBLIC);

        if(!hcfPlayer.getFaction().getClaims().isEmpty()) {
            double backmoney = Claiming.calculateMoneyFromClaim(hcfPlayer.getFaction());
            hcfPlayer.addMoney(Math.toIntExact(Math.round(backmoney)));
            hcfPlayer.getFaction().clearClaims(); // done

        }
//        displayTeams.removePlayerFromTeam(p);
//        displayTeams.removeTeam(selectedFaction);
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
                    f.cause = "Disbanded";
                    f.lastRole = rankName;
                    f.name = selectedFaction.getName();
                }
            }
        }

        addCooldown(p);


        // LogLibrary.sendFactionDisband(p, selectedFaction.name);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(Messages.faction_disband
                    .language(onlinePlayer)
                    .setPlayer(p)
                    .setFaction(selectedFaction.getName()).queue());
        }

        Scoreboards.refresh(p);
        GUISound.playSound(p, GUISound.HCFSounds.SUCCESS);
        NameChanger.refresh(p);
        NameChanger.refreshAll();

        selectedFaction.selfDestruct();
        //Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(p).setFaction(selectedFaction.factioname).queue());
    }
}
