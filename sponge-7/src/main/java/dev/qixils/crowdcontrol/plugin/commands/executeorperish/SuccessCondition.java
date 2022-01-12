package dev.qixils.crowdcontrol.plugin.commands.executeorperish;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.Player;

public interface SuccessCondition {
	Component getComponent();

	boolean hasSucceeded(Player player);
}
