package cc.dreamcode.menu.core;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * Interface of menu base.
 * @param <I> Itemstack
 * @param <E> InventoryClickEvent
 */
public interface DreamMenu<I, E, H> {

    void addItem(@NonNull I itemstack);

    void addItem(@NonNull I itemstack, @NonNull Consumer<E> event);

    void setItem(int slot, @NonNull I itemstack);

    void setItem(int slot, @NonNull I itemstack, @NonNull Consumer<E> event);

    H build();
}
