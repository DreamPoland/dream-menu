package cc.dreamcode.menu.bukkit.base;

import cc.dreamcode.menu.base.DreamMenuPaginated;
import cc.dreamcode.menu.utilities.MenuUtil;
import cc.dreamcode.utilities.option.Option;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class BukkitMenuPaginated implements DreamMenuPaginated<BukkitMenuPaginated, BukkitMenu, ItemStack, InventoryClickEvent, HumanEntity> {

    private final BukkitMenu menuPlatform;
    private final List<Integer> storageItemSlots;
    private final Map<Integer, BukkitMenu> bukkitMenuMap = new HashMap<>();
    private final Map<UUID, Integer> cacheSlotPlayerViewing = new HashMap<>();

    @Getter @Setter private BiConsumer<HumanEntity, Integer> nextPagePreEvent;
    @Getter @Setter private BiConsumer<HumanEntity, Integer> nextPageEvent;
    @Getter @Setter private BiConsumer<HumanEntity, Integer> nextPagePostEvent;
    @Getter @Setter private BiConsumer<HumanEntity, Integer> previousPagePreEvent;
    @Getter @Setter private BiConsumer<HumanEntity, Integer> previousPageEvent;
    @Getter @Setter private BiConsumer<HumanEntity, Integer> previousPagePostEvent;

    public BukkitMenuPaginated(@NonNull BukkitMenu menuPlatform) {
        this.menuPlatform = menuPlatform;
        this.storageItemSlots = IntStream.rangeClosed(0, menuPlatform.getSize())
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public BukkitMenu getMenuPlatform() {
        return this.menuPlatform;
    }

    @Override
    public List<Integer> getStorageItemSlots() {
        return this.storageItemSlots;
    }

    @Override
    public Optional<BukkitMenu> getMenuByPage(int page) {
        return Optional.ofNullable(this.bukkitMenuMap.get(page));
    }

    @Override
    public Map<Integer, BukkitMenu> getMenuPages() {
        return Collections.unmodifiableMap(this.bukkitMenuMap);
    }

    @Override
    public int getSize() {
        return this.bukkitMenuMap.size();
    }

    @Override
    public List<HumanEntity> getViewers() {
        final List<HumanEntity> viewers = new ArrayList<>();

        this.bukkitMenuMap.values()
                .stream()
                .map(bukkitMenu -> bukkitMenu.getInventory().getViewers())
                .forEach(viewers::addAll);

        return viewers;
    }

    @Override
    public int getPlayerPage(@NonNull HumanEntity humanEntity) {
        return this.cacheSlotPlayerViewing.getOrDefault(humanEntity.getUniqueId(), 0);
    }

    @Override
    public BukkitMenuPaginated setNextPageSlot(int slot, @NonNull Consumer<HumanEntity> nextPageReach) {
        this.menuPlatform.getHolder().setActionOnSlot(slot, e -> {
            e.setCancelled(true);

            final HumanEntity humanEntity = e.getWhoClicked();
            final int nextPage = this.getPlayerPage(humanEntity) + 1;

            if (this.nextPagePreEvent != null) {
                this.nextPagePreEvent.accept(humanEntity, nextPage);
            }

            if (this.getSize() <= nextPage) {
                nextPageReach.accept(humanEntity);
                return;
            }

            if (this.nextPageEvent != null) {
                this.nextPageEvent.accept(humanEntity, nextPage);
            }

            this.open(nextPage, humanEntity);

            if (this.nextPagePostEvent != null) {
                this.nextPagePostEvent.accept(humanEntity, nextPage);
            }
        });

        return this;
    }

    @Override
    public BukkitMenuPaginated setNextPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> nextPageReach) {
        this.setNextPageSlot(slot, nextPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);

        return this;
    }

    @Override
    public BukkitMenuPaginated setNextPageSlot(int x, int z, @NonNull Consumer<HumanEntity> lastPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setNextPageSlot(slot, lastPageReach);

        return this;
    }

    @Override
    public BukkitMenuPaginated setNextPageSlot(int x, int z, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> lastPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setNextPageSlot(slot, itemStack, lastPageReach);

        return this;
    }

    @Override
    public BukkitMenuPaginated setPreviousPageSlot(int slot, @NonNull Consumer<HumanEntity> firstPageReach) {
        this.menuPlatform.getHolder().setActionOnSlot(slot, e -> {
            e.setCancelled(true);

            final HumanEntity humanEntity = e.getWhoClicked();
            final int previousPage = this.getPlayerPage(humanEntity) - 1;

            if (this.previousPagePreEvent != null) {
                this.previousPagePreEvent.accept(humanEntity, previousPage);
            }

            if (0 > previousPage) {
                firstPageReach.accept(humanEntity);
                return;
            }

            if (this.previousPageEvent != null) {
                this.previousPageEvent.accept(humanEntity, previousPage);
            }

            this.open(previousPage, humanEntity);

            if (this.previousPagePostEvent != null) {
                this.previousPagePostEvent.accept(humanEntity, previousPage);
            }
        });

        return this;
    }

    @Override
    public BukkitMenuPaginated setPreviousPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> firstPageReach) {
        this.setPreviousPageSlot(slot, firstPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);

        return this;
    }

    @Override
    public BukkitMenuPaginated setPreviousPageSlot(int x, int z, @NonNull Consumer<HumanEntity> firstPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setPreviousPageSlot(slot, firstPageReach);

        return this;
    }

    @Override
    public BukkitMenuPaginated setPreviousPageSlot(int x, int z, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> firstPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setPreviousPageSlot(slot, itemStack, firstPageReach);

        return this;
    }

    @Override
    public BukkitMenuPaginated addStorageItem(@NonNull BukkitMenu bukkitMenu, int page, @NonNull ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        final int slot = this.addItem(bukkitMenu, itemStack, this.storageItemSlots);
        if (slot != -1) {
            if (event != null) {
                bukkitMenu.getHolder().setActionOnSlot(slot, event.andThen(e -> {
                    if (e.isCancelled()) {
                        return;
                    }

                    bukkitMenu.getHolder().removeActionOnSlot(slot);
                }));
            }

            return this;
        }

        AtomicReference<BukkitMenu> nextMenuRef = new AtomicReference<>(this.bukkitMenuMap.get(page + 1));
        if (nextMenuRef.get() == null) {
            nextMenuRef.set(this.getMenuPlatform().cloneMenu(page + 2));
            this.bukkitMenuMap.put(page + 1, nextMenuRef.get());
        }

        final BukkitMenu nextMenu = nextMenuRef.get();
        final int nextMenuSlot = this.addItem(nextMenu, itemStack, this.storageItemSlots);
        if (nextMenuSlot != -1 && event != null) {
            nextMenu.getHolder().setActionOnSlot(nextMenuSlot, event.andThen(e -> {
                if (e.isCancelled()) {
                    return;
                }

                nextMenu.getHolder().removeActionOnSlot(nextMenuSlot);
            }));
        }

        return this;
    }

    @Override
    public BukkitMenuPaginated addStorageItem(@NonNull ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        final Optional<Map.Entry<Integer, BukkitMenu>> optionalMenu = this.bukkitMenuMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getKey));

        if (optionalMenu.isPresent()) {
            final int page = optionalMenu.get().getKey();
            final BukkitMenu bukkitMenu = optionalMenu.get().getValue();

            this.addStorageItem(bukkitMenu, page, itemStack, event);
        }
        else {
            final BukkitMenu bukkitMenu = this.getMenuPlatform().cloneMenu(1);
            this.bukkitMenuMap.put(0, bukkitMenu);

            this.addStorageItem(bukkitMenu, 0, itemStack, event);
        }

        return this;
    }

    @Override
    public BukkitMenuPaginated addStorageItem(@NonNull ItemStack itemStack) {
        this.addStorageItem(itemStack, null);

        return this;
    }

    @Override
    public BukkitMenuPaginated addStorageItems(@NonNull List<ItemStack> list, Consumer<InventoryClickEvent> event) {
        list.forEach(itemStack -> this.addStorageItem(itemStack, event));

        return this;
    }

    @Override
    public BukkitMenuPaginated addStorageItems(@NonNull List<ItemStack> list) {
        this.addStorageItems(list, null);

        return this;
    }

    @Override
    public BukkitMenuPaginated open(int page, @NonNull HumanEntity humanEntity) {
        BukkitMenu bukkitMenu = this.bukkitMenuMap.get(page);
        if (bukkitMenu == null) {
            this.openFirstPage(humanEntity);
            return this;
        }

        this.cacheSlotPlayerViewing.put(humanEntity.getUniqueId(), page);
        bukkitMenu.getHolder().open(humanEntity);

        return this;
    }

    @Override
    public BukkitMenuPaginated openPage(@NonNull HumanEntity humanEntity) {
        this.open(this.getPlayerPage(humanEntity), humanEntity);

        return this;
    }

    @Override
    public BukkitMenuPaginated openFirstPage(@NonNull HumanEntity humanEntity) {
        Option.ofOptional(this.bukkitMenuMap.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getKey))
                .ifPresentOrElse(page -> this.open(page, humanEntity), () -> {
                    this.bukkitMenuMap.put(0, this.getMenuPlatform().cloneMenu(1));
                    this.openFirstPage(humanEntity);
                });

        return this;
    }

    @Override
    public BukkitMenuPaginated openLastPage(@NonNull HumanEntity humanEntity) {
        this.bukkitMenuMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .ifPresent(page -> this.open(page, humanEntity));

        return this;
    }

    @Override
    public BukkitMenuPaginated dispose() {
        this.bukkitMenuMap.values().forEach(BukkitMenu::dispose);
        this.bukkitMenuMap.clear();
        this.cacheSlotPlayerViewing.clear();

        return this;
    }

    private int addItem(@NonNull BukkitMenu bukkitMenu, @NonNull ItemStack itemStack, @NonNull List<Integer> applySlots) {
        for (int slot = 0; slot < bukkitMenu.getSize(); slot++) {
            if (!applySlots.contains(slot)) {
                continue;
            }

            if (bukkitMenu.getInventory().getItem(slot) == null) {
                bukkitMenu.setItem(slot, itemStack);
                return slot;
            }
        }

        return -1;
    }
}
