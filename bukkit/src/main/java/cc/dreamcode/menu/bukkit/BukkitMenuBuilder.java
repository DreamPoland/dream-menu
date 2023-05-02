package cc.dreamcode.menu.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.core.DreamMenuBuilder;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BukkitMenuBuilder extends DreamMenuBuilder<BukkitMenu, ItemStack> {

    public BukkitMenuBuilder(@NonNull String name, int rows, boolean disabledActions, boolean disposeWhenClose, Map<Integer, ItemStack> items) {
        super(name, rows, disabledActions, disposeWhenClose, items == null ? new HashMap<>() : items);
    }

    @Override
    public BukkitMenu buildEmpty() {
        return new BukkitMenu(
                this.getName(),
                this.getRows(),
                this.isDisabledActions(),
                this.isDisposeWhenClose(),
                0
        );
    }

    @Override
    public BukkitMenu buildEmpty(@NonNull Map<String, Object> replaceMap) {
        return new BukkitMenu(
                this.getName(),
                replaceMap,
                this.getRows(),
                this.isDisabledActions(),
                this.isDisposeWhenClose(),
                0
        );
    }

    @Override
    public BukkitMenu buildWithItems() {
        final BukkitMenu bukkitMenu = this.buildEmpty();

        this.getItems().forEach(bukkitMenu::setItem);

        return bukkitMenu;
    }

    @Override
    public BukkitMenu buildWithItems(@NonNull Map<String, Object> replaceMap) {
        final BukkitMenu bukkitMenu = this.buildEmpty(replaceMap);

        this.getItems().forEach(bukkitMenu::setItem);

        return bukkitMenu;
    }
}
