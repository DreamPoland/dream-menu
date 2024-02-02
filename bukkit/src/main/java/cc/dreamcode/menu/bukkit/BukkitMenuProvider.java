package cc.dreamcode.menu.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.bukkit.base.BukkitMenuPaginated;
import cc.dreamcode.menu.bukkit.listener.BukkitMenuListener;
import cc.dreamcode.menu.core.DreamMenuProvider;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.function.Consumer;

public final class BukkitMenuProvider implements DreamMenuProvider<BukkitMenu, InventoryType, BukkitMenuPaginated> {

    public static BukkitMenuProvider create(@NonNull Plugin plugin) {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new BukkitMenuListener(), plugin);

        return new BukkitMenuProvider();
    }

    @Override
    public BukkitMenu createDefault(@NonNull String title, int rows) {
        return new BukkitMenu(title, rows, 0);
    }

    @Override
    public BukkitMenu createDefault(@NonNull String title, int rows, @NonNull Consumer<BukkitMenu> consumer) {
        final BukkitMenu bukkitMenu = new BukkitMenu(title, rows, 0);
        consumer.accept(bukkitMenu);

        return bukkitMenu;
    }

    @Override
    public BukkitMenu createDefault(@NonNull InventoryType type, @NonNull String title) {
        return new BukkitMenu(type, title, 0);
    }

    @Override
    public BukkitMenu createDefault(@NonNull InventoryType type, @NonNull String title, @NonNull Consumer<BukkitMenu> consumer) {
        final BukkitMenu bukkitMenu = new BukkitMenu(type, title, 0);
        consumer.accept(bukkitMenu);

        return bukkitMenu;
    }

    @Override
    public BukkitMenuPaginated createPaginated(@NonNull BukkitMenu bukkitMenu) {
        return new BukkitMenuPaginated(bukkitMenu);
    }

    @Override
    public BukkitMenuPaginated createPaginated(@NonNull BukkitMenu bukkitMenu, @NonNull Consumer<BukkitMenu> consumer) {
        consumer.accept(bukkitMenu);

        return new BukkitMenuPaginated(bukkitMenu);
    }

}