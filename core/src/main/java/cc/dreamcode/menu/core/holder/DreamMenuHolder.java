package cc.dreamcode.menu.core.holder;

import lombok.NonNull;

/**
 * Interface of menu holder.
 * @param <P> HumanEntity/Player
 * @param <E> InventoryClickEvent
 */
public interface DreamMenuHolder<P> {

    void open(@NonNull P p);
}
