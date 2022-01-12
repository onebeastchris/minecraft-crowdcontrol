package dev.qixils.crowdcontrol.plugin.paper.commands;

import dev.qixils.crowdcontrol.plugin.paper.Command;
import dev.qixils.crowdcontrol.plugin.paper.PaperCrowdControlPlugin;
import dev.qixils.crowdcontrol.socket.Request;
import dev.qixils.crowdcontrol.socket.Response.Builder;
import dev.qixils.crowdcontrol.socket.Response.ResultType;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static dev.qixils.crowdcontrol.common.CommandConstants.DINNERBONE_NAME;
import static dev.qixils.crowdcontrol.common.CommandConstants.DINNERBONE_RADIUS;

@Getter
public class DinnerboneCommand extends Command {
	private final String effectName = "dinnerbone";
	private final String displayName = "Flip Mobs Upside-Down";

	public DinnerboneCommand(PaperCrowdControlPlugin plugin) {
		super(plugin);
	}

	@Override
	public @NotNull CompletableFuture<Builder> execute(@NotNull List<@NotNull Player> players, @NotNull Request request) {
		Set<LivingEntity> entities = new HashSet<>();
		CompletableFuture<Boolean> successFuture = new CompletableFuture<>();
		sync(() -> {
			for (Player player : players) {
				entities.addAll(player.getLocation().getNearbyLivingEntities(DINNERBONE_RADIUS,
						x -> x.getType() != EntityType.PLAYER
								&& (x.getCustomName() == null
								|| x.getCustomName().isEmpty()
								|| x.getCustomName().equals(DINNERBONE_NAME)
								|| SummonEntityCommand.isMobViewerSpawned(plugin, x))));
			}
			successFuture.complete(!entities.isEmpty());
			entities.forEach(x -> {
				// TODO: save/restore old name
				x.setCustomNameVisible(false);
				x.setCustomName(Objects.equals(x.getCustomName(), DINNERBONE_NAME) ? null : DINNERBONE_NAME);
			});
		});
		return successFuture.thenApply(success -> success
				? request.buildResponse().type(ResultType.SUCCESS)
				: request.buildResponse().type(ResultType.RETRY).message("No nearby entities"));
	}
}
