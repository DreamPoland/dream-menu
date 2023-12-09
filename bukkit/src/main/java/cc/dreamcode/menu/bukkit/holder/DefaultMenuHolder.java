package cc.dreamcode.menu.bukkit.holder;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class DefaultMenuHolder implements BukkitMenuHolder {

    private final BukkitMenu bukkitMenu;
    @Getter @Setter private boolean cancelInventoryClick = true;
    @Getter @Setter private boolean disposeWhenClose = false;

    @Getter @Setter private Consumer<InventoryClickEvent> preInventoryClickEvent;
    @Getter @Setter private Consumer<InventoryClickEvent> postInventoryClickEvent;
    @Getter @Setter private Consumer<InventoryCloseEvent> inventoryCloseEvent;

    private final Map<Integer, Consumer<InventoryClickEvent>> inventoryActions = new HashMap<>();

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @Override
    public Inventory getInventory() {
        return this.bukkitMenu.getInventory();
    }

    @Override
    public void open(@NonNull HumanEntity humanEntity) {
        humanEntity.openInventory(this.bukkitMenu.getInventory());
    }

    @Override
    public Map<Integer, Consumer<InventoryClickEvent>> getSlotActions() {
        return this.inventoryActions;
    }

    @Override
    public void setActionOnSlot(int slot, @NonNull Consumer<InventoryClickEvent> consumer) {
        this.inventoryActions.put(slot, consumer);
    }

    @Override
    public void removeActionOnSlot(int slot) {
        this.inventoryActions.remove(slot);
    }

    /**
     * Void for event, do not call it - when you do not know what are you doing.
     */
    @Override
    public void handleClick(@NonNull InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent) {
            final InventoryClickEvent inventoryClickEvent = (InventoryClickEvent) event;

            if (this.preInventoryClickEvent != null) {
                this.preInventoryClickEvent.accept(inventoryClickEvent);
            }

            this.inventoryActions.getOrDefault(inventoryClickEvent.getRawSlot(), e -> {
                if (this.cancelInventoryClick) {
                    e.setCancelled(true);
                }
            }).accept(inventoryClickEvent);

            if (this.postInventoryClickEvent != null) {
                this.postInventoryClickEvent.accept(inventoryClickEvent);
            }
        }

        if (this.cancelInventoryClick) {
            event.setCancelled(true);
        }
    }

    /**
     * Void for event, do not call it - when you do not know what are you doing.
     */
    @Override
    public void handleClose(@NonNull InventoryCloseEvent event) {
        if (this.inventoryCloseEvent != null) {
            this.inventoryCloseEvent.accept(event);
        }

        if (this.disposeWhenClose) {
            this.dispose();
        }
    }

    @Override
    public void dispose() {
        this.getInventory().clear();
        this.inventoryActions.clear();
    }

}
