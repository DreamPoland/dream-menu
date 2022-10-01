package me.devtest.menuexample;

import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.bukkit.base.BukkitMenuPaginated;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

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

        // Test it with stick and right/left air click
        if (e.getItem() != null &&
                e.getItem().getType().equals(Material.STICK)) {
            if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                this.buildAndOpenMenu(player);
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                this.buildPaginatedAndOpenMenu(player);
            }
        }
    }

    public void buildAndOpenMenu(@NonNull Player player) {
        // Example basis for new default menu
        final BukkitMenu bukkitMenu = this.bukkitMenuProvider.createDefault(
                ChatColor.translateAlternateColorCodes('&', "&7&lTest default menu."), // title
                3, // rows
                true // cancel move item when click
        );

        // Example item for defaultBukkitMenu above.
        bukkitMenu.addItem(this.getExampleItem(), e -> {
            final HumanEntity humanEntity = e.getWhoClicked();
            humanEntity.sendMessage("hello from item event");
        });

        // Open inventory
        bukkitMenu.open(player);
    }

    public void buildPaginatedAndOpenMenu(@NonNull Player player) {
        // Example basis for new paginated menu
        final BukkitMenuPaginated bukkitMenuPaginated = this.bukkitMenuProvider.createPaginated(
                // Default background without items etc.
                this.bukkitMenuProvider.createDefault(
                        ChatColor.translateAlternateColorCodes('&', "&7&lTest default menu."), // title
                        3, // rows
                        false // cancel move item when click
                )
        );

        // Or use consumer to add background items
        final BukkitMenuPaginated bukkitMenuPaginatedWithBackground = this.bukkitMenuProvider.createPaginated(
                this.bukkitMenuProvider.createDefault(
                        ChatColor.translateAlternateColorCodes('&', "&7&lTest default menu. Page &c&l%PAGE%"), // title
                        3, // rows
                        false // cancel move item when click
                ), bukkitMenu -> bukkitMenu.setItem(10, this.getExampleItem(), e -> {
                    e.setCancelled(true); // cancel background item move

                    final HumanEntity humanEntity = e.getWhoClicked();
                    humanEntity.sendMessage("hello from background item event");
                })
        );

        // Set next/previous page switch button
        bukkitMenuPaginatedWithBackground.setPreviousPageSlot(18, this.getPreviousPageItem(), humanEntity ->
                humanEntity.sendMessage("First page reach!"));

        bukkitMenuPaginatedWithBackground.setNextPageSlot(26, this.getNextPageItem(), humanEntity ->
                humanEntity.sendMessage("Last page reach!"));

        // Simple add item to paginated menu storage
        bukkitMenuPaginatedWithBackground.addStorageItem(this.getStorageItem());

        // Or with event
        bukkitMenuPaginatedWithBackground.addStorageItem(this.getStorageItem(), e -> {
            final HumanEntity humanEntity = e.getWhoClicked();
            humanEntity.sendMessage("hello from storage item event");
        });

        final List<ItemStack> items = new ArrayList<>();
        IntStream.range(0, 30).forEach(i -> items.add(this.getStorageItem()));

        // Or add list of items with event :o
        bukkitMenuPaginatedWithBackground.addStorageItems(items, e -> {
            final HumanEntity humanEntity = e.getWhoClicked();
            humanEntity.sendMessage("take it!");
        });

        // Open paginated inventory
        bukkitMenuPaginatedWithBackground.openFirstPage(player);
    }

    private ItemStack getExampleItem() {
        final ItemStack item = new ItemStack(Material.BOOK);
        final ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cTest item"));
        itemMeta.setLore(Collections.singletonList(
                ChatColor.translateAlternateColorCodes('&', "&7Test lore")
        ));

        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getStorageItem() {
        final ItemStack item = new ItemStack(Material.DIRT, 64);
        final ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cTest storage"));
        itemMeta.setLore(Collections.singletonList(
                ChatColor.translateAlternateColorCodes('&', "&7Test lore")
        ));

        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getNextPageItem() {
        final ItemStack item = new ItemStack(Material.DIAMOND);
        final ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aNext page"));
        itemMeta.setLore(Collections.singletonList(
                ChatColor.translateAlternateColorCodes('&', "&7Test lore")
        ));

        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getPreviousPageItem() {
        final ItemStack item = new ItemStack(Material.REDSTONE);
        final ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cPrevious page"));
        itemMeta.setLore(Collections.singletonList(
                ChatColor.translateAlternateColorCodes('&', "&7Test lore")
        ));

        item.setItemMeta(itemMeta);
        return item;
    }

}