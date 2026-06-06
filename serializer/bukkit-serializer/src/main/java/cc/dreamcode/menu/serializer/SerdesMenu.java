package cc.dreamcode.menu.serializer;

import eu.okaeri.configs.serdes.OkaeriSerdes;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;

public class SerdesMenu implements OkaeriSerdes {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new MenuBuilderSerializer());
    }
}
