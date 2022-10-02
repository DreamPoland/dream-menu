package cc.dreamcode.menu.serdes.bukkit.setup;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import lombok.NonNull;
import org.bukkit.entity.HumanEntity;

/**
 * Interface to set up custom menu from config.
 * Here you can apply event on slots, add custom items in real-time and what you want.
 * This class works like consumer class.
 */
public interface MenuSetup {

    BukkitMenu apply(@NonNull HumanEntity humanEntity);

}
