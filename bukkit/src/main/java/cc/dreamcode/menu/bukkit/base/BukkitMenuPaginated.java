package cc.dreamcode.menu.bukkit.base;

import cc.dreamcode.menu.core.base.DreamMenuPaginated;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class BukkitMenuPaginated implements DreamMenuPaginated<BukkitMenu, ItemStack, InventoryClickEvent, HumanEntity> {

    private final BukkitMenu menuPlatform;
    private final Map<Integer, BukkitMenu> bukkitMenuMap = new HashMap<>();
    private final Map<UUID, Integer> cacheSlotPlayerViewing = new HashMap<>();

    @Override
    public BukkitMenu getMenuPlatform() {
        return this.menuPlatform;
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

            if (this.bukkitMenuMap.size() <= nextPage) {
                nextPageReach.accept(humanEntity);
                return;
            }

            this.open(nextPage, humanEntity);
        });
    }

    @Override
    public void setNextPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> nextPageReach) {
        this.setNextPageSlot(slot, nextPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
    }

    @Override
    public void setPreviousPageSlot(int slot, @NonNull Consumer<HumanEntity> firstPageReach) {
        this.menuPlatform.getHolder().setActionOnSlot(slot, e -> {
            e.setCancelled(true);

            final HumanEntity humanEntity = e.getWhoClicked();
            final int previousPage = this.getPlayerPage(humanEntity) - 1;

            if (0 > previousPage) {
                firstPageReach.accept(humanEntity);
                return;
            }

            this.open(previousPage, humanEntity);
        });
    }

    @Override
    public void setPreviousPageSlot(int slot, @NonNull ItemStack itemStack, @NonNull Consumer<HumanEntity> firstPageReach) {
        this.setPreviousPageSlot(slot, firstPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
    }

    @Override
    public void addStorageItem(@NonNull BukkitMenu bukkitMenu, int page, @NonNull ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        final int slot = bukkitMenu.addItem(itemStack);
        if (slot != -1) {
            if (event != null) {
                bukkitMenu.getHolder().setActionOnSlot(slot, event.andThen(e -> {
                    if (e.isCancelled()) {
                        return;
                    }

                    bukkitMenu.getHolder().removeActionOnSlot(slot);
                }));
            }
            return;
        }

        AtomicReference<BukkitMenu> nextMenu = new AtomicReference<>(this.bukkitMenuMap.get(page + 1));
        if (nextMenu.get() == null) {
            nextMenu.set(this.getMenuPlatform().cloneMenu(page + 2));
            this.bukkitMenuMap.put(page + 1, nextMenu.get());
        }

        final int nextMenuSlot = nextMenu.get().addItem(itemStack);
        if (nextMenuSlot != -1) {
            if (event != null) {
                nextMenu.get().getHolder().setActionOnSlot(nextMenuSlot, event.andThen(e -> {
                    if (e.isCancelled()) {
                        return;
                    }

                    nextMenu.get().getHolder().removeActionOnSlot(nextMenuSlot);
                }));
            }
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
        this.bukkitMenuMap.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .ifPresent(bukkitMenu -> bukkitMenu.open(humanEntity));
    }

    @Override
    public void openLastPage(@NonNull HumanEntity humanEntity) {
        this.bukkitMenuMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .ifPresent(bukkitMenu -> bukkitMenu.open(humanEntity));
    }

}
