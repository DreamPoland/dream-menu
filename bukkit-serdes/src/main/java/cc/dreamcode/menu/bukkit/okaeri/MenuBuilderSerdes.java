package cc.dreamcode.menu.bukkit.okaeri;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
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
public class MenuBuilderSerdes implements ObjectSerializer<BukkitMenuBuilder> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super BukkitMenuBuilder> type) {
        return BukkitMenuBuilder.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(@NonNull BukkitMenuBuilder object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("name", object.getName());
        data.add("rows", object.getRows());
        if (!object.isDisabledActions()) {
            data.add("cancel-inventory-click", object.isDisabledActions());
        }

        if (!object.isDisposeWhenClose()) {
            data.add("dispose-when-close", object.isDisposeWhenClose());
        }

        data.addAsMap("items", object.getItems(), Integer.class, ItemStack.class);
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public BukkitMenuBuilder deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new BukkitMenuBuilder(
                data.get("name", String.class),
                data.get("rows", Integer.class),
                data.containsKey("cancel-inventory-click") ? data.get("cancel-inventory-click", Boolean.class) : true,
                data.containsKey("dispose-when-close") ? data.get("dispose-when-close", Boolean.class) : true,
                data.getAsMap("items", Integer.class, ItemStack.class)
        );
    }
}
