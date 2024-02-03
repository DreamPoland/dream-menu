package cc.dreamcode.menu.bukkit.listener;

import cc.dreamcode.menu.bukkit.holder.BukkitMenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class BukkitMenuListener implements Listener {

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }

        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder) inventoryHolder;
            bukkitMenuHolder.handleClick(event);
        }
    }

    @EventHandler
    private void onInventoryInteract(InventoryInteractEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }

        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder) inventoryHolder;
            bukkitMenuHolder.handleClick(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }

        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder) inventoryHolder;
            bukkitMenuHolder.handleClose(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }

        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder) inventoryHolder;
            bukkitMenuHolder.handleDrag(event);
        }
    }
}