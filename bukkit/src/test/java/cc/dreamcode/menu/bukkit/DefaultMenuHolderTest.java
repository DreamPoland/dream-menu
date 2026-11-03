package cc.dreamcode.menu.bukkit;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.bukkit.holder.DefaultMenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultMenuHolderTest {

    private Inventory topInventory;
    private Inventory bottomInventory;
    private HumanEntity humanEntity;
    private InventoryView inventoryView;
    private BukkitMenu bukkitMenu;
    private DefaultMenuHolder holder;

    private AtomicBoolean inventoryCleared;
    private AtomicBoolean playerClosed;
    private Map<Integer, ItemStack> inventoryItems;

    @BeforeEach
    void setUp() throws Exception {
        inventoryCleared = new AtomicBoolean(false);
        playerClosed = new AtomicBoolean(false);
        inventoryItems = new HashMap<>();

        humanEntity = (HumanEntity) Proxy.newProxyInstance(
                HumanEntity.class.getClassLoader(),
                new Class<?>[]{HumanEntity.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("closeInventory")) {
                        playerClosed.set(true);
                        return null;
                    }
                    if (method.getName().equals("equals")) {
                        return proxy == args[0];
                    }
                    if (method.getName().equals("hashCode")) {
                        return System.identityHashCode(proxy);
                    }
                    return null;
                }
        );

        topInventory = (Inventory) Proxy.newProxyInstance(
                Inventory.class.getClassLoader(),
                new Class<?>[]{Inventory.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getSize")) {
                        return 27;
                    }
                    if (method.getName().equals("clear")) {
                        inventoryCleared.set(true);
                        inventoryItems.clear();
                        return null;
                    }
                    if (method.getName().equals("getViewers")) {
                        return Collections.singletonList(humanEntity);
                    }
                    if (method.getName().equals("setItem")) {
                        int slot = (int) args[0];
                        ItemStack item = (ItemStack) args[1];
                        inventoryItems.put(slot, item);
                        return null;
                    }
                    if (method.getName().equals("getItem")) {
                        int slot = (int) args[0];
                        return inventoryItems.get(slot);
                    }
                    return null;
                }
        );

        bottomInventory = (Inventory) Proxy.newProxyInstance(
                Inventory.class.getClassLoader(),
                new Class<?>[]{Inventory.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getSize")) {
                        return 36;
                    }
                    return null;
                }
        );

        inventoryView = new InventoryView() {
            @Override
            public Inventory getTopInventory() {
                return topInventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return bottomInventory;
            }

            @Override
            public HumanEntity getPlayer() {
                return humanEntity;
            }

            @Override
            public InventoryType getType() {
                return InventoryType.CHEST;
            }
        };

        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        bukkitMenu = (BukkitMenu) unsafe.allocateInstance(BukkitMenu.class);

        Field invField = BukkitMenu.class.getDeclaredField("inventory");
        invField.setAccessible(true);
        invField.set(bukkitMenu, topInventory);

        Field sizeField = BukkitMenu.class.getDeclaredField("size");
        sizeField.setAccessible(true);
        sizeField.set(bukkitMenu, 27);

        holder = new DefaultMenuHolder(bukkitMenu);

        Field holderField = BukkitMenu.class.getDeclaredField("defaultMenuHolder");
        holderField.setAccessible(true);
        holderField.set(bukkitMenu, holder);
    }

    @Test
    void shouldCancelClickByDefault() {
        InventoryClickEvent event = new InventoryClickEvent(
                inventoryView,
                InventoryType.SlotType.CONTAINER,
                5,
                ClickType.LEFT,
                InventoryAction.PICKUP_ALL
        );

        holder.handleClick(event);

        assertTrue(event.isCancelled());
    }

    @Test
    void shouldExecuteActionWhenClickedInsideTopInventory() {
        InventoryClickEvent event = new InventoryClickEvent(
                inventoryView,
                InventoryType.SlotType.CONTAINER,
                10,
                ClickType.LEFT,
                InventoryAction.PICKUP_ALL
        );

        AtomicInteger counter = new AtomicInteger();
        holder.setActionOnSlot(10, e -> counter.incrementAndGet());

        holder.handleClick(event);

        assertEquals(1, counter.get());
        assertTrue(event.isCancelled());
    }

    @Test
    void shouldNotExecuteActionWhenClickedInPlayerInventory() {
        // Raw slot 27 belongs to player's bottom inventory
        InventoryClickEvent event = new InventoryClickEvent(
                inventoryView,
                InventoryType.SlotType.CONTAINER,
                27,
                ClickType.LEFT,
                InventoryAction.PICKUP_ALL
        );

        AtomicInteger counter = new AtomicInteger();
        holder.setActionOnSlot(27, e -> counter.incrementAndGet());

        holder.handleClick(event);

        assertEquals(0, counter.get());
        assertTrue(event.isCancelled());
    }

    @Test
    void shouldCancelDragTouchingTopInventory() {
        ItemStack cursor = new ItemStack(Material.STONE);
        Map<Integer, ItemStack> newItems = new HashMap<>();
        newItems.put(2, new ItemStack(Material.STONE));
        newItems.put(30, new ItemStack(Material.STONE));

        InventoryDragEvent event = new InventoryDragEvent(
                inventoryView,
                cursor,
                cursor,
                false,
                newItems
        );

        holder.handleDrag(event);

        assertTrue(event.isCancelled());
    }

    @Test
    void shouldNotCancelDragWhenOnlyInBottomInventory() {
        ItemStack cursor = new ItemStack(Material.STONE);
        Map<Integer, ItemStack> newItems = new HashMap<>();
        newItems.put(28, new ItemStack(Material.STONE));
        newItems.put(35, new ItemStack(Material.STONE));

        InventoryDragEvent event = new InventoryDragEvent(
                inventoryView,
                cursor,
                cursor,
                false,
                newItems
        );

        holder.handleDrag(event);

        assertFalse(event.isCancelled());
    }

    @Test
    void shouldCloseViewersAndClearOnDispose() {
        holder.dispose();

        assertTrue(playerClosed.get());
        assertTrue(inventoryCleared.get());
        assertTrue(holder.getSlotActions().isEmpty());
    }

    @Test
    void shouldRemoveItemAndSlotAction() {
        bukkitMenu.setItem(3, new ItemStack(Material.APPLE), e -> {});
        assertTrue(bukkitMenu.hasItem(3));
        assertTrue(holder.getSlotActions().containsKey(3));

        bukkitMenu.removeItem(3);
        assertFalse(bukkitMenu.hasItem(3));
        assertFalse(holder.getSlotActions().containsKey(3));
    }

    @Test
    void shouldManageActionsDirectlyOnMenu() {
        bukkitMenu.setActionOnSlot(7, e -> {});
        assertTrue(holder.getSlotActions().containsKey(7));

        bukkitMenu.removeActionOnSlot(7);
        assertFalse(holder.getSlotActions().containsKey(7));
    }

    @Test
    void shouldCloseAllViewersFromMenu() {
        bukkitMenu.closeAll();
        assertTrue(playerClosed.get());
    }

    @Test
    void shouldNotRecurseWhenDisposeWhenCloseIsTrueOnPaperLeaf() {
        holder.setDisposeWhenClose(true);

        AtomicInteger closeCount = new AtomicInteger(0);
        // Simulate Paper/Leaf: calling humanEntity.closeInventory() synchronously triggers handleClose
        HumanEntity paperPlayer = (HumanEntity) Proxy.newProxyInstance(
                HumanEntity.class.getClassLoader(),
                new Class<?>[]{HumanEntity.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("closeInventory")) {
                        closeCount.incrementAndGet();
                        InventoryCloseEvent recursiveEvent = new InventoryCloseEvent(new InventoryView() {
                            @Override
                            public Inventory getTopInventory() {
                                return topInventory;
                            }

                            @Override
                            public Inventory getBottomInventory() {
                                return bottomInventory;
                            }

                            @Override
                            public HumanEntity getPlayer() {
                                return (HumanEntity) proxy;
                            }

                            @Override
                            public InventoryType getType() {
                                return InventoryType.CHEST;
                            }
                        });
                        holder.handleClose(recursiveEvent);
                        return null;
                    }
                    if (method.getName().equals("equals")) {
                        return proxy == args[0];
                    }
                    if (method.getName().equals("hashCode")) {
                        return System.identityHashCode(proxy);
                    }
                    return null;
                }
        );

        InventoryView paperView = new InventoryView() {
            @Override
            public Inventory getTopInventory() {
                return topInventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return bottomInventory;
            }

            @Override
            public HumanEntity getPlayer() {
                return paperPlayer;
            }

            @Override
            public InventoryType getType() {
                return InventoryType.CHEST;
            }
        };

        // Player is still in viewers during InventoryCloseEvent in Paper/Leaf
        Inventory paperInventory = (Inventory) Proxy.newProxyInstance(
                Inventory.class.getClassLoader(),
                new Class<?>[]{Inventory.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getSize")) {
                        return 27;
                    }
                    if (method.getName().equals("clear")) {
                        inventoryCleared.set(true);
                        return null;
                    }
                    if (method.getName().equals("getViewers")) {
                        return Collections.singletonList(paperPlayer);
                    }
                    return null;
                }
        );

        try {
            Field invField = BukkitMenu.class.getDeclaredField("inventory");
            invField.setAccessible(true);
            invField.set(bukkitMenu, paperInventory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        InventoryCloseEvent event = new InventoryCloseEvent(paperView);
        holder.handleClose(event);

        // closeInventory should NOT have been called on the closing player
        assertEquals(0, closeCount.get());
        assertTrue(inventoryCleared.get());
        assertTrue(holder.isDisposed());
        assertTrue(bukkitMenu.isDisposed());
    }

    @Test
    void shouldCancelClickWhenDisposed() {
        holder.dispose();

        InventoryClickEvent event = new InventoryClickEvent(
                inventoryView,
                InventoryType.SlotType.CONTAINER,
                5,
                ClickType.LEFT,
                InventoryAction.PICKUP_ALL
        );
        event.setCancelled(false);

        holder.handleClick(event);

        assertTrue(event.isCancelled());
    }

    @Test
    void shouldBeIdempotentOnMultipleDisposeCalls() {
        assertFalse(holder.isDisposed());
        holder.dispose();
        assertTrue(holder.isDisposed());

        playerClosed.set(false);
        inventoryCleared.set(false);

        holder.dispose();
        // Since it's already disposed, it shouldn't try to close players or clear again
        assertFalse(playerClosed.get());
        assertFalse(inventoryCleared.get());
        assertTrue(holder.isDisposed());
    }
}
