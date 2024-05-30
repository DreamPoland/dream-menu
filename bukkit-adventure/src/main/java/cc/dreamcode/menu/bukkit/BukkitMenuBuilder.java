package cc.dreamcode.menu.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.DreamMenuBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BukkitMenuBuilder extends DreamMenuBuilder<BukkitMenu, InventoryType, ItemStack> {

    public BukkitMenuBuilder(@NonNull String name, int rows, Map<Integer, ItemStack> items) {
        super(InventoryType.CHEST, name, rows, items == null ? new HashMap<>() : items);
    }

    public BukkitMenuBuilder(@NonNull InventoryType inventoryType, @NonNull String name, Map<Integer, ItemStack> items) {
        super(inventoryType, name, inventoryType.getDefaultSize(), items == null ? new HashMap<>() : items);
    }

    @Override
    public BukkitMenu buildEmpty() {
        if (this.getInventoryType().equals(InventoryType.CHEST)) {
            return new BukkitMenu(this.getName(), this.getRows(), 0);
        }

        return new BukkitMenu(this.getInventoryType(), this.getName(), 0);
    }

    @Override
    public BukkitMenu buildEmpty(@NonNull Map<String, Object> replaceMap) {
        if (this.getInventoryType().equals(InventoryType.CHEST)) {
            return new BukkitMenu(this.getName(), replaceMap, this.getRows(), 0);
        }

        return new BukkitMenu(this.getInventoryType(), this.getName(), replaceMap, 0);
    }

    @Override
    public BukkitMenu buildWithItems() {
        final BukkitMenu bukkitMenu = this.buildEmpty();

        this.getItems().forEach((slot, item) ->
                bukkitMenu.setItem(slot, ItemBuilder.of(item)
                        .fixColors()
                        .toItemStack()));

        return bukkitMenu;
    }

    @Override
    public BukkitMenu buildWithItems(@NonNull Map<String, Object> replaceMap) {
        final BukkitMenu bukkitMenu = this.buildEmpty(replaceMap);

        this.getItems().forEach((slot, item) ->
                bukkitMenu.setItem(slot, ItemBuilder.of(item)
                        .fixColors(replaceMap)
                        .toItemStack()));

        return bukkitMenu;
    }
}
