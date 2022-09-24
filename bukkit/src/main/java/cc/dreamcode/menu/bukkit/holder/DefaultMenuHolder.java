package cc.dreamcode.menu.bukkit.holder;

import cc.dreamcode.menu.bukkit.base.DefaultBukkitMenu;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class DefaultMenuHolder implements BukkitMenuHolder {

    private final DefaultBukkitMenu defaultBukkitMenu;
    @Getter private final boolean cancelInventoryClick;
    private final Map<Integer, Consumer<InventoryClickEvent>> inventoryActions = new HashMap<>();

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @Override
    public Inventory getInventory() {
        return this.defaultBukkitMenu.build().getInventory();
    }

    @Override
    public void open(@NonNull HumanEntity humanEntity) {
        humanEntity.openInventory(this.defaultBukkitMenu.build().getInventory());
    }

    @Override
    public void setActionOnSlot(int slot, @NonNull Consumer<InventoryClickEvent> consumer) {
        this.inventoryActions.put(slot, consumer);
    }

    /**
     * Void for event, do not call it - when you do not know what are you doing.
     */
    @Override
    public void handleClick(@NonNull InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent) {
            final InventoryClickEvent inventoryClickEvent = (InventoryClickEvent) event;

            this.inventoryActions.getOrDefault(inventoryClickEvent.getRawSlot(), e -> {
                if (this.cancelInventoryClick) {
                    e.setCancelled(true);
                }
            }).accept(inventoryClickEvent);
        }

        if (this.cancelInventoryClick) {
            event.setCancelled(true);
        }
    }

}
