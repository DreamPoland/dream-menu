package cc.dreamcode.menu.bukkit.holder;

import cc.dreamcode.menu.core.holder.DreamMenuHolder;
import lombok.NonNull;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;
import java.util.function.Consumer;

public interface BukkitMenuHolder extends DreamMenuHolder<HumanEntity>, InventoryHolder {

    Map<Integer, Consumer<InventoryClickEvent>> getSlotActions();

    void setActionOnSlot(int slot, @NonNull Consumer<InventoryClickEvent> consumer);

    void removeActionOnSlot(int slot);

    /**
     * Void for event, do not call it - when you do not know what are you doing.
     */
    void handleClick(@NonNull InventoryInteractEvent event);
}
