package cc.dreamcode.menu.base;

import lombok.NonNull;

import java.util.List;
import java.util.function.Consumer;

public interface DreamMenu<R, I, E, H, P> {

    R addItem(@NonNull I i);

    R addItem(@NonNull I i, @NonNull List<Integer> applySlots);

    R addItem(@NonNull I i, @NonNull Consumer<E> event);

    R addItem(@NonNull I i, @NonNull List<Integer> applySlots, @NonNull Consumer<E> event);

    R setItem(int slot, @NonNull I i);

    R setItem(int slot, @NonNull I i, @NonNull Consumer<E> event);

    R setItem(int x, int z, @NonNull I i);

    R setItem(int x, int z, @NonNull I i, @NonNull Consumer<E> event);

    R setItem(int[] slots, @NonNull I i);

    R setItem(int[] slots, @NonNull I i, @NonNull Consumer<E> event);

    R fillInventoryWith(@NonNull I i);

    R fillInventoryWith(@NonNull I i, @NonNull Consumer<E> event);

    H getHolder();

    R open(@NonNull P p);

    R dispose();
}
