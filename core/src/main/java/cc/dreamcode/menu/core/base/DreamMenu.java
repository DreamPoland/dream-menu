package cc.dreamcode.menu.core.base;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * Interface of menu base.
 *
 * @param <I> ItemStack
 * @param <E> InventoryClickEvent
 * @param <H> MenuHolder
 * @param <P> HumanEntity/Player
 */
public interface DreamMenu<I, E, H, P> {

    int addItem(@NonNull I i);

    int addItem(@NonNull I i, @NonNull Consumer<E> event);

    void setItem(int slot, @NonNull I i);

    void setItem(int slot, @NonNull I i, @NonNull Consumer<E> event);

    H getHolder();

    void open(@NonNull P p);
}
