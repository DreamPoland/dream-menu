package cc.dreamcode.menu.base;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public interface DreamMenuPaginated<R, M, I, E, H> {

    M getMenuPlatform();

    List<Integer> getStorageItemSlots();

    R setStorageItemSlots(@NonNull List<Integer> storageItemSlots);

    default R setStorageItemSlots(int... slots) {
        final List<Integer> list = new ArrayList<>();
        for (int s : slots) {
            list.add(s);
        }
        return this.setStorageItemSlots(list);
    }

    default R setStorageItemSlots(int fromSlot, int toSlot) {
        return this.setStorageItemSlots(IntStream.rangeClosed(fromSlot, toSlot)
                .boxed()
                .collect(Collectors.toList()));
    }

    Optional<M> getMenuByPage(int page);

    Map<Integer, M> getMenuPages();

    int getSize();

    List<H> getViewers();

    int getPlayerPage(@NonNull H h);

    R setNextPageSlot(int slot, @NonNull Consumer<H> lastPageReach);

    R setNextPageSlot(int slot, @NonNull I i, @NonNull Consumer<H> lastPageReach);

    default R setNextPageSlot(int slot) {
        return this.setNextPageSlot(slot, h -> {});
    }

    default R setNextPageSlot(int slot, @NonNull I i) {
        return this.setNextPageSlot(slot, i, h -> {});
    }

    R setNextPageSlot(int x, int z, @NonNull Consumer<H> lastPageReach);

    R setNextPageSlot(int x, int z, @NonNull I i, @NonNull Consumer<H> lastPageReach);

    default R setNextPageSlot(int x, int z) {
        return this.setNextPageSlot(x, z, h -> {});
    }

    default R setNextPageSlot(int x, int z, @NonNull I i) {
        return this.setNextPageSlot(x, z, i, h -> {});
    }

    R setPreviousPageSlot(int slot, @NonNull Consumer<H> firstPageReach);

    R setPreviousPageSlot(int slot, @NonNull I i, @NonNull Consumer<H> firstPageReach);

    default R setPreviousPageSlot(int slot) {
        return this.setPreviousPageSlot(slot, h -> {});
    }

    default R setPreviousPageSlot(int slot, @NonNull I i) {
        return this.setPreviousPageSlot(slot, i, h -> {});
    }

    R setPreviousPageSlot(int x, int z, @NonNull Consumer<H> firstPageReach);

    R setPreviousPageSlot(int x, int z, @NonNull I i, @NonNull Consumer<H> firstPageReach);

    default R setPreviousPageSlot(int x, int z) {
        return this.setPreviousPageSlot(x, z, h -> {});
    }

    default R setPreviousPageSlot(int x, int z, @NonNull I i) {
        return this.setPreviousPageSlot(x, z, i, h -> {});
    }

    default boolean hasPage(int page) {
        return this.getMenuByPage(page).isPresent();
    }

    default boolean hasNextPage(@NonNull H h) {
        return this.getPlayerPage(h) + 1 < this.getSize();
    }

    default boolean hasPreviousPage(@NonNull H h) {
        return this.getPlayerPage(h) > 0;
    }

    default int getPagesCount() {
        return this.getSize();
    }

    R addStorageItem(@NonNull M m, int page, @NonNull I i, Consumer<E> event);

    R addStorageItem(@NonNull I i, Consumer<E> event);

    R addStorageItem(@NonNull I i);

    R addStorageItems(@NonNull List<I> list, Consumer<E> event);

    R addStorageItems(@NonNull List<I> list);

    R clearStorageItems();

    R open(int page, @NonNull H h);

    default R open(int page, @NonNull H... viewers) {
        Arrays.stream(viewers).forEach(h -> this.open(page, h));
        return (R) this;
    }

    default R open(int page, @NonNull Iterable<H> viewers) {
        viewers.forEach(h -> this.open(page, h));
        return (R) this;
    }

    R openPage(@NonNull H h);

    R openFirstPage(@NonNull H h);

    default R openFirstPage(@NonNull H... viewers) {
        Arrays.stream(viewers).forEach(this::openFirstPage);
        return (R) this;
    }

    default R openFirstPage(@NonNull Iterable<H> viewers) {
        viewers.forEach(this::openFirstPage);
        return (R) this;
    }

    R openLastPage(@NonNull H h);

    R close(@NonNull H h);

    R closeAll();

    R dispose();
}
