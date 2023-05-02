package cc.dreamcode.menu.bukkit.base;

import cc.dreamcode.menu.bukkit.holder.DefaultMenuHolder;
import cc.dreamcode.menu.core.base.DreamMenu;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.ChatUtil;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public final class BukkitMenu implements DreamMenu<ItemStack, InventoryClickEvent, DefaultMenuHolder, HumanEntity> {

    @Getter private final String title;
    @Getter private final int rows;
    @Getter private final boolean cancelInventoryClick;
    @Getter private final boolean disposeWhenClose;
    @Getter private final Inventory inventory;
    private final DefaultMenuHolder defaultMenuHolder;

    public BukkitMenu(@NonNull String title, int rows, boolean cancelInventoryClick, boolean disposeWhenClose, int page) {

        this.title = ChatUtil.fixColor(title, new MapBuilder<String, Object>()
                .put("page", page)
                .build());

        this.rows = rows;
        this.cancelInventoryClick = cancelInventoryClick;
        this.disposeWhenClose = disposeWhenClose;
        this.defaultMenuHolder = new DefaultMenuHolder(this, cancelInventoryClick, disposeWhenClose);

        this.inventory = Bukkit.createInventory(this.defaultMenuHolder, this.rows > 6 ? 6 * 9 : this.rows * 9, this.title);
    }

    public BukkitMenu(@NonNull String title, @NonNull Map<String, Object> placeholders, int rows, boolean cancelInventoryClick, boolean disposeWhenClose, int page) {
        this.title = ChatUtil.fixColor(title, new MapBuilder<String, Object>()
                .put("page", page)
                .putAll(placeholders)
                .build());

        this.rows = rows;
        this.cancelInventoryClick = cancelInventoryClick;
        this.disposeWhenClose = disposeWhenClose;
        this.defaultMenuHolder = new DefaultMenuHolder(this, cancelInventoryClick, disposeWhenClose);

        this.inventory = Bukkit.createInventory(this.defaultMenuHolder, this.rows > 6 ? 6 * 9 : this.rows * 9, this.title);
    }

    @Override
    public int addItem(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < this.rows * 9; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                return slot;
            }
        }

        return -1;
    }

    @Override
    public int addItem(@NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.rows * 9 - 1; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.defaultMenuHolder.setActionOnSlot(slot, event);
                this.inventory.setItem(slot, itemStack);
                return slot;
            }
        }

        return -1;
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
    public void fillInventoryWith(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < this.rows * 9; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
            }
        }
    }

    @Override
    public void fillInventoryWith(@NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.rows * 9; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                this.defaultMenuHolder.setActionOnSlot(slot, event);
            }
        }
    }

    @Override
    public DefaultMenuHolder getHolder() {
        return this.defaultMenuHolder;
    }

    @Override
    public void open(@NonNull HumanEntity humanEntity) {
        this.getHolder().open(humanEntity);
    }

    public BukkitMenu cloneMenu(int slot) {
        final BukkitMenu bukkitMenu = new BukkitMenu(this.title, this.rows, this.cancelInventoryClick, this.disposeWhenClose, slot);

        bukkitMenu.getInventory().setContents(this.inventory.getContents());
        this.getHolder().getSlotActions().forEach((integer, inventoryClickEventConsumer) ->
                bukkitMenu.getHolder().setActionOnSlot(integer, inventoryClickEventConsumer));

        return bukkitMenu;
    }
}
