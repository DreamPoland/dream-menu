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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        this.storageItemSlots = IntStream.range(0, menuPlatform.getSize())
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
    public BukkitMenuPaginated setStorageItemSlots(@NonNull List<Integer> storageItemSlots) {
        this.storageItemSlots.clear();
        this.storageItemSlots.addAll(storageItemSlots);
        return this;
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
        if (humanEntity.getOpenInventory() != null && humanEntity.getOpenInventory().getTopInventory() != null) {
            final Inventory topInventory = humanEntity.getOpenInventory().getTopInventory();
            for (Map.Entry<Integer, BukkitMenu> entry : this.bukkitMenuMap.entrySet()) {
                if (entry.getValue().getInventory().equals(topInventory)) {
                    return entry.getKey();
                }
            }
        }

        return this.cacheSlotPlayerViewing.getOrDefault(humanEntity.getUniqueId(), 0);
    }

    @Override
    public BukkitMenuPaginated setNextPageSlot(int slot, @NonNull Consumer<HumanEntity> nextPageReach) {
        this.storageItemSlots.remove(Integer.valueOf(slot));

        final Consumer<InventoryClickEvent> action = e -> {
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
        };

        this.menuPlatform.getHolder().setActionOnSlot(slot, action);
        this.bukkitMenuMap.values().forEach(menu -> menu.getHolder().setActionOnSlot(slot, action));

        return this;
    }

    @Override
    public BukkitMenuPaginated setNextPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> nextPageReach) {
        this.setNextPageSlot(slot, nextPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
        this.bukkitMenuMap.values().forEach(menu -> menu.setItem(slot, itemStack));

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
        this.storageItemSlots.remove(Integer.valueOf(slot));

        final Consumer<InventoryClickEvent> action = e -> {
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
        };

        this.menuPlatform.getHolder().setActionOnSlot(slot, action);
        this.bukkitMenuMap.values().forEach(menu -> menu.getHolder().setActionOnSlot(slot, action));

        return this;
    }

    @Override
    public BukkitMenuPaginated setPreviousPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> firstPageReach) {
        this.setPreviousPageSlot(slot, firstPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
        this.bukkitMenuMap.values().forEach(menu -> menu.setItem(slot, itemStack));

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
                bukkitMenu.getHolder().setActionOnSlot(slot, event);
            }

            return this;
        }

        AtomicReference<BukkitMenu> nextMenuRef = new AtomicReference<>(this.bukkitMenuMap.get(page + 1));
        if (nextMenuRef.get() == null) {
            nextMenuRef.set(this.getMenuPlatform().cloneMenu(page + 2));
            this.bukkitMenuMap.put(page + 1, nextMenuRef.get());
        }

        final BukkitMenu nextMenu = nextMenuRef.get();
        return this.addStorageItem(nextMenu, page + 1, itemStack, event);
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
    public BukkitMenuPaginated clearStorageItems() {
        for (BukkitMenu menu : this.bukkitMenuMap.values()) {
            for (int slot : this.storageItemSlots) {
                menu.removeItem(slot);
            }
        }
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
    public BukkitMenuPaginated close(@NonNull HumanEntity humanEntity) {
        humanEntity.closeInventory();
        return this;
    }

    @Override
    public BukkitMenuPaginated closeAll() {
        this.getViewers().forEach(HumanEntity::closeInventory);
        return this;
    }

    @Override
    public BukkitMenuPaginated dispose() {
        new ArrayList<>(this.bukkitMenuMap.values())
                .stream()
                .filter(Objects::nonNull)
                .forEach(BukkitMenu::dispose);
        this.bukkitMenuMap.clear();
        this.cacheSlotPlayerViewing.clear();

        return this;
    }

    @Override
    public boolean isDisposed() {
        return this.bukkitMenuMap.values()
                .stream()
                .filter(Objects::nonNull)
                .allMatch(BukkitMenu::isDisposed);
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
