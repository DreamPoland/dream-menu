package cc.dreamcode.menu.bukkit.base;

import cc.dreamcode.menu.core.base.DreamMenuPaginated;
import cc.dreamcode.menu.core.utilities.MenuUtil;
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
public class BukkitMenuPaginated implements DreamMenuPaginated<BukkitMenu, ItemStack, InventoryClickEvent, HumanEntity> {

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
    public void setNextPageSlot(int slot, @NonNull Consumer<HumanEntity> nextPageReach) {
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
    }

    @Override
    public void setNextPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> nextPageReach) {
        this.setNextPageSlot(slot, nextPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
    }

    @Override
    public void setNextPageSlot(int x, int z, @NonNull Consumer<HumanEntity> lastPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setNextPageSlot(slot, lastPageReach);
    }

    @Override
    public void setNextPageSlot(int x, int z, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> lastPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setNextPageSlot(slot, itemStack, lastPageReach);
    }

    @Override
    public void setPreviousPageSlot(int slot, @NonNull Consumer<HumanEntity> firstPageReach) {
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
    }

    @Override
    public void setPreviousPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> firstPageReach) {
        this.setPreviousPageSlot(slot, firstPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
    }

    @Override
    public void setPreviousPageSlot(int x, int z, @NonNull Consumer<HumanEntity> firstPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setPreviousPageSlot(slot, firstPageReach);
    }

    @Override
    public void setPreviousPageSlot(int x, int z, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> firstPageReach) {
        int slot = MenuUtil.countSlot(x, z);
        this.setPreviousPageSlot(slot, itemStack, firstPageReach);
    }

    @Override
    public void addStorageItem(@NonNull BukkitMenu bukkitMenu, int page, @NonNull ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        final int slot = bukkitMenu.addItem(itemStack, this.storageItemSlots);
        if (slot != -1 && event != null) {
            bukkitMenu.getHolder().setActionOnSlot(slot, event.andThen(e -> {
                if (e.isCancelled()) {
                    return;
                }

                bukkitMenu.getHolder().removeActionOnSlot(slot);
            }));

            return;
        }

        AtomicReference<BukkitMenu> nextMenu = new AtomicReference<>(this.bukkitMenuMap.get(page + 1));
        if (nextMenu.get() == null) {
            nextMenu.set(this.getMenuPlatform().cloneMenu(page + 2));
            this.bukkitMenuMap.put(page + 1, nextMenu.get());
        }

        final int nextMenuSlot = nextMenu.get().addItem(itemStack, this.storageItemSlots);
        if (nextMenuSlot != -1 && event != null) {
            nextMenu.get().getHolder().setActionOnSlot(nextMenuSlot, event.andThen(e -> {
                if (e.isCancelled()) {
                    return;
                }

                nextMenu.get().getHolder().removeActionOnSlot(nextMenuSlot);
            }));
        }
    }

    @Override
    public void addStorageItem(@NonNull ItemStack itemStack, Consumer<InventoryClickEvent> event) {
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
    }

    @Override
    public void addStorageItem(@NonNull ItemStack itemStack) {
        this.addStorageItem(itemStack, null);
    }

    @Override
    public void addStorageItems(@NonNull List<ItemStack> list, Consumer<InventoryClickEvent> event) {
        list.forEach(itemStack -> this.addStorageItem(itemStack, event));
    }

    @Override
    public void addStorageItems(@NonNull List<ItemStack> list) {
        this.addStorageItems(list, null);
    }

    @Override
    public void open(int page, @NonNull HumanEntity humanEntity) {
        BukkitMenu bukkitMenu = this.bukkitMenuMap.get(page);
        if (bukkitMenu == null) {
            this.openFirstPage(humanEntity);
            return;
        }
        this.cacheSlotPlayerViewing.put(humanEntity.getUniqueId(), page);

        bukkitMenu.getHolder().open(humanEntity);
    }

    @Override
    public void openPage(@NonNull HumanEntity humanEntity) {
        this.open(this.getPlayerPage(humanEntity), humanEntity);
    }

    @Override
    public void openFirstPage(@NonNull HumanEntity humanEntity) {
        Option.ofOptional(this.bukkitMenuMap.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getKey))
                .acceptOrElse(page -> this.open(page, humanEntity), () -> {
                    this.bukkitMenuMap.put(0, this.getMenuPlatform().cloneMenu(1));
                    this.openFirstPage(humanEntity);
                });
    }

    @Override
    public void openLastPage(@NonNull HumanEntity humanEntity) {
        this.bukkitMenuMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .ifPresent(page -> this.open(page, humanEntity));
    }

    @Override
    public void dispose() {
        this.bukkitMenuMap.values().forEach(BukkitMenu::dispose);
        this.bukkitMenuMap.clear();
        this.cacheSlotPlayerViewing.clear();
    }

}
