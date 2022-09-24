package cc.dreamcode.menu.bukkit;

import cc.dreamcode.menu.bukkit.base.DefaultBukkitMenu;
import cc.dreamcode.menu.bukkit.listener.BukkitMenuListener;
import cc.dreamcode.menu.core.DreamMenuProvider;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class BukkitMenuProvider implements DreamMenuProvider<DefaultBukkitMenu> {

    public static BukkitMenuProvider create(@NonNull Plugin plugin) {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new BukkitMenuListener(), plugin);

        return new BukkitMenuProvider();
    }

    @Override
    public DefaultBukkitMenu createDefault(@NonNull String title, int rows, boolean cancelInventoryClick) {
        return new DefaultBukkitMenu(title, rows, cancelInventoryClick);
    }
}
