package me.devtest.menuexample;

import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.base.DefaultBukkitMenu;
import cc.dreamcode.menu.bukkit.holder.DefaultMenuHolder;
import cc.dreamcode.menu.bukkit.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "Dream-Menu-Example", version = "1.0-SNAPSHOT")
@Author("Ravis96")
@Description("PluginExample for dream-menu example.")
@Website("DreamCode - https://discord.gg/dreamcode")
@ApiVersion(ApiVersion.Target.v1_13)
public class PluginMain extends JavaPlugin implements Listener {

    private BukkitMenuProvider bukkitMenuProvider;

    @Override
    public void onEnable() {
        // Create an instance of menus.
        this.bukkitMenuProvider = BukkitMenuProvider.create(this);

        // Register event below.
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();

        if (e.getItem() != null &&
                e.getItem().getType().equals(Material.STICK) &&
                e.getAction().equals(Action.RIGHT_CLICK_AIR)) {

            this.buildAndOpenMenu(player);
        }
    }

    public void buildAndOpenMenu(@NonNull Player player) {
        // Example basis for new default menu
        final DefaultBukkitMenu defaultBukkitMenu = bukkitMenuProvider.createDefault(
                ChatColor.translateAlternateColorCodes('&', "&7&lTest default menu."),
                3,
                true
        );

        // Example item (easy with ItemBuilder)
        final ItemStack item = new ItemBuilder(Material.BOOK)
                .setName(ChatColor.translateAlternateColorCodes('&', "&cTest item"))
                .setLore(ChatColor.translateAlternateColorCodes('&', "&7Test lore"))
                .toItemStack();

        // Example item for defaultBukkitMenu above.
        defaultBukkitMenu.addItem(item, e -> {
            final HumanEntity humanEntity = e.getWhoClicked();
            humanEntity.sendMessage("hello from item event");
        });

        // Build MenuHolder & Open
        final DefaultMenuHolder defaultMenuHolder = defaultBukkitMenu.build();
        defaultMenuHolder.open(player);
    }
}
