package dev.qixils.crowdcontrol.plugin.paper.commands;

import dev.qixils.crowdcontrol.common.util.CompletableFutureUtils;
import dev.qixils.crowdcontrol.plugin.paper.Command;
import dev.qixils.crowdcontrol.plugin.paper.PaperCrowdControlPlugin;
import dev.qixils.crowdcontrol.socket.Request;
import dev.qixils.crowdcontrol.socket.Response.Builder;
import dev.qixils.crowdcontrol.socket.Response.ResultType;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.qixils.crowdcontrol.common.command.CommandConstants.DINNERBONE_COMPONENT;
import static dev.qixils.crowdcontrol.common.command.CommandConstants.DINNERBONE_RADIUS;
import static dev.qixils.crowdcontrol.plugin.paper.PaperCrowdControlPlugin.COMPONENT_TYPE;

@Getter
public class DinnerboneCommand extends Command {
	private final NamespacedKey key;
	private final String effectName = "dinnerbone";

	public DinnerboneCommand(PaperCrowdControlPlugin plugin) {
		super(plugin);
		this.key = new NamespacedKey(plugin, "original_name");
	}

	@Override
	public @NotNull CompletableFuture<Builder> execute(@NotNull List<@NotNull Player> players, @NotNull Request request) {
		List<CompletableFuture<Boolean>> successFutures = new ArrayList<>();
		for (Player player : players) {
			Location location = player.getLocation();
			CompletableFuture<Boolean> future = new CompletableFuture<>();
			successFutures.add(future);
			sync(location, () -> {
				Collection<LivingEntity> entities = location.getNearbyLivingEntities(DINNERBONE_RADIUS, e -> e.getType() != EntityType.PLAYER);
				future.complete(!entities.isEmpty());
				for (LivingEntity entity : entities) {
					PersistentDataContainer data = entity.getPersistentDataContainer();
					Component currentName = entity.customName();
					if (DINNERBONE_COMPONENT.equals(currentName)) {
						Component savedName = data.get(key, COMPONENT_TYPE);
						entity.customName(savedName);
						if (savedName != null)
							entity.setCustomNameVisible(true);
						data.remove(key);
					} else {
						if (currentName != null)
							data.set(key, COMPONENT_TYPE, currentName);
						entity.customName(DINNERBONE_COMPONENT);
						entity.setCustomNameVisible(false);
					}
				}
			});
		}
		return CompletableFutureUtils.allOf(successFutures).thenApply(successes -> successes.stream().anyMatch(Boolean::booleanValue)
				? request.buildResponse().type(ResultType.SUCCESS)
				: request.buildResponse().type(ResultType.RETRY).message("No nearby entities"));
	}
}
