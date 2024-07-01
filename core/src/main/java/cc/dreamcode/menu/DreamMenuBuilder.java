package cc.dreamcode.menu;

import cc.dreamcode.menu.utilities.MenuUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

    public DreamMenuBuilder<B, T, I> fillTop(@NonNull I i) {
        Stream.of(
                MenuUtil.countSlot(1, 1),
                MenuUtil.countSlot(1, 2),
                MenuUtil.countSlot(1, 3),
                MenuUtil.countSlot(1, 4),
                MenuUtil.countSlot(1, 5),
                MenuUtil.countSlot(1, 6),
                MenuUtil.countSlot(1, 7),
                MenuUtil.countSlot(1, 8),
                MenuUtil.countSlot(1, 9)
        ).forEach(slot -> {
            if (!this.items.containsKey(slot)) {
                this.items.put(slot, i);
            }
        });

        return this;
    }

    public DreamMenuBuilder<B, T, I> fillBottom(@NonNull I i) {
        Stream.of(
                MenuUtil.countSlot(this.rows, 1),
                MenuUtil.countSlot(this.rows, 2),
                MenuUtil.countSlot(this.rows, 3),
                MenuUtil.countSlot(this.rows, 4),
                MenuUtil.countSlot(this.rows, 5),
                MenuUtil.countSlot(this.rows, 6),
                MenuUtil.countSlot(this.rows, 7),
                MenuUtil.countSlot(this.rows, 8),
                MenuUtil.countSlot(this.rows, 9)
        ).forEach(slot -> {
            if (!this.items.containsKey(slot)) {
                this.items.put(slot, i);
            }
        });

        return this;
    }

    public DreamMenuBuilder<B, T, I> fillTopAndBottom(@NonNull I i) {
        this.fillTop(i);
        this.fillBottom(i);

        return this;
    }

    public DreamMenuBuilder<B, T, I> fillMargin(@NonNull I i) {
        this.fillTop(i);
        this.fillBottom(i);

        for (int row = 2; row < this.rows; row++) {
            Stream.of(
                    MenuUtil.countSlot(row, 1),
                    MenuUtil.countSlot(row, 9)
            ).forEach(slot -> {
                if (!this.items.containsKey(slot)) {
                    this.items.put(slot, i);
                }
            });
        }

        return this;
    }

    public abstract B buildEmpty();
    public abstract B buildEmpty(@NonNull Map<String, Object> replaceMap);
    public abstract B buildWithItems();
    public abstract B buildWithItems(@NonNull Map<String, Object> replaceMap);
}
