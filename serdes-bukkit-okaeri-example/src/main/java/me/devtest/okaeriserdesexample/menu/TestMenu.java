package me.devtest.okaeriserdesexample.menu;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.bukkit.setup.BukkitMenuSetup;
import cc.dreamcode.menu.serdes.bukkit.BukkitMenuBuilder;
import cc.dreamcode.utilities.bukkit.builders.ItemBuilder;
import lombok.RequiredArgsConstructor;
import me.devtest.okaeriserdesexample.PluginConfig;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public class TestMenu implements BukkitMenuSetup {

    private final PluginConfig pluginConfig;

    @Override
    public BukkitMenu build() {
        final BukkitMenuBuilder bukkitMenuBuilder = this.pluginConfig.bukkitMenuBuilder;
        final BukkitMenu bukkitMenu = bukkitMenuBuilder.build(); // or with items

        bukkitMenuBuilder.getItems().forEach((integer, itemStack) -> { // scan all config items
            if (this.pluginConfig.alertSlot == integer) {
                bukkitMenu.setItem(integer, new ItemBuilder(itemStack).fixColors().toItemStack(), e ->
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', this.pluginConfig.alertNotice)));
                return;
            }

            bukkitMenu.setItem(integer, new ItemBuilder(itemStack).fixColors().toItemStack());
        });

        return bukkitMenu;
    }
}
