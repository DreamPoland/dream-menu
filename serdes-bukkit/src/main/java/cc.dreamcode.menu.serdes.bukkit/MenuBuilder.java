package cc.dreamcode.menu.serdes.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.serdes.bukkit.helper.ItemHelper;
import cc.dreamcode.menu.serdes.bukkit.helper.PlaceholderHelper;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class MenuBuilder {

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

    public BukkitMenu build(@NonNull Map<String, String> replaceMap) {
        return new BukkitMenu(
                new PlaceholderHelper(
                    ChatColor.translateAlternateColorCodes('&', this.name)
                ).replaceFromMap(replaceMap),
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
                bukkitMenu.setItem(integer, new ItemHelper(itemStack).fixColors(null))));

        return bukkitMenu;
    }

    public BukkitMenu buildWithItem(@NonNull Map<String, String> replaceMap) {
        final BukkitMenu bukkitMenu = new BukkitMenu(
                new PlaceholderHelper(
                        ChatColor.translateAlternateColorCodes('&', this.name)
                ).replaceFromMap(replaceMap),
                this.rows,
                this.cancelInventoryClick,
                0
        );

        this.items.forEach(((integer, itemStack) ->
                bukkitMenu.setItem(integer, new ItemHelper(itemStack).fixColors(replaceMap))));

        return bukkitMenu;
    }

}
