package dev.qixils.crowdcontrol.plugin.sponge8.commands;

import dev.qixils.crowdcontrol.common.CommandConstants.EnchantmentWeights;
import dev.qixils.crowdcontrol.common.util.RandomUtil;
import dev.qixils.crowdcontrol.common.util.sound.Sounds;
import dev.qixils.crowdcontrol.exceptions.ExceptionUtil;
import dev.qixils.crowdcontrol.plugin.sponge8.ImmediateCommand;
import dev.qixils.crowdcontrol.plugin.sponge8.SpongeCrowdControlPlugin;
import dev.qixils.crowdcontrol.socket.Request;
import dev.qixils.crowdcontrol.socket.Response;
import lombok.Getter;
import net.kyori.adventure.sound.Sound.Emitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dev.qixils.crowdcontrol.common.CommandConstants.buildLootboxLore;
import static dev.qixils.crowdcontrol.common.CommandConstants.buildLootboxTitle;

@Getter
public class LootboxCommand extends ImmediateCommand {
	private final List<ItemType> allItems;
	private final List<ItemType> goodItems;
	private final String effectName = "lootbox";
	private final String displayName = "Open Lootbox";

	public LootboxCommand(SpongeCrowdControlPlugin plugin) {
		super(plugin);
		allItems = plugin.getGame().registry(RegistryTypes.ITEM_TYPE).stream().collect(Collectors.toList());
		goodItems = allItems.stream()
				.filter(itemType ->
						itemType.get(Keys.MAX_DURABILITY).orElse(0) > 1
								|| itemType.equals(ItemTypes.GOLDEN_APPLE.get())
								|| itemType.equals(ItemTypes.ENCHANTED_GOLDEN_APPLE.get())
								|| itemType.equals(ItemTypes.NETHERITE_BLOCK.get())
								|| itemType.equals(ItemTypes.DIAMOND_BLOCK.get())
								|| itemType.equals(ItemTypes.IRON_BLOCK.get())
								|| itemType.equals(ItemTypes.GOLD_BLOCK.get()))
				.collect(Collectors.toList());
	}

	private boolean isGoodItem(@Nullable ItemType item) {
		return item != null && goodItems.contains(item);
	}

	/**
	 * Creates a random item that is influenced by the supplied luck value.
	 * The item may contain enchantments and modifiers if applicable.
	 *
	 * @param luck zero-indexed level of luck
	 * @return new random item
	 */
	public ItemStack createRandomItem(int luck) {
		// determine the item used in the stack
		// "good" items have a higher likelihood of being picked with positive luck
		List<ItemType> items = new ArrayList<>(allItems);
		Collections.shuffle(items, random);
		ItemType item = null;
		for (int i = 0; i <= luck * 4; i++) {
			ItemType oldItem = item;
			item = items.get(i);
			if (isGoodItem(item) && !isGoodItem(oldItem))
				break;
		}
		assert item != null;

		// determine the size of the item stack
		int quantity = 1;
		if (item.maxStackQuantity() > 1) {
			for (int i = 0; i <= luck; i++) {
				quantity = Math.max(quantity, RandomUtil.nextInclusiveInt(1, item.maxStackQuantity()));
			}
		}

		// create item stack
		ItemStack itemStack = ItemStack.of(item, quantity);
		// make item unbreakable with a default chance of 10% (up to 100% at 6 luck)
		if (random.nextDouble() >= (0.9D - (luck * .15D)))
			itemStack.offer(Keys.IS_UNBREAKABLE, true);

		// determine enchantments to add
		int enchantments = 0;
		for (int i = 0; i <= luck; i++) {
			enchantments = Math.max(enchantments, RandomUtil.weightedRandom(EnchantmentWeights.values(), EnchantmentWeights.TOTAL_WEIGHTS).getLevel());
		}
		List<EnchantmentType> enchantmentList = plugin.getGame().registry(RegistryTypes.ENCHANTMENT_TYPE).stream()
				.filter(enchantmentType -> enchantmentType.canBeAppliedToStack(itemStack)).collect(Collectors.toList());
		List<EnchantmentType> addedEnchantments = new ArrayList<>(enchantments);

		// add enchantments
		while (enchantments > 0 && !enchantmentList.isEmpty()) {
			EnchantmentType enchantment = enchantmentList.remove(0);

			// block conflicting enchantments (unless the die roll decides otherwise)
			if (addedEnchantments.stream().anyMatch(x -> !x.isCompatibleWith(enchantment)) && random.nextDouble() >= (.1d + (luck * .1d)))
				continue;
			addedEnchantments.add(enchantment);
			enchantments--;

			// determine enchantment level
			int level = enchantment.minimumLevel();
			if (enchantment.maximumLevel() > enchantment.minimumLevel()) {
				for (int j = 0; j <= luck; j++) {
					level = Math.max(level, RandomUtil.nextInclusiveInt(enchantment.minimumLevel(), enchantment.maximumLevel()));
				}
				if (random.nextDouble() >= (0.5D - (luck * .07D)))
					level += random.nextInt(4);
			}

			// create & add enchant
			Enchantment builtEnchantment = Enchantment.builder()
					.type(enchantment)
					.level(level)
					.build();
			itemStack.transform(Keys.APPLIED_ENCHANTMENTS, enchants -> {
				enchants = ExceptionUtil.validateNotNullElseGet(enchants, ArrayList::new);
				enchants.add(builtEnchantment);
				return enchants;
			});
		}

		// API8: attributes
		// (attributes kinda exist in Sponge 7 but they are not available as a CatalogType, just
		//  as Keys, and even then I don't think I can add them to items)

		return itemStack;
	}

	@Override
	public Response.@NotNull Builder executeImmediately(@NotNull List<@NotNull ServerPlayer> players, @NotNull Request request) {
		for (ServerPlayer player : players) {
			ViewableInventory inventory = Inventory.builder()
					.grid(9, 3)
					.completeStructure()
					.plugin(plugin.getPluginContainer())
					.build()
					.asViewable().orElseThrow(() -> new IllegalStateException("Could not create custom inventory"));
			InventoryMenu menu = inventory.asMenu();
			menu.setTitle(buildLootboxTitle(request));
			menu.setReadOnly(false);

			// create item
			ItemStack itemStack = createRandomItem(0);
			itemStack.offer(
					Keys.LORE,
					Collections.singletonList(buildLootboxLore(request))
			);

			// the custom inventory does not implement anything sensible so enjoy this hack
			int i = 0;
			for (Slot slot : inventory.slots()) {
				if (i++ == 13) {
					slot.offer(itemStack);
					break;
				}
			}
			// sound & open
			player.playSound(Sounds.LOOTBOX_CHIME.get(), (Emitter) player);
			sync(() -> menu.open(player));
		}
		return request.buildResponse().type(Response.ResultType.SUCCESS);
	}
}
