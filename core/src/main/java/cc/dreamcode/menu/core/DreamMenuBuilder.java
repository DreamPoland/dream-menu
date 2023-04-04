package cc.dreamcode.menu.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class DreamMenuBuilder<B, I> {

    private final String name;
    private final int rows;
    private final boolean disabledActions;
    private final Map<Integer, I> items;

    public DreamMenuBuilder<B, I> fillInventoryWith(@NonNull I i) {
        for (int slot = 0; slot < this.rows * 9; slot++) {
            if (!this.items.containsKey(slot)) {
                this.items.put(slot, i);
            }
        }

        return this;
    }

    public abstract B buildEmpty();
    public abstract B buildEmpty(@NonNull Map<String, Object> replaceMap);
    public abstract B buildWithItems();
    public abstract B buildWithItems(@NonNull Map<String, Object> replaceMap);

}
