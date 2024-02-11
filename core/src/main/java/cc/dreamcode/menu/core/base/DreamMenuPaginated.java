package cc.dreamcode.menu.core.base;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface DreamMenuPaginated<M, I, E, H> {

    M getMenuPlatform();

    List<Integer> getStorageItemSlots();

    Optional<M> getMenuByPage(int page);
    Map<Integer, M> getMenuPages();

    int getSize();

    List<H> getViewers();
    int getPlayerPage(@NonNull H h);

    void setNextPageSlot(int slot, @NonNull Consumer<H> lastPageReach);
    void setNextPageSlot(int slot, @NonNull I i, @NonNull Consumer<H> lastPageReach);

    void setPreviousPageSlot(int slot, @NonNull Consumer<H> firstPageReach);
    void setPreviousPageSlot(int slot, @NonNull I i, @NonNull Consumer<H> firstPageReach);

    void addStorageItem(@NonNull M m, int page, @NonNull I i, Consumer<E> event);
    void addStorageItem(@NonNull I i, Consumer<E> event);
    void addStorageItem(@NonNull I i);

    void addStorageItems(@NonNull List<I> list, Consumer<E> event);
    void addStorageItems(@NonNull List<I> list);

    void open(int page, @NonNull H h);
    void openPage(@NonNull H h);
    void openFirstPage(@NonNull H h);
    void openLastPage(@NonNull H h);

    void dispose();
}
