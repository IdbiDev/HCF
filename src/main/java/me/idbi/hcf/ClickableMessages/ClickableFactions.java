package me.idbi.hcf.ClickableMessages;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ClickableFactions {

    public static void sendList(Player p, Faction f, int number) {
        TextComponent text = new TextComponent(Messages.faction_list.language(p)
                .setNumber(number).setFaction(f).setOnlines(f.countOnlineMembers()).setDTR(f).queue());
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/f show "+f.getName()).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f show " + f.getName()));
        p.spigot().sendMessage(text);
    }
    public static void sendListCustom(Player p, Faction f, int number) {
        TextComponent text = new TextComponent(Messages.faction_custom_list.language(p)
                .setNumber(number).setFaction(f).replace("%type%", "Custom").queue());
//        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/f show "+f.getName()).create()));
//        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f show " + f.getName()));
        p.spigot().sendMessage(text);
    }

    public static void sendTop(Player p, Faction f, int number, int value) {
        TextComponent text = new TextComponent(Messages.faction_top.language(p).setNumber(number).setFaction(f).setValue(value).queue());
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/f show "+f.getName()).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f show " + f.getName()));
        p.spigot().sendMessage(text);
    }

    public static void sendPage(Player p, int currentPage, boolean hasNext, boolean hasPrevious, String command) {

        TextComponent next = new TextComponent("");
        TextComponent previous = new TextComponent("");

        if(hasNext) {
            next = new TextComponent(Messages.faction_chat_page_next.language(p).queue());
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("next").create()));
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command + " " + (currentPage + 1)));
        }
        if(hasPrevious) {
            previous = new TextComponent(Messages.faction_chat_page_previous.language(p).queue());
            previous.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("previous").create()));
            previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command + " " + (Math.max(1, currentPage - 1))));
        }

        TextComponent text = new TextComponent(Messages.faction_chat_page.language(p).setPage(currentPage).queue());
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
        previous.addExtra(text);
        previous.addExtra(next);
        p.spigot().sendMessage(previous);
    }
}
