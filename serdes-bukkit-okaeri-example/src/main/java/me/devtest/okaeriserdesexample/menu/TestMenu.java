package me.devtest.okaeriserdesexample.menu;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.serdes.bukkit.MenuBuilder;
import cc.dreamcode.menu.serdes.bukkit.MenuSetup;
import cc.dreamcode.menu.serdes.bukkit.helper.ItemHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.devtest.okaeriserdesexample.PluginConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;

@RequiredArgsConstructor
public class TestMenu implements MenuSetup {

    private final PluginConfig pluginConfig;

    @Override
    public BukkitMenu apply(@NonNull HumanEntity humanEntity) {
        final MenuBuilder menuBuilder = this.pluginConfig.menuBuilder;
        final BukkitMenu bukkitMenu = menuBuilder.build(); // or with items

        menuBuilder.getItems().forEach((integer, itemStack) -> { // scan all config items
            if (this.pluginConfig.alertSlot == integer) {
                bukkitMenu.setItem(integer, new ItemHelper(itemStack).fixColors(null), e ->
                        humanEntity.sendMessage(ChatColor.translateAlternateColorCodes('&', this.pluginConfig.alertNotice)));
                return;
            }

            bukkitMenu.setItem(integer, new ItemHelper(itemStack).fixColors(null));
        });

        return bukkitMenu;
    }
}
