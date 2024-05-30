package cc.dreamcode.menu.bukkit.adventure;

import cc.dreamcode.utilities.bukkit.StringColorUtil;
import eu.okaeri.placeholders.context.PlaceholderContext;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class AdventureLegacy {

    private static final Pattern FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)}");

    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new AdventureLegacyColor())
            .build();

    public static Component component(@NonNull String text) {
        return AMPERSAND_SERIALIZER.deserialize(text);
    }

    public static Component deserialize(@NonNull String text) {
        return deserialize(text, null);
    }

    public static Component deserialize(@NonNull String text, TextReplacementConfig textReplacementConfig) {

        Component component = MINI_MESSAGE.deserialize(text);

        if (textReplacementConfig != null) {
            component = component.replaceText(textReplacementConfig);
        }

        return component;
    }

    public static TextReplacementConfig getPlaceholderConfig(@NonNull PlaceholderContext placeholderContext) {

        Map<String, String> renderedFields = placeholderContext.renderFields()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getRaw(),
                        Map.Entry::getValue
                ));

        return TextReplacementConfig.builder()
                .match(FIELD_PATTERN)
                .replacement((result, input) -> {
                    String fieldValue = StringColorUtil.fixColor(renderedFields.get(result.group(1)));
                    return AdventureLegacy.component(fieldValue);
                })
                .build();
    }
}
