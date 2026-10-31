package cc.dreamcode.menu.bukkit;

import cc.dreamcode.menu.DreamMenuBuilder;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
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
    public BukkitMenuBuilder setItem(int slot, @NonNull ItemStack item) {
        final int maxSize = this.getInventoryType().equals(InventoryType.CHEST)
                ? this.getRows() * 9
                : this.getInventoryType().getDefaultSize();

        if (slot >= 0 && slot < maxSize) {
            super.setItem(slot, item);
        }

        return this;
    }

    public BukkitMenuBuilder setItem(int slot, @NonNull ItemBuilder itemBuilder) {
        return this.setItem(slot, itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder setItem(int x, int z, @NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.setItem(x, z, itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder setItem(int[] slots, @NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.setItem(slots, itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder fillBackground(@NonNull ItemBuilder itemBuilder) {
        return this.fillBackground(itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder fillRow(int row, @NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.fillRow(row, itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder fillColumn(int column, @NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.fillColumn(column, itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder fillTop(@NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.fillTop(itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder fillBottom(@NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.fillBottom(itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder fillTopAndBottom(@NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.fillTopAndBottom(itemBuilder.toItemStack());
    }

    public BukkitMenuBuilder fillMargin(@NonNull ItemBuilder itemBuilder) {
        return (BukkitMenuBuilder) this.fillMargin(itemBuilder.toItemStack());
    }

    @Override
    public BukkitMenuBuilder fillBackground(@NonNull ItemStack item) {
        if (!this.getInventoryType().equals(InventoryType.CHEST)) {
            final int defaultSize = this.getInventoryType().getDefaultSize();
            for (int slot = 0; slot < defaultSize; slot++) {
                if (!this.getItems().containsKey(slot)) {
                    this.getItems().put(slot, item);
                }
            }
            return this;
        }

        super.fillBackground(item);
        return this;
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
