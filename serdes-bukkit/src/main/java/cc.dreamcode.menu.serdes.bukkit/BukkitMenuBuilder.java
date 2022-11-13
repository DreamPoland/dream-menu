package cc.dreamcode.menu.serdes.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.utilities.bukkit.ChatUtil;
import cc.dreamcode.utilities.bukkit.builders.ItemBuilder;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class BukkitMenuBuilder {

    private final String name;
    private final int rows;
    private final boolean cancelInventoryClick;
    private final Map<Integer, ItemStack> items;

    public BukkitMenu build() {
        return new BukkitMenu(
                ChatColor.translateAlternateColorCodes('&', this.name),
                this.rows,
                this.cancelInventoryClick,
                0
        );
    }

    public BukkitMenu build(@NonNull Map<String, Object> replaceMap) {
        final CompiledMessage compiledMessage = CompiledMessage.of(this.name);
        final PlaceholderContext placeholderContext = PlaceholderContext.of(compiledMessage);

        return new BukkitMenu(
                ChatUtil.fixColor(placeholderContext.with(replaceMap).apply()),
                this.rows,
                this.cancelInventoryClick,
                0
        );
    }

    public BukkitMenu buildWithItem() {
        final BukkitMenu bukkitMenu = new BukkitMenu(
                ChatColor.translateAlternateColorCodes('&', this.name),
                this.rows,
                this.cancelInventoryClick,
                0
        );

        this.items.forEach(((integer, itemStack) ->
                bukkitMenu.setItem(integer, new ItemBuilder(itemStack)
                        .fixColors()
                        .toItemStack())));

        return bukkitMenu;
    }

    public BukkitMenu buildWithItem(@NonNull Map<String, Object> replaceMap) {
        final CompiledMessage compiledMessage = CompiledMessage.of(this.name);
        final PlaceholderContext placeholderContext = PlaceholderContext.of(compiledMessage);

        final BukkitMenu bukkitMenu = new BukkitMenu(
                ChatUtil.fixColor(placeholderContext.with(replaceMap).apply()),
                this.rows,
                this.cancelInventoryClick,
                0
        );

        this.items.forEach(((integer, itemStack) ->
                bukkitMenu.setItem(integer, new ItemBuilder(itemStack)
                        .fixColors(replaceMap)
                        .toItemStack())));

        return bukkitMenu;
    }

}
