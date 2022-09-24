package cc.dreamcode.menu.bukkit.base;

import cc.dreamcode.menu.bukkit.holder.DefaultMenuHolder;
import cc.dreamcode.menu.core.DreamMenu;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public final class DefaultBukkitMenu implements DreamMenu<ItemStack, InventoryClickEvent, DefaultMenuHolder> {

    @Getter private final int rows;
    @Getter private final Inventory inventory;
    private final DefaultMenuHolder defaultMenuHolder;

    public DefaultBukkitMenu(@NonNull String title, int rows, boolean cancelInventoryClick) {
        this.rows = rows;
        this.defaultMenuHolder = new DefaultMenuHolder(this, cancelInventoryClick);
        this.inventory = Bukkit.createInventory(this.defaultMenuHolder, rows > 6 ? 6 * 9 : rows * 9, title);
    }

    @Override
    public void addItem(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < this.rows * 9; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                return;
            }
        }
    }

    @Override
    public void addItem(@NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.rows * 9 - 1; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.defaultMenuHolder.setActionOnSlot(slot, event);
                this.inventory.setItem(slot, itemStack);
                return;
            }
        }
    }


    @Override
    public void setItem(int slot, @NonNull ItemStack itemStack) {
        this.inventory.setItem(slot, itemStack);
    }

    @Override
    public void setItem(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        this.defaultMenuHolder.setActionOnSlot(slot, event);
        this.inventory.setItem(slot, itemStack);
    }

    @Override
    public DefaultMenuHolder build() {
        return this.defaultMenuHolder;
    }

}
