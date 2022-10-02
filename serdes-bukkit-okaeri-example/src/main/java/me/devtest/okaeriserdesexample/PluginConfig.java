package me.devtest.okaeriserdesexample;

import cc.dreamcode.menu.serdes.bukkit.BukkitMenuBuilder;
import com.google.common.collect.ImmutableMap;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Header("Test dream-menu config")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class PluginConfig extends OkaeriConfig {

    @Comment("Ustaw jak chcesz swoje menu: (background)")
    public BukkitMenuBuilder bukkitMenuBuilder = new BukkitMenuBuilder("&7Test config-menu", 3, true, new ImmutableMap.Builder<Integer, ItemStack>()
            .put(0, new ItemStack(Material.DIAMOND)) // easy with item-builder type class
            .put(1, new ItemStack(Material.DIRT))
            .put(5, new ItemStack(Material.EMERALD, 64))
            .build());

    @Comment("Pod ktorym slotem ma byc wysylany komunikat?")
    public int alertSlot = 5;

    @Comment("Jaka wiadomosc ma byc wyslana?")
    public String alertNotice = "&aEmerald.";

    // and other stuff
}
