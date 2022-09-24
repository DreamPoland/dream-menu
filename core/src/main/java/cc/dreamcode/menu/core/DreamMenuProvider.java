package cc.dreamcode.menu.core;

import lombok.NonNull;

/**
 * Create an instance of menu.
 * @param <I> Item
 * @param <E> Event
 */
public interface DreamMenuProvider<D> {

    D createDefault(@NonNull String title, int rows, boolean cancelInventoryClick);
}
