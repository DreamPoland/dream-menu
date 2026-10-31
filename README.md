# Dream-Menu

[![Build](https://github.com/DreamPoland/dream-menu/actions/workflows/gradle.yml/badge.svg)](https://github.com/DreamPoland/dream-menu/actions/workflows/gradle.yml)
[![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](https://repo.dreamcode.cc/releases)
[![Java](https://img.shields.io/badge/java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENCE)

Modern, secure, and lightweight Inventory & GUI API for Bukkit/Spigot/Paper Minecraft servers with built-in Okaeri Configs serialization.

---

## ✨ Features in 2.0

* 🛡️ **Anti-Dupe & Exploit Prevention** – Automatic protection against item stealing, drag exploits (`InventoryDragEvent`), double action triggers, and shift-click manipulation.
* 📄 **Advanced Paginated Menus** – High-performance multi-page inventory support (`BukkitMenuPaginated`) with dynamic page resolution, custom storage slots, and zero memory leaks.
* 📦 **Okaeri Configs Serialization** – Full serialization and deserialization of menu layouts via Okaeri Serdes (`BukkitMenuBuilder`).
* 🎨 **Fluent Layout & Filling Helpers** – Methods for filling borders (`fillMargin`), rows (`fillRow`), columns (`fillColumn`), and backgrounds (`fillBackground`).
* ⚡ **Performance & Safety** – Bounded slot validation, automatic viewer closure on disposal, and lightweight design.

---

## 📦 Installation

Add the DreamCode repository to your build configuration:

### Gradle (Kotlin DSL)
```kotlin
repositories {
    maven("https://repo.dreamcode.cc/releases")
    maven("https://storehouse.okaeri.eu/repository/maven-public")
}

dependencies {
    // Core Bukkit Inventory API
    implementation("cc.dreamcode.menu:bukkit:2.0.0")

    // Optional: Okaeri Configs serialization
    implementation("cc.dreamcode.menu:bukkit-serializer:2.0.0")
}
```

### Gradle (Groovy DSL)
```groovy
repositories {
    maven { url "https://repo.dreamcode.cc/releases" }
    maven { url "https://storehouse.okaeri.eu/repository/maven-public" }
}

dependencies {
    // Core Bukkit Inventory API
    implementation "cc.dreamcode.menu:bukkit:2.0.0"

    // Optional: Okaeri Configs serialization
    implementation "cc.dreamcode.menu:bukkit-serializer:2.0.0"
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>dream-repository-releases</id>
        <name>DreamCode | Repository</name>
        <url>https://repo.dreamcode.cc/releases</url>
    </repository>
    <repository>
        <id>okaeri-repo</id>
        <name>Okaeri Repository</name>
        <url>https://storehouse.okaeri.eu/repository/maven-public</url>
    </repository>
</repositories>

<dependencies>
    <!-- Core Bukkit Inventory API -->
    <dependency>
        <groupId>cc.dreamcode.menu</groupId>
        <artifactId>bukkit</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- Optional: Okaeri Configs serialization -->
    <dependency>
        <groupId>cc.dreamcode.menu</groupId>
        <artifactId>bukkit-serializer</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

---

## 🚀 Quick Start

### 1. Initialization
Register `BukkitMenuProvider` in your plugin's `onEnable`:

```java
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitMenuProvider.create(this);
    }
}
```

---

### 2. Basic Interactive Menu
Create menus using `BukkitMenu` with custom titles, rows, and click actions:

```java
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public void openShopMenu(Player player) {
    BukkitMenu menu = new BukkitMenu("&8Shop Menu", 3, 0);

    // Fill margin with black stained glass
    ItemStack glass = ItemBuilder.of(Material.STAINED_GLASS_PANE, (short) 15)
            .setName(" ")
            .toItemStack();
    menu.fillMargin(glass);

    // Set an interactive item on slot 13 (row 2, column 5)
    ItemStack diamond = ItemBuilder.of(Material.DIAMOND)
            .setName("&b&lBuy Diamond")
            .setLore("&7Click to purchase!")
            .toItemStack();

    menu.setItem(13, diamond, event -> {
        player.sendMessage("You clicked the diamond!");
    });

    menu.open(player);
}
```

---

### 3. Paginated Menu (`BukkitMenuPaginated`)
Easily display large or dynamic lists of items across multiple pages:

```java
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.bukkit.base.BukkitMenuPaginated;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public void openWarpList(Player player, List<String> warps) {
    // 1. Create base menu platform (title supports {page} placeholder)
    BukkitMenu baseMenu = new BukkitMenu("&8Warps (Page {page})", 4, 0);

    // 2. Wrap into paginated menu
    BukkitMenuPaginated paginated = baseMenu.toPaginated();

    // 3. Define storage slots for items (e.g. slots 10 to 16 and 19 to 25)
    paginated.setStorageItemSlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25);

    // 4. Set Previous / Next page navigation buttons
    ItemStack prevItem = ItemBuilder.of(Material.ARROW).setName("&aPrevious Page").toItemStack();
    ItemStack nextItem = ItemBuilder.of(Material.ARROW).setName("&aNext Page").toItemStack();

    paginated.setPreviousPageSlot(27, prevItem, p -> p.sendMessage("&cYou are on the first page!"));
    paginated.setNextPageSlot(35, nextItem, p -> p.sendMessage("&cYou are on the last page!"));

    // 5. Add storage items with click events
    for (String warpName : warps) {
        ItemStack item = ItemBuilder.of(Material.ENDER_PEARL)
                .setName("&aWarp: " + warpName)
                .toItemStack();

        paginated.addStorageItem(item, event -> {
            player.sendMessage("Teleporting to " + warpName + "...");
            player.closeInventory();
        });
    }

    // 6. Open the first page
    paginated.openFirstPage(player);
}
```

---

### 4. Okaeri Configs Serialization (`BukkitMenuBuilder`)
Define configurable menus in Okaeri Config files:

```java
import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PluginConfig extends OkaeriConfig {

    public BukkitMenuBuilder profileMenu = new BukkitMenuBuilder(
            "&8Profile Menu",
            3,
            new ItemBuilder(Material.BOOK).setName("&eInformation").toItemStack()
    );
}
```

Register the serdes pack in your config initialization:

```java
import cc.dreamcode.menu.serializer.SerdesMenu;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;

ConfigManager.create(PluginConfig.class, (it) -> {
    it.withConfigurer(new YamlBukkitConfigurer(), new SerdesMenu());
    it.withBindFile(new File(getDataFolder(), "config.yml"));
    it.saveDefaults();
    it.load(true);
});
```

---

## 🧩 Modules

| Module | Artifact | Description |
| :--- | :--- | :--- |
| **`core`** | `cc.dreamcode.menu:core` | Platform-independent interfaces, builders, and slot utilities. |
| **`bukkit`** | `cc.dreamcode.menu:bukkit` | Spigot/Paper implementation of menus, listeners, and holders. |
| **`bukkit-serializer`** | `cc.dreamcode.menu:bukkit-serializer` | Okaeri Configs serdes pack for `BukkitMenuBuilder`. |

---

## 📄 License

This project is licensed under the [MIT License](LICENCE).
