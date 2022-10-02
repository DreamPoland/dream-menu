package cc.dreamcode.menu.serdes.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenuPaginated;
import lombok.NonNull;
import org.bukkit.entity.HumanEntity;

/**
 * Interface to set up custom paginated menu from config.
 * Here you can apply event on slots, add custom items in real-time and what you want.
 * This class works like consumer class.
 */
public interface MenuPaginatedSetup {

    BukkitMenuPaginated apply(@NonNull HumanEntity humanEntity);

}
