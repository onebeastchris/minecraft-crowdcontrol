package dev.qixils.crowdcontrol.plugin.commands;

import dev.qixils.crowdcontrol.TimedEffect;
import dev.qixils.crowdcontrol.plugin.SpongeCrowdControlPlugin;
import dev.qixils.crowdcontrol.plugin.TimedCommand;
import dev.qixils.crowdcontrol.socket.Request;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public final class FreezeCommand extends TimedCommand {
	private final Duration duration = Duration.ofSeconds(7);
	private final String effectName = "freeze";
	private final String displayName = "Freeze";

	public FreezeCommand(SpongeCrowdControlPlugin plugin) {
		super(plugin);
	}

	@Override
	public void voidExecute(@NotNull List<@NotNull Player> ignored, @NotNull Request request) {
		AtomicReference<Task> task = new AtomicReference<>();

		new TimedEffect.Builder()
				.request(request)
				.effectGroup("gamemode")
				.duration(duration)
				.startCallback($ -> {
					List<Player> players = plugin.getPlayers(request);
					Map<UUID, Location<World>> locations = new HashMap<>();
					players.forEach(player -> locations.put(player.getUniqueId(), player.getLocation()));
					task.set(Task.builder()
							.delayTicks(1)
							.intervalTicks(1)
							.execute(() -> players.forEach(player -> {
								if (!locations.containsKey(player.getUniqueId()))
									return;

								Location<World> location = locations.get(player.getUniqueId());
								Location<World> playerLoc = player.getLocation();
								if (!location.getExtent().equals(playerLoc.getExtent()))
									return;

								if (location.getX() != playerLoc.getX() || location.getY() != playerLoc.getY() || location.getZ() != playerLoc.getZ()) {
									player.setLocation(location);
								}
							}))
							.submit(plugin));
					playerAnnounce(players, request);
					return null;
				})
				.completionCallback($ -> task.get().cancel())
				.build().queue();
	}
}
