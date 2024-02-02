package cc.dreamcode.menu.bukkit.okaeri;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

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
        if (!object.getInventoryType().equals(InventoryType.CHEST)) {
            data.add("type", object.getInventoryType());
        }

        data.add("name", object.getName());

        if (object.getInventoryType().equals(InventoryType.CHEST)) {
            data.add("rows", object.getRows());
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
        if (data.containsKey("type")) {
            return new BukkitMenuBuilder(
                    data.get("type", InventoryType.class),
                    data.get("name", String.class),
                    data.getAsMap("items", Integer.class, ItemStack.class)
            );
        }

        return new BukkitMenuBuilder(
                data.get("name", String.class),
                data.get("rows", Integer.class),
                data.getAsMap("items", Integer.class, ItemStack.class)
        );
    }
}
