package dev.qixils.crowdcontrol.plugin.sponge7.utils;

import dev.qixils.crowdcontrol.common.util.TextUtil;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.translation.Translatable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.text.translation.Translation;

public class Sponge7TextUtil extends TextUtil {
	public Sponge7TextUtil() {
		super(ComponentFlattener.basic());
	}

	@NotNull
	public static String getFixedName(org.spongepowered.api.text.translation.@NotNull Translatable translatable) {
		if (translatable.equals(EntityTypes.RIDEABLE_MINECART))
			return "Minecart";
		if (translatable.equals(EntityTypes.CHESTED_MINECART))
			return "Minecart with Chest";
		if (translatable.equals(EntityTypes.FURNACE_MINECART))
			return "Minecart with Furnace";
		if (translatable.equals(EntityTypes.TNT_MINECART))
			return "Minecart with TNT";
		if (translatable.equals(EntityTypes.HOPPER_MINECART))
			return "Minecart with Hopper";
		return translatable.getTranslation().get();
	}

	@Override
	public @NotNull String translate(@NotNull Translatable translatable) {
		String key = translatable.translationKey();
		return Sponge.getRegistry().getTranslationById(key).map(Translation::get).orElse(key);
	}

	public static String valueOf(CatalogType type) {
		return type.getId().replaceFirst("minecraft:", "");
	}

	/**
	 * Returns the ID of an object according to the C# file.
	 *
	 * @param type typed object
	 * @return its CS file ID
	 */
	public static String csIdOf(CatalogType type) {
		String value = valueOf(type);
		switch (value) {
			case "villager_golem":
				return "iron_golem";
			case "web":
				return "cobweb";
			case "mooshroom":
				return "mushroom_cow";
			case "tnt":
				return "primed_tnt";
			case "zombie_pigman":
				return "zombified_piglin";
			case "illusioner_villager":
				return "illusioner";
			case "vindication_illager":
				return "vindicator";
			default:
				return value;
		}
	}
}
