package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.ClickableMessages.ClickableFactions;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionListGUI.FactionToplistInventory;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FactionListCommand extends SubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "List of online factions";
    }

    @Override
    public String getSyntax() {
        return "/faction list [dtr | [page]] [page]";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        ArrayList<Faction> factions = Playertools.sortByOnlineMembers();
        String type = "Online";
        if(args.length == 1) {
            try {/*
                List<Faction> newFactions = factions.subList(0, Math.min(factions.size(), 10));
                printFactions(p, newFactions, type);*/
                run(p, factions, 1, type);
            } catch (IndexOutOfBoundsException e) {
                p.sendMessage(Messages.cant_find_page.language(p).queue());
            }
            return;
        }

        if(args[1].equalsIgnoreCase("dtr")) {
            factions = Playertools.sortByDTR();
            type = "DTR";
        }

        if(Playertools.isInt(args[1])) {
            int page = Integer.parseInt(args[1]);
            run(p, factions, page, type);

        } else if(args.length == 3) {
            if(Playertools.isInt(args[2])) {
                int page = Integer.parseInt(args[2]);
                run(p, factions, page, type);
            }
        } else {
            run(p, factions, 1, type);
        }
    }

    private void printFactions(Player p, List<Faction> factions, String type) {
        int count = 1;
        p.sendMessage(Messages.faction_list_title.language(p).setType(type).queue());
        for (Faction f : factions) {
            //p.sendMessage(Messages.faction_list.language(p).setNumber(count).setOnlines(f.getOnlineMembers().size()).setFaction(f).queue());
            ClickableFactions.sendList(p, f, count);
            count++;
        }
    }

    private void run(Player p, List<Faction> factions, int page, String type) {
        if (!(page > 0)) {
            p.sendMessage(Messages.cant_find_page.language(p).queue());
            return;
        }
        boolean hasNext = false;
        if(factions.size() > 10 && factions.size() > (page * 10 + 10))
            hasNext = true;

        //p.openInventory(FactionToplistInventory.listInv(p, factions, type, 1));
        if(page == 1) {
            List<Faction> newFactions = factions.subList(0, Math.min(factions.size(), 10));
            printFactions(p, newFactions, type);
            ClickableFactions.sendPage(p, page, hasNext, false, "/f list " + type.toLowerCase());
            return;
        }
        try {
            if (page * 10 <= factions.size()) {
                List<Faction> pageFactions = factions.subList(page * 10, Math.min(page * 10 + 10, factions.size()));
                printFactions(p, pageFactions, type);
                ClickableFactions.sendPage(p, page, hasNext, true, "/f list " + type.toLowerCase());
            } else {
                p.sendMessage(Messages.cant_find_page.language(p).queue());
            }
        } catch (IndexOutOfBoundsException e) {
            p.sendMessage(Messages.cant_find_page.language(p).queue());
        }
    }
}
