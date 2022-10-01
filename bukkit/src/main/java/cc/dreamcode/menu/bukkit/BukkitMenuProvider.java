package cc.dreamcode.menu.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.bukkit.base.BukkitMenuPaginated;
import cc.dreamcode.menu.bukkit.listener.BukkitMenuListener;
import cc.dreamcode.menu.core.DreamMenuProvider;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.function.Consumer;

public final class BukkitMenuProvider implements DreamMenuProvider<BukkitMenu, BukkitMenuPaginated> {

    public static BukkitMenuProvider create(@NonNull Plugin plugin) {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new BukkitMenuListener(), plugin);

        return new BukkitMenuProvider();
    }

    @Override
    public BukkitMenu createDefault(@NonNull String title, int rows, boolean cancelInventoryClick) {
        return new BukkitMenu(title, rows, cancelInventoryClick, 0);
    }

    @Override
    public BukkitMenu createDefault(@NonNull String title, int rows, boolean cancelInventoryClick, @NonNull Consumer<BukkitMenu> consumer) {
        final BukkitMenu bukkitMenu = new BukkitMenu(title, rows, cancelInventoryClick, 0);
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