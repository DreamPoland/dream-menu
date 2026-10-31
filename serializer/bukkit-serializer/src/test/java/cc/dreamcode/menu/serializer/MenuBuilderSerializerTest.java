package cc.dreamcode.menu.serializer;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import eu.okaeri.configs.serdes.SerdesRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuBuilderSerializerTest {

    @Test
    void shouldSupportBukkitMenuBuilder() {
        MenuBuilderSerializer serializer = new MenuBuilderSerializer();
        assertTrue(serializer.supports(BukkitMenuBuilder.class));
        assertFalse(serializer.supports(String.class));
    }

    @Test
    void shouldRegisterInSerdesRegistry() {
        SerdesRegistry registry = new SerdesRegistry();
        SerdesMenu serdesMenu = new SerdesMenu();
        serdesMenu.register(registry);

        assertNotNull(registry.getSerializer(BukkitMenuBuilder.class));
    }
}
