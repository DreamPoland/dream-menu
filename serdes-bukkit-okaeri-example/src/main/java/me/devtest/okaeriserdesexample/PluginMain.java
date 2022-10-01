package me.devtest.okaeriserdesexample;

import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.serdes.bukkit.okaeri.MenuSerdes;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import jdk.jfr.Description;
import me.devtest.okaeriserdesexample.menu.TestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.File;

@Plugin(name = "Dream-Menu-Config-Example", version = "1.0-SNAPSHOT")
@Author("Ravis96")
@Description("PluginExample for dream-menu-config example.")
@Website("DreamCode - https://discord.gg/dreamcode")
@ApiVersion(ApiVersion.Target.v1_13)
public class PluginMain extends JavaPlugin implements Listener {

    private BukkitMenuProvider bukkitMenuProvider;
    private PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        // Create an instance of menus. (required)
        this.bukkitMenuProvider = BukkitMenuProvider.create(this);

        this.pluginConfig = ConfigManager.create(PluginConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit(), new MenuSerdes());
            it.withBindFile(new File(this.getDataFolder(), "config.yml"));
            it.saveDefaults();
            it.load(true);
        });

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();

        // Test it with stick and left air click
        if (e.getItem() != null &&
                e.getItem().getType().equals(Material.STICK)) {
            if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                final BukkitMenu bukkitMenu = new TestMenu(this.pluginConfig).apply(player); // build with custom setup
                bukkitMenu.open(player); // open player inventory
            }
        }
    }
}
