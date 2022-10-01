package cc.dreamcode.menu.core;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * Create an instance of menu.
 */
public interface DreamMenuProvider<D, P> {

    D createDefault(@NonNull String title, int rows, boolean cancelInventoryClick);

    D createDefault(@NonNull String title, int rows, boolean cancelInventoryClick, @NonNull Consumer<D> consumer);

    P createPaginated(@NonNull D d);

    P createPaginated(@NonNull D d, @NonNull Consumer<D> consumer);
}
