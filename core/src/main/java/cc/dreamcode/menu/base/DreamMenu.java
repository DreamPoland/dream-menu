package cc.dreamcode.menu.base;

import cc.dreamcode.menu.utilities.MenuUtil;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public interface DreamMenu<R, T, I, E, H, P> {

    int getPage();

    R addItem(@NonNull I i);

    R addItem(@NonNull I i, @NonNull List<Integer> applySlots);

    R addItem(@NonNull I i, @NonNull Consumer<E> event);

    R addItem(@NonNull I i, @NonNull List<Integer> applySlots, @NonNull Consumer<E> event);

    R setItem(int slot, @NonNull I i);

    R setItem(int slot, @NonNull I i, @NonNull Consumer<E> event);

    R setItem(int x, int z, @NonNull I i);

    R setItem(int x, int z, @NonNull I i, @NonNull Consumer<E> event);

    R setItem(int[] slots, @NonNull I i);

    R setItem(int[] slots, @NonNull I i, @NonNull Consumer<E> event);

    default R setItem(@NonNull Iterable<Integer> slots, @NonNull I i) {
        slots.forEach(slot -> this.setItem(slot, i));
        return (R) this;
    }

    default R setItem(@NonNull Iterable<Integer> slots, @NonNull I i, @NonNull Consumer<E> event) {
        slots.forEach(slot -> this.setItem(slot, i, event));
        return (R) this;
    }

    R removeItem(int slot);

    default R removeItem(int x, int z) {
        return this.removeItem(MenuUtil.countSlot(x, z));
    }

    default R removeItem(int[] slots) {
        for (int slot : slots) {
            this.removeItem(slot);
        }
        return (R) this;
    }

    default R removeItem(@NonNull Iterable<Integer> slots) {
        slots.forEach(this::removeItem);
        return (R) this;
    }

    Optional<I> getItem(int slot);

    default Optional<I> getItem(int x, int z) {
        return this.getItem(MenuUtil.countSlot(x, z));
    }

    default boolean hasItem(int slot) {
        return this.getItem(slot).isPresent();
    }

    default boolean isSlotEmpty(int slot) {
        return !this.hasItem(slot);
    }

    R fillInventoryWith(@NonNull I i);

    R fillInventoryWith(@NonNull I i, @NonNull Consumer<E> event);

    default R fillRow(int row, @NonNull I i) {
        return this.setItem(MenuUtil.countRowSlots(row), i);
    }

    default R fillRow(int row, @NonNull I i, @NonNull Consumer<E> event) {
        return this.setItem(MenuUtil.countRowSlots(row), i, event);
    }

    default R fillColumn(int column, @NonNull I i) {
        for (int r = 1; r <= 6; r++) {
            this.setItem(r, column, i);
        }
        return (R) this;
    }

    default R fillColumn(int column, @NonNull I i, @NonNull Consumer<E> event) {
        for (int r = 1; r <= 6; r++) {
            this.setItem(r, column, i, event);
        }
        return (R) this;
    }

    default R fillTop(@NonNull I i) {
        return this.fillRow(1, i);
    }

    default R fillTop(@NonNull I i, @NonNull Consumer<E> event) {
        return this.fillRow(1, i, event);
    }

    R fillBottom(@NonNull I i);

    R fillBottom(@NonNull I i, @NonNull Consumer<E> event);

    default R fillTopAndBottom(@NonNull I i) {
        this.fillTop(i);
        this.fillBottom(i);
        return (R) this;
    }

    default R fillTopAndBottom(@NonNull I i, @NonNull Consumer<E> event) {
        this.fillTop(i, event);
        this.fillBottom(i, event);
        return (R) this;
    }

    R fillMargin(@NonNull I i);

    R fillMargin(@NonNull I i, @NonNull Consumer<E> event);

    R setActionOnSlot(int slot, @NonNull Consumer<E> event);

    default R setActionOnSlot(int x, int z, @NonNull Consumer<E> event) {
        return this.setActionOnSlot(MenuUtil.countSlot(x, z), event);
    }

    R removeActionOnSlot(int slot);

    default R removeActionOnSlot(int x, int z) {
        return this.removeActionOnSlot(MenuUtil.countSlot(x, z));
    }

    List<P> getViewers();

    H getHolder();

    R open(@NonNull P p);

    default R open(@NonNull P... players) {
        Arrays.stream(players).forEach(this::open);
        return (R) this;
    }

    default R open(@NonNull Iterable<P> players) {
        players.forEach(this::open);
        return (R) this;
    }

    R close(@NonNull P p);

    R closeAll();

    T toPaginated();

    R dispose();

    default boolean isDisposed() {
        return false;
    }
}
