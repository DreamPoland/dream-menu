package cc.dreamcode.menu.core;

import lombok.NonNull;

/**
 * Create an instance of menu.
 */
public interface DreamMenuProvider<D> {

    D createDefault(@NonNull String title, int rows, boolean cancelInventoryClick);
}
