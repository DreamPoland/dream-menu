package cc.dreamcode.menu.bukkit.listener;

import cc.dreamcode.menu.bukkit.holder.BukkitMenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BukkitMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        final Inventory inventory = e.getInventory();
        if (!inventory.getType().equals(InventoryType.CHEST)) {
            return;
        }

        InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder) inventoryHolder;
            bukkitMenuHolder.handleClick(e);
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent e) {
        final Inventory inventory = e.getInventory();
        if (!inventory.getType().equals(InventoryType.CHEST)) {
            return;
        }

        InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder) inventoryHolder;
            bukkitMenuHolder.handleClick(e);
        }
    }
}
