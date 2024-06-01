package cc.dreamcode.menu.base;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface DreamMenuPaginated<R, M, I, E, H> {

    M getMenuPlatform();

    List<Integer> getStorageItemSlots();

    Optional<M> getMenuByPage(int page);
    Map<Integer, M> getMenuPages();

    int getSize();

    List<H> getViewers();
    int getPlayerPage(@NonNull H h);

    R setNextPageSlot(int slot, @NonNull Consumer<H> lastPageReach);
    R setNextPageSlot(int slot, @NonNull I i, @NonNull Consumer<H> lastPageReach);

    R setNextPageSlot(int x, int z, @NonNull Consumer<H> lastPageReach);
    R setNextPageSlot(int x, int z, @NonNull I i, @NonNull Consumer<H> lastPageReach);

    R setPreviousPageSlot(int slot, @NonNull Consumer<H> firstPageReach);
    R setPreviousPageSlot(int slot, @NonNull I i, @NonNull Consumer<H> firstPageReach);

    R setPreviousPageSlot(int x, int z, @NonNull Consumer<H> firstPageReach);
    R setPreviousPageSlot(int x, int z, @NonNull I i, @NonNull Consumer<H> firstPageReach);

    R addStorageItem(@NonNull M m, int page, @NonNull I i, Consumer<E> event);
    R addStorageItem(@NonNull I i, Consumer<E> event);
    R addStorageItem(@NonNull I i);

    R addStorageItems(@NonNull List<I> list, Consumer<E> event);
    R addStorageItems(@NonNull List<I> list);

    R open(int page, @NonNull H h);
    R openPage(@NonNull H h);
    R openFirstPage(@NonNull H h);
    R openLastPage(@NonNull H h);

    R dispose();
}
