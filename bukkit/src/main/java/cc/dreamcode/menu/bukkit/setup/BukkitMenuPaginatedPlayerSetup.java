package cc.dreamcode.menu.bukkit.setup;

import cc.dreamcode.menu.bukkit.base.BukkitMenuPaginated;
import cc.dreamcode.menu.core.setup.MenuPlayerSetup;
import org.bukkit.entity.HumanEntity;

/**
 * Bukkit - Interface to easy build menu by interface - like consumer class.
 * Here you can apply event on slots, add custom items in real-time and what you want.
 * Build by creating new instance. (clean-code style)
 */
public interface BukkitMenuPaginatedPlayerSetup extends MenuPlayerSetup<BukkitMenuPaginated, HumanEntity> {
}
