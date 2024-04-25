package cc.dreamcode.menu.core;

import cc.dreamcode.menu.core.utilities.MenuUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class DreamMenuBuilder<B, T, I> {

    private final T inventoryType;
    private final String name;
    private final int rows;
    private Map<Integer, I> items = new HashMap<>();

    public DreamMenuBuilder<B, T, I> setItem(int x, int z, @NonNull I i) {
        this.items.put(MenuUtil.countSlot(x, z), i);
        return this;
    }

    public DreamMenuBuilder<B, T, I> setItem(int slot, @NonNull I i) {
        this.items.put(slot, i);
        return this;
    }

    public DreamMenuBuilder<B, T, I> fillBackground(@NonNull I i) {
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
