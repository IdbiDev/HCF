package me.idbi.hcf.Tools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface IItemBuilder {
    static IItemBuilder create(@NotNull Material material) {
        return new IIItemBuilder(material);
    }

    static IItemBuilder create(@NotNull Material material, int count) {
        return new IIItemBuilder(material, count);
    }

    static IItemBuilder create(@NotNull Material material, int count, short damage) {
        return new IIItemBuilder(material, count, damage);
    }

    static IItemBuilder create(@NotNull Material material, int count, short damage, byte data) {
        return new IIItemBuilder(material, count, damage, data);
    }

    static IItemBuilder create(ItemStack item) {
        return new IIItemBuilder(item);
    }

    IItemBuilder setType(@NotNull Material material);

    IItemBuilder setCount(int newCount);

    IItemBuilder setName(@NotNull String name);

    IItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level);

    default IItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this::addEnchantment);

        return this;
    }

    IItemBuilder addLore(String lore);

    IItemBuilder setUnbreakable();

    //IItemBuilder addFlag(@NotNull ItemFlag... flags);

    IItemBuilder addFlag(ItemFlag... flags);

    default IItemBuilder setLore(@NotNull String... lores) {
        Arrays.stream(lores).forEach(this::addLore);
        return this;
    }

    IItemBuilder setLore(@NotNull List<String> lore);

    IItemBuilder removeLore(int line);
    IItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments);

    ItemStack finish();

    boolean isFinished();

}

class IIItemBuilder implements IItemBuilder {

    private final ItemStack is;
    private final ItemMeta meta;
    private boolean finished = false;

    IIItemBuilder(@NotNull ItemStack item) {
        is = item;
        meta = item.getItemMeta();
    }

    IIItemBuilder(@NotNull Material type) {
        this(type, 1);
    }

    IIItemBuilder(@NotNull Material type, @Range(from = 0, to = 64) int amount) {
        this(type, amount, (short) 0);
    }

    IIItemBuilder(@NotNull Material type, @Range(from = 0, to = 64) int amount, short damage) {
        this(type, amount, damage, null);
    }

    @SuppressWarnings("deprecation")
    IIItemBuilder(@NotNull Material type, @Range(from = 0, to = 64) int amount, short damage, @Nullable Byte data) {
        is = new ItemStack(type, amount, damage, data);
        meta = is.getItemMeta();
    }

    public IIItemBuilder setType(@NotNull Material material) {
        is.setType(material);

        return this;
    }

    public IIItemBuilder setCount(@Range(from = 0, to = 64) int newCount) {
        is.setAmount(newCount);
        return this;
    }

    @SuppressWarnings("deprecation")
    public IIItemBuilder setName(@NotNull String name) {
        meta.setDisplayName(format(name));

        return this;
    }

    public IIItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment : enchantments.keySet()) {
            is.removeEnchantment(enchantment);
        }
        is.addUnsafeEnchantments(enchantments);
        return this;
    }

    public IIItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);

        return this;
    }

    @SuppressWarnings("deprecation")
    public IIItemBuilder addLore(@NotNull String lore) {
        List<String> lores = meta.getLore();
        lores = lores == null ? new ArrayList<>() : lores;
        lores.add(format(lore));
        meta.setLore(lores);

        return this;
    }
    public IIItemBuilder setUnbreakable() {
        meta.spigot().setUnbreakable(true);

        return this;
    }

    @Override
    public IItemBuilder setLore(@NotNull List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    @Override
    public IIItemBuilder addFlag(ItemFlag... flag) {
        meta.addItemFlags(flag);

        return this;
    }

    @SuppressWarnings("deprecation")
    public IIItemBuilder removeLore(int line) {
        List<String> lores = meta.getLore();
        lores = lores == null ? new ArrayList<>() : lores;

        lores.remove(Math.min(line, lores.size()));

        meta.setLore(lores);

        return this;
    }

    public ItemStack finish() {
        is.setItemMeta(meta);

        finished = true;
        return is;
    }

    public boolean isFinished() {
        return finished;
    }

    private String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private List<String> format(List<String> message) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < message.size(); i++) {
            list.add(ChatColor.translateAlternateColorCodes('&', message.get(i)));
        }
        return list;
    }
}