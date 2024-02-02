package cc.dreamcode.menu.bukkit.base;

import cc.dreamcode.menu.bukkit.holder.DefaultMenuHolder;
import cc.dreamcode.menu.core.base.DreamMenu;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.StringColorUtil;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class BukkitMenu implements DreamMenu<ItemStack, InventoryClickEvent, DefaultMenuHolder, HumanEntity> {

    @Getter private final InventoryType inventoryType;
    @Getter private final String title;
    @Getter private final int size;
    @Getter private final Map<String, Object> placeholders;
    @Getter private final Inventory inventory;

    @Getter private boolean cancelInventoryClick = true;
    @Getter private boolean disposeWhenClose = false;

    @Getter private Consumer<InventoryClickEvent> inventoryClickEvent;
    @Getter private Consumer<InventoryClickEvent> postInventoryClickEvent;
    @Getter private Consumer<InventoryDragEvent> inventoryDragEvent;
    @Getter private Consumer<InventoryCloseEvent> inventoryCloseEvent;

    private final DefaultMenuHolder defaultMenuHolder;

    public BukkitMenu(@NonNull InventoryType inventoryType, @NonNull String title, int page) {
        this.inventoryType = inventoryType;
        this.title = title;
        this.size = inventoryType.getDefaultSize();
        this.placeholders = new HashMap<>();
        this.defaultMenuHolder = new DefaultMenuHolder(this);

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.inventoryType,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .build())
        );
    }

    public BukkitMenu(@NonNull InventoryType inventoryType, @NonNull String title, @NonNull Map<String, Object> placeholders, int page) {
        this.inventoryType = inventoryType;
        this.title = title;
        this.size = inventoryType.getDefaultSize();
        this.placeholders = placeholders;
        this.defaultMenuHolder = new DefaultMenuHolder(this);

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.inventoryType,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .putAll(placeholders)
                        .build())
        );
    }

    /**
     * Create empty menu with type {@link InventoryType#CHEST}
     */
    public BukkitMenu(@NonNull String title, int rows, int page) {
        this.inventoryType = InventoryType.CHEST;
        this.title = title;
        this.size = rows > 6 ? 6 * 9 : rows * 9;
        this.placeholders = new HashMap<>();
        this.defaultMenuHolder = new DefaultMenuHolder(this);

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.size,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .build())
        );
    }

    /**
     * Create empty menu with type {@link InventoryType#CHEST}
     */
    public BukkitMenu(@NonNull String title, @NonNull Map<String, Object> placeholders, int rows, int page) {
        this.inventoryType = InventoryType.CHEST;
        this.title = title;
        this.size = rows > 6 ? 6 * 9 : rows * 9;
        this.placeholders = placeholders;
        this.defaultMenuHolder = new DefaultMenuHolder(this);

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.size,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .putAll(placeholders)
                        .build())
        );
    }

    @Override
    public int addItem(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < this.size; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                return slot;
            }
        }

        return -1;
    }

    @Override
    public int addItem(@NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.size; slot++) {
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
    public void setItem(int[] slots, @NonNull ItemStack itemStack) {
        for (int slot : slots) {
            this.setItem(slot, itemStack);
        }
    }

    @Override
    public void setItem(int[] slots, @NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot : slots) {
            this.setItem(slot, itemStack, event);
        }
    }

    @Override
    public void fillInventoryWith(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < this.size; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
            }
        }
    }

    @Override
    public void fillInventoryWith(@NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.size; slot++) {
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

    @Override
    public void dispose() {
        this.getHolder().dispose();
    }

    public BukkitMenu cloneMenu(int slot) {
        final BukkitMenu bukkitMenu;
        if (this.inventoryType.equals(InventoryType.CHEST)) {
            bukkitMenu = new BukkitMenu(this.title, this.placeholders, this.size, slot);
        }
        else {
            bukkitMenu = new BukkitMenu(this.inventoryType, this.title, this.placeholders, slot);
        }

        bukkitMenu.setCancelInventoryClick(this.cancelInventoryClick);
        bukkitMenu.setDisposeWhenClose(this.disposeWhenClose);

        bukkitMenu.getInventory().setContents(this.inventory.getContents());
        this.getHolder().getSlotActions().forEach((integer, inventoryClickEventConsumer) ->
                bukkitMenu.getHolder().setActionOnSlot(integer, inventoryClickEventConsumer));

        return bukkitMenu;
    }

    public void setCancelInventoryClick(boolean cancelInventoryClick) {
        this.cancelInventoryClick = cancelInventoryClick;
        this.defaultMenuHolder.setCancelInventoryClick(cancelInventoryClick);
    }

    public void setDisposeWhenClose(boolean disposeWhenClose) {
        this.disposeWhenClose = disposeWhenClose;
        this.defaultMenuHolder.setDisposeWhenClose(disposeWhenClose);
    }

    public void setInventoryCloseEvent(Consumer<InventoryCloseEvent> inventoryCloseEvent) {
        this.inventoryCloseEvent = inventoryCloseEvent;
        this.defaultMenuHolder.setInventoryCloseEvent(inventoryCloseEvent);
    }

    public void setInventoryClickEvent(Consumer<InventoryClickEvent> inventoryClickEvent) {
        this.inventoryClickEvent = inventoryClickEvent;
        this.defaultMenuHolder.setInventoryClickEvent(inventoryClickEvent);
    }

    public void setPostInventoryClickEvent(Consumer<InventoryClickEvent> postInventoryClickEvent) {
        this.postInventoryClickEvent = postInventoryClickEvent;
        this.defaultMenuHolder.setPostInventoryClickEvent(postInventoryClickEvent);
    }

    public void setInventoryDragEvent(Consumer<InventoryDragEvent> inventoryDragEvent) {
        this.inventoryDragEvent = inventoryDragEvent;
        this.defaultMenuHolder.setInventoryDragEvent(inventoryDragEvent);
    }
}
