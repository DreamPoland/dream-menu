package cc.dreamcode.menu.serdes.bukkit.helper;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class PlaceholderHelper {

    private final String text;

    public String replaceFromMap(Map<String, String> map) {
        if (map == null) {
            return this.text;
        }

        return map.entrySet().stream()
                .map(entryToReplace -> (Function<String, String>) s ->
                        s.replace(entryToReplace.getKey(), entryToReplace.getValue()))
                .reduce(Function.identity(), Function::andThen)
                .apply(this.text);
    }

}
