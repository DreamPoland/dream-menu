package cc.dreamcode.menu.core;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * Interface of menu base.
 * @param <P> HumanEntity/Player
 * @param <I> Item
 * @param <E> InventoryClickEvent
 */
public interface DreamMenu<I, E, H> {

    void addItem(@NonNull I i);

    void addItem(@NonNull I i, @NonNull Consumer<E> e);

    void setItem(int slot, @NonNull I i);

    void setItem(int slot, @NonNull I i, @NonNull Consumer<E> e);

    H build();
}
