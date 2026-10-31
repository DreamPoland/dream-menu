package cc.dreamcode.menu.bukkit.holder;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class DefaultMenuHolder implements BukkitMenuHolder {

    @Getter private final BukkitMenu bukkitMenu;

    @Getter @Setter private boolean cancelInventoryClick = true;
    @Getter @Setter private boolean disposeWhenClose = false;

    @Getter @Setter private Consumer<InventoryClickEvent> inventoryClickEvent;
    @Getter @Setter private Consumer<InventoryClickEvent> postInventoryClickEvent;
    @Getter @Setter private Consumer<InventoryCloseEvent> inventoryCloseEvent;
    @Getter @Setter private Consumer<InventoryDragEvent> inventoryDragEvent;

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

    @Override
    public void handleClick(@NonNull InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent) {
            final InventoryClickEvent inventoryClickEvent = (InventoryClickEvent) event;

            if (this.cancelInventoryClick) {
                inventoryClickEvent.setCancelled(true);
            }

            if (this.inventoryClickEvent != null) {
                this.inventoryClickEvent.accept(inventoryClickEvent);
            }

            final int rawSlot = inventoryClickEvent.getRawSlot();
            if (rawSlot >= 0 && rawSlot < this.getInventory().getSize()) {
                final Consumer<InventoryClickEvent> action = this.inventoryActions.get(rawSlot);
                if (action != null) {
                    action.accept(inventoryClickEvent);
                }
            }

            if (this.postInventoryClickEvent != null) {
                this.postInventoryClickEvent.accept(inventoryClickEvent);
            }
        } else if (this.cancelInventoryClick) {
            event.setCancelled(true);
        }
    }

    @Override
    public void handleClose(@NonNull InventoryCloseEvent event) {
        if (this.inventoryCloseEvent != null) {
            this.inventoryCloseEvent.accept(event);
        }

        if (this.disposeWhenClose) {
            final int viewerCount = this.getInventory().getViewers().size();
            if (viewerCount == 0 || (viewerCount == 1 && this.getInventory().getViewers().contains(event.getPlayer()))) {
                this.dispose();
            }
        }
    }

    @Override
    public void handleDrag(@NonNull InventoryDragEvent event) {
        if (this.cancelInventoryClick) {
            final int topInventorySize = this.getInventory().getSize();
            for (int rawSlot : event.getRawSlots()) {
                if (rawSlot < topInventorySize) {
                    event.setCancelled(true);
                    break;
                }
            }
        }

        if (this.inventoryDragEvent != null) {
            this.inventoryDragEvent.accept(event);
        }
    }

    @Override
    public void dispose() {
        new ArrayList<>(this.getInventory().getViewers()).forEach(HumanEntity::closeInventory);
        this.getInventory().clear();
        this.inventoryActions.clear();
    }
}
