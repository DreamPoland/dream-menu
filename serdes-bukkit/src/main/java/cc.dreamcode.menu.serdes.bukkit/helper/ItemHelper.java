package cc.dreamcode.menu.serdes.bukkit.helper;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ItemHelper {

    private final ItemStack itemStack;

    public ItemStack fixColors(Map<String, String> replaceMap) {
        ItemStack is = new ItemStack(this.itemStack);
        ItemMeta im = is.getItemMeta();
        assert im != null;

        if(im.hasDisplayName()) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    new PlaceholderHelper(im.getDisplayName()).replaceFromMap(replaceMap)));
        }

        if(im.hasLore()) {
            im.setLore(Objects.requireNonNull(im.getLore())
                    .stream()
                    .map(text -> new PlaceholderHelper(text).replaceFromMap(replaceMap))
                    .collect(Collectors.toList()));
        }

        is.setItemMeta(im);
        return is;
    }

}
