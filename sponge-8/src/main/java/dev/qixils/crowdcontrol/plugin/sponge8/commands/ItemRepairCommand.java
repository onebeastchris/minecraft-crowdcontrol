package dev.qixils.crowdcontrol.plugin.sponge8.commands;

import dev.qixils.crowdcontrol.plugin.sponge8.SpongeCrowdControlPlugin;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;

public final class ItemRepairCommand extends ItemDurabilityCommand {
	public ItemRepairCommand(SpongeCrowdControlPlugin plugin) {
		super(plugin, "Repair Item");
	}

	@Override
	protected void modifyDurability(MutableBoundedValue<Integer> data, int maxDurability) {
		data.set(maxDurability);
	}
}
