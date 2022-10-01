package cc.dreamcode.menu.serdes.bukkit.okaeri;

import cc.dreamcode.menu.serdes.bukkit.MenuBuilder;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

/**
 * Required Yaml-Bukkit serdes pack from okaeri-config.
 * Add this class to your config serdes pack.
 */
public class MenuBuilderSerdes implements ObjectSerializer<MenuBuilder> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super MenuBuilder> type) {
        return MenuBuilder.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(@NonNull MenuBuilder object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("name", object.getName());
        data.add("rows", object.getRows());
        data.add("cancel-inventory-click", object.isCancelInventoryClick());
        data.addAsMap("items", object.getItems(), Integer.class, ItemStack.class);
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public MenuBuilder deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new MenuBuilder(
                data.get("name", String.class),
                data.get("rows", Integer.class),
                data.get("cancel-inventory-click", Boolean.class),
                data.getAsMap("items", Integer.class, ItemStack.class)
        );
    }
}
