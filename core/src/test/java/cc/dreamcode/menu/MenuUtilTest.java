package cc.dreamcode.menu;

import cc.dreamcode.menu.utilities.MenuUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuUtilTest {

    @Test
    void shouldCountSlotCorrectly() {
        assertEquals(0, MenuUtil.countSlot(1, 1));
        assertEquals(8, MenuUtil.countSlot(1, 9));
        assertEquals(9, MenuUtil.countSlot(2, 1));
        assertEquals(17, MenuUtil.countSlot(2, 9));
        assertEquals(53, MenuUtil.countSlot(6, 9));
    }

    @Test
    void shouldCountRowAndColumnCorrectly() {
        assertEquals(1, MenuUtil.countRow(0));
        assertEquals(1, MenuUtil.countColumn(0));

        assertEquals(1, MenuUtil.countRow(8));
        assertEquals(9, MenuUtil.countColumn(8));

        assertEquals(2, MenuUtil.countRow(9));
        assertEquals(1, MenuUtil.countColumn(9));

        assertEquals(6, MenuUtil.countRow(53));
        assertEquals(9, MenuUtil.countColumn(53));
    }

    @Test
    void shouldCountRowSlots() {
        assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, MenuUtil.countRowSlots(1));
        assertArrayEquals(new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17}, MenuUtil.countRowSlots(2));
    }

    @Test
    void shouldCountColumnSlots() {
        assertArrayEquals(new int[]{0, 9, 18}, MenuUtil.countColumnSlots(1, 3));
        assertArrayEquals(new int[]{8, 17, 26}, MenuUtil.countColumnSlots(9, 3));
    }

    @Test
    void shouldDetectMarginSlots() {
        assertTrue(MenuUtil.isMarginSlot(0, 3)); // Top-left
        assertTrue(MenuUtil.isMarginSlot(8, 3)); // Top-right
        assertTrue(MenuUtil.isMarginSlot(18, 3)); // Bottom-left
        assertTrue(MenuUtil.isMarginSlot(26, 3)); // Bottom-right
        assertTrue(MenuUtil.isMarginSlot(9, 3)); // Middle-left
        assertTrue(MenuUtil.isMarginSlot(17, 3)); // Middle-right

        assertFalse(MenuUtil.isMarginSlot(10, 3)); // Inner slot
        assertFalse(MenuUtil.isMarginSlot(13, 3)); // Center slot
    }

    @Test
    void shouldValidateSlotBounds() {
        assertTrue(MenuUtil.isValidSlot(0, 3));
        assertTrue(MenuUtil.isValidSlot(26, 3));
        assertFalse(MenuUtil.isValidSlot(27, 3));
        assertFalse(MenuUtil.isValidSlot(-1, 3));
    }
}
