package cc.dreamcode.menu.utilities;

import lombok.experimental.UtilityClass;

import java.util.stream.IntStream;

@UtilityClass
public class MenuUtil {

    public static int countSlot(int x, int z) {
        return (x - 1) * 9 + (z - 1);
    }

    public static int countRow(int slot) {
        return (slot / 9) + 1;
    }

    public static int countColumn(int slot) {
        return (slot % 9) + 1;
    }

    public static int[] countRowSlots(int row) {
        final int start = (row - 1) * 9;
        return IntStream.range(start, start + 9).toArray();
    }

    public static int[] countColumnSlots(int column, int rows) {
        final int[] slots = new int[rows];
        for (int r = 1; r <= rows; r++) {
            slots[r - 1] = countSlot(r, column);
        }
        return slots;
    }

    public static int[] countMarginSlots(int rows) {
        if (rows <= 0) {
            return new int[0];
        }
        if (rows == 1) {
            return countRowSlots(1);
        }

        final IntStream top = IntStream.range(0, 9);
        final IntStream bottom = IntStream.range((rows - 1) * 9, rows * 9);
        final IntStream middle = IntStream.range(1, rows - 1)
                .flatMap(row -> IntStream.of(row * 9, row * 9 + 8));

        return IntStream.concat(top, IntStream.concat(middle, bottom)).distinct().toArray();
    }

    public static boolean isMarginSlot(int slot, int rows) {
        if (slot < 0 || slot >= rows * 9) {
            return false;
        }
        final int row = countRow(slot);
        final int col = countColumn(slot);
        return row == 1 || row == rows || col == 1 || col == 9;
    }

    public static boolean isValidSlot(int slot, int rows) {
        return slot >= 0 && slot < rows * 9;
    }
}
