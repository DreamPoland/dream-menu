package cc.dreamcode.menu.adventure.base;

import cc.dreamcode.menu.adventure.holder.DefaultMenuHolder;
import cc.dreamcode.menu.base.DreamMenu;
import cc.dreamcode.menu.utilities.MenuUtil;
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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class BukkitMenu implements DreamMenu<BukkitMenu, BukkitMenuPaginated, ItemStack, InventoryClickEvent, DefaultMenuHolder, HumanEntity> {

    @Getter private final InventoryType inventoryType;
    @Getter private final String title;
    @Getter private final int size;
    @Getter private final Map<String, Object> placeholders;
    @Getter private final Inventory inventory;
    @Getter private final boolean colorizePlaceholders;

    @Getter private final int page;

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
        this.colorizePlaceholders = false;

        this.page = page;

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.inventoryType,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .build(), false)
        );
    }

    public BukkitMenu(@NonNull InventoryType inventoryType, @NonNull String title, @NonNull Map<String, Object> placeholders, int page) {
        this.inventoryType = inventoryType;
        this.title = title;
        this.size = inventoryType.getDefaultSize();
        this.placeholders = placeholders;
        this.defaultMenuHolder = new DefaultMenuHolder(this);
        this.colorizePlaceholders = true;

        this.page = page;

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.inventoryType,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .putAll(placeholders)
                        .build(), true)
        );
    }

    public BukkitMenu(@NonNull InventoryType inventoryType, @NonNull String title, @NonNull Map<String, Object> placeholders, int page, boolean colorizePlaceholders) {
        this.inventoryType = inventoryType;
        this.title = title;
        this.size = inventoryType.getDefaultSize();
        this.placeholders = placeholders;
        this.defaultMenuHolder = new DefaultMenuHolder(this);
        this.colorizePlaceholders = colorizePlaceholders;

        this.page = page;

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.inventoryType,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .putAll(placeholders)
                        .build(), colorizePlaceholders)
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
        this.colorizePlaceholders = false;

        this.page = page;

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.size,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .build(), false)
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
        this.colorizePlaceholders = true;

        this.page = page;

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.size,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .putAll(placeholders)
                        .build(), true)
        );
    }

    /**
     * Create empty menu with type {@link InventoryType#CHEST}
     */
    public BukkitMenu(@NonNull String title, @NonNull Map<String, Object> placeholders, int rows, int page, boolean colorizePlaceholders) {
        this.inventoryType = InventoryType.CHEST;
        this.title = title;
        this.size = rows > 6 ? 6 * 9 : rows * 9;
        this.placeholders = placeholders;
        this.defaultMenuHolder = new DefaultMenuHolder(this);
        this.colorizePlaceholders = colorizePlaceholders;

        this.page = page;

        this.inventory = Bukkit.createInventory(
                this.defaultMenuHolder,
                this.size,
                StringColorUtil.fixColor(title, new MapBuilder<String, Object>()
                        .put("page", page)
                        .putAll(placeholders)
                        .build(), colorizePlaceholders)
        );
    }

    @Override
    public BukkitMenu addItem(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < this.size; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                return this;
            }
        }

        return this;
    }

    @Override
    public BukkitMenu addItem(@NonNull ItemStack itemStack, @NonNull List<Integer> applySlots) {
        for (int slot = 0; slot < this.size; slot++) {
            if (!applySlots.contains(slot)) {
                continue;
            }

            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                return this;
            }
        }

        return this;
    }

    @Override
    public BukkitMenu addItem(@NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.size; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.defaultMenuHolder.setActionOnSlot(slot, event);
                this.inventory.setItem(slot, itemStack);
                return this;
            }
        }

        return this;
    }

    @Override
    public BukkitMenu addItem(@NonNull ItemStack itemStack, @NonNull List<Integer> applySlots, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.size; slot++) {
            if (!applySlots.contains(slot)) {
                continue;
            }

            if (this.inventory.getItem(slot) == null) {
                this.defaultMenuHolder.setActionOnSlot(slot, event);
                this.inventory.setItem(slot, itemStack);
                return this;
            }
        }

        return this;
    }


    @Override
    public BukkitMenu setItem(int slot, @NonNull ItemStack itemStack) {
        this.inventory.setItem(slot, itemStack);

        return this;
    }

    @Override
    public BukkitMenu setItem(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        this.defaultMenuHolder.setActionOnSlot(slot, event);
        this.inventory.setItem(slot, itemStack);

        return this;
    }

    @Override
    public BukkitMenu setItem(int x, int z, @NonNull ItemStack itemStack) {
        int slot = MenuUtil.countSlot(x, z);
        this.setItem(slot, itemStack);

        return this;
    }

    @Override
    public BukkitMenu setItem(int x, int z, @NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        int slot = MenuUtil.countSlot(x, z);
        this.setItem(slot, itemStack, event);

        return this;
    }

    @Override
    public BukkitMenu setItem(int[] slots, @NonNull ItemStack itemStack) {
        for (int slot : slots) {
            this.setItem(slot, itemStack);
        }

        return this;
    }

    @Override
    public BukkitMenu setItem(int[] slots, @NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot : slots) {
            this.setItem(slot, itemStack, event);
        }

        return this;
    }

    @Override
    public BukkitMenu fillInventoryWith(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < this.size; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
            }
        }

        return this;
    }

    @Override
    public BukkitMenu fillInventoryWith(@NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> event) {
        for (int slot = 0; slot < this.size; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                this.defaultMenuHolder.setActionOnSlot(slot, event);
            }
        }

        return this;
    }

    @Override
    public DefaultMenuHolder getHolder() {
        return this.defaultMenuHolder;
    }

    @Override
    public BukkitMenu open(@NonNull HumanEntity humanEntity) {
        this.getHolder().open(humanEntity);

        return this;
    }

    @Override
    public BukkitMenuPaginated toPaginated() {
        return new BukkitMenuPaginated(this);
    }

    @Override
    public BukkitMenu dispose() {
        this.getHolder().dispose();

        return this;
    }

    public BukkitMenu cloneMenu(int slot) {
        final BukkitMenu bukkitMenu;
        if (this.inventoryType.equals(InventoryType.CHEST)) {
            bukkitMenu = new BukkitMenu(this.title, this.placeholders, this.size / 9, slot, this.colorizePlaceholders);
        }
        else {
            bukkitMenu = new BukkitMenu(this.inventoryType, this.title, this.placeholders, slot, this.colorizePlaceholders);
        }

        bukkitMenu.setCancelInventoryClick(this.cancelInventoryClick);
        bukkitMenu.setDisposeWhenClose(this.disposeWhenClose);

        bukkitMenu.setInventoryClickEvent(this.inventoryClickEvent);
        bukkitMenu.setPostInventoryClickEvent(this.postInventoryClickEvent);
        bukkitMenu.setInventoryDragEvent(this.inventoryDragEvent);
        bukkitMenu.setInventoryCloseEvent(this.inventoryCloseEvent);

        bukkitMenu.getInventory().setContents(this.inventory.getContents());
        this.getHolder().getSlotActions().forEach((integer, inventoryClickEventConsumer) ->
                bukkitMenu.getHolder().setActionOnSlot(integer, inventoryClickEventConsumer));

        return bukkitMenu;
    }

    public BukkitMenu setCancelInventoryClick(boolean cancelInventoryClick) {
        this.cancelInventoryClick = cancelInventoryClick;
        this.defaultMenuHolder.setCancelInventoryClick(cancelInventoryClick);

        return this;
    }

    public BukkitMenu setDisposeWhenClose(boolean disposeWhenClose) {
        this.disposeWhenClose = disposeWhenClose;
        this.defaultMenuHolder.setDisposeWhenClose(disposeWhenClose);

        return this;
    }

    public BukkitMenu setInventoryCloseEvent(Consumer<InventoryCloseEvent> inventoryCloseEvent) {
        this.inventoryCloseEvent = inventoryCloseEvent;
        this.defaultMenuHolder.setInventoryCloseEvent(inventoryCloseEvent);

        return this;
    }

    public BukkitMenu setInventoryClickEvent(Consumer<InventoryClickEvent> inventoryClickEvent) {
        this.inventoryClickEvent = inventoryClickEvent;
        this.defaultMenuHolder.setInventoryClickEvent(inventoryClickEvent);

        return this;
    }

    public BukkitMenu setPostInventoryClickEvent(Consumer<InventoryClickEvent> postInventoryClickEvent) {
        this.postInventoryClickEvent = postInventoryClickEvent;
        this.defaultMenuHolder.setPostInventoryClickEvent(postInventoryClickEvent);

        return this;
    }

    public BukkitMenu setInventoryDragEvent(Consumer<InventoryDragEvent> inventoryDragEvent) {
        this.inventoryDragEvent = inventoryDragEvent;
        this.defaultMenuHolder.setInventoryDragEvent(inventoryDragEvent);

        return this;
    }
}
