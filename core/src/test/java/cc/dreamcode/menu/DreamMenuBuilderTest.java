package cc.dreamcode.menu;

import lombok.NonNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DreamMenuBuilderTest {

    private static class TestMenuBuilder extends DreamMenuBuilder<String, String, String> {
        public TestMenuBuilder(String inventoryType, String name, int rows) {
            super(inventoryType, name, rows, new HashMap<>());
        }

        @Override
        public String buildEmpty() {
            return "empty";
        }

        @Override
        public String buildEmpty(@NonNull Map<String, Object> replaceMap) {
            return "empty";
        }

        @Override
        public String buildWithItems() {
            return "items";
        }

        @Override
        public String buildWithItems(@NonNull Map<String, Object> replaceMap) {
            return "items";
        }
    }

    @Test
    void shouldFillBackground() {
        TestMenuBuilder builder = new TestMenuBuilder("CHEST", "Test", 3);
        builder.fillBackground("STONE");

        assertEquals(27, builder.getItems().size());
        for (int i = 0; i < 27; i++) {
            assertEquals("STONE", builder.getItems().get(i));
        }
    }

    @Test
    void shouldNotOverwriteExistingOnFillBackground() {
        TestMenuBuilder builder = new TestMenuBuilder("CHEST", "Test", 1);
        builder.setItem(0, "DIAMOND");
        builder.fillBackground("STONE");

        assertEquals(9, builder.getItems().size());
        assertEquals("DIAMOND", builder.getItems().get(0));
        assertEquals("STONE", builder.getItems().get(1));
    }

    @Test
    void shouldFillMargin() {
        TestMenuBuilder builder = new TestMenuBuilder("CHEST", "Test", 3);
        builder.fillMargin("GLASS");

        // Top row: 0..8
        for (int i = 0; i < 9; i++) {
            assertEquals("GLASS", builder.getItems().get(i));
        }

        // Bottom row: 18..26
        for (int i = 18; i < 27; i++) {
            assertEquals("GLASS", builder.getItems().get(i));
        }

        // Middle row sides: row 2 col 1 (slot 9) and col 9 (slot 17)
        assertEquals("GLASS", builder.getItems().get(9));
        assertEquals("GLASS", builder.getItems().get(17));

        // Center slots should be empty
        assertFalse(builder.getItems().containsKey(10));
        assertFalse(builder.getItems().containsKey(13));
    }

    @Test
    void shouldFillRowAndColumn() {
        TestMenuBuilder builder = new TestMenuBuilder("CHEST", "Test", 3);
        builder.fillRow(2, "EMERALD");
        builder.fillColumn(5, "GOLD");

        // Row 2 is slots 9..17
        assertEquals("EMERALD", builder.getItems().get(9));
        assertEquals("EMERALD", builder.getItems().get(10));

        // Column 5 row 1 (slot 4) should be GOLD
        assertEquals("GOLD", builder.getItems().get(4));
        // Slot 13 was row 2 col 5, so filled with EMERALD first, fillColumn didn't overwrite because containsKey
        assertEquals("EMERALD", builder.getItems().get(13));
        // Column 5 row 3 (slot 22) should be GOLD
        assertEquals("GOLD", builder.getItems().get(22));
    }

    @Test
    void shouldHandleRemoveAndClear() {
        TestMenuBuilder builder = new TestMenuBuilder("CHEST", "Test", 2);
        builder.setItem(Arrays.asList(0, 1, 2), "DIRT");

        assertTrue(builder.hasItem(0));
        assertEquals("DIRT", builder.getItem(1).orElse(null));

        builder.removeItem(1);
        assertFalse(builder.hasItem(1));
        assertEquals(2, builder.getItems().size());

        builder.clearItems();
        assertEquals(0, builder.getItems().size());
    }
}
