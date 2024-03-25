package cc.dreamcode.menu.core.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuUtil {
    public static int countSlot(int x, int z) {
        return (x - 1) * 9 + (z - 1);
    }
}
