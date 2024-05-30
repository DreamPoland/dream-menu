package cc.dreamcode.menu.base;

import lombok.NonNull;

import java.util.List;
import java.util.function.Consumer;

public interface DreamMenu<I, E, H, P> {

    int addItem(@NonNull I i);

    int addItem(@NonNull I i, @NonNull List<Integer> applySlots);

    int addItem(@NonNull I i, @NonNull Consumer<E> event);

    int addItem(@NonNull I i, @NonNull List<Integer> applySlots, @NonNull Consumer<E> event);

    void setItem(int slot, @NonNull I i);

    void setItem(int slot, @NonNull I i, @NonNull Consumer<E> event);

    void setItem(int x, int z, @NonNull I i);

    void setItem(int x, int z, @NonNull I i, @NonNull Consumer<E> event);

    void setItem(int[] slots, @NonNull I i);

    void setItem(int[] slots, @NonNull I i, @NonNull Consumer<E> event);

    void fillInventoryWith(@NonNull I i);

    void fillInventoryWith(@NonNull I i, @NonNull Consumer<E> event);

    H getHolder();

    void open(@NonNull P p);

    void dispose();
}
