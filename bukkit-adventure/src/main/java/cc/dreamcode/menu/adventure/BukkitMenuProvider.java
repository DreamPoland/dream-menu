package cc.dreamcode.menu.adventure;

import cc.dreamcode.menu.DreamMenuProvider;
import cc.dreamcode.menu.adventure.base.BukkitMenu;
import cc.dreamcode.menu.adventure.base.BukkitMenuPaginated;
import cc.dreamcode.menu.adventure.listener.BukkitMenuListener;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.function.Consumer;

public final class BukkitMenuProvider implements DreamMenuProvider<BukkitMenu, InventoryType, BukkitMenuPaginated> {

    private static BukkitMenuProvider INSTANCE;

    public BukkitMenuProvider(@NonNull Plugin plugin) {
        INSTANCE = this;

        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new BukkitMenuListener(), plugin);
    }

    public static BukkitMenuProvider create(@NonNull Plugin plugin) {
        return new BukkitMenuProvider(plugin);
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

    public static BukkitMenuProvider getInstance() {

        if (BukkitMenuProvider.INSTANCE == null) {
            throw new RuntimeException("BukkitMenuProvider not found, make sure it is registered");
        }

        return BukkitMenuProvider.INSTANCE;
    }
}