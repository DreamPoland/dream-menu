package cc.dreamcode.menu.core;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * Interface of menu base.
 *
 * @param <I> ItemStack
 * @param <E> InventoryClickEvent
 */
public interface DreamMenu<I, E, H> {

    void addItem(@NonNull I itemStack);

    void addItem(@NonNull I itemStack, @NonNull Consumer<E> event);

    void setItem(int slot, @NonNull I itemStack);

    void setItem(int slot, @NonNull I itemStack, @NonNull Consumer<E> event);

    H build();
}
