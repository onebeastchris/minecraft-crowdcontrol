package dev.qixils.crowdcontrol.plugin.sponge8.commands;

import dev.qixils.crowdcontrol.plugin.sponge8.ImmediateCommand;
import dev.qixils.crowdcontrol.plugin.sponge8.SpongeCrowdControlPlugin;
import dev.qixils.crowdcontrol.socket.Request;
import dev.qixils.crowdcontrol.socket.Response;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.RespawnLocation;
import org.spongepowered.api.world.WorldTypes;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.WorldManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class RespawnCommand extends ImmediateCommand {
	private final String effectName = "respawn";
	private final String displayName = "Respawn Players";

	public RespawnCommand(SpongeCrowdControlPlugin plugin) {
		super(plugin);
	}

	@NotNull
	@Override
	public Response.Builder executeImmediately(@NotNull List<@NotNull ServerPlayer> players, @NotNull Request request) {
		// todo this is teleporting players to 0 0 0
		//   maybe RESPAWN_LOCATIONS is broken
		//   need to add some debugging to find out
		sync(() -> {
			for (ServerPlayer player : players) {
				if (player.respawn())
					continue;
				Optional<Map<ResourceKey, RespawnLocation>> optionalData = player.get(Keys.RESPAWN_LOCATIONS);
				if (!optionalData.isPresent()) {
					player.setLocation(getDefaultSpawn());
					continue;
				}
				Map<ResourceKey, RespawnLocation> data = optionalData.get();
				RespawnLocation location = data.get(player.world().key());
				if ((location == null || !location.asLocation().isPresent()) && !data.isEmpty()) {
					for (RespawnLocation curLocation : data.values()) {
						if (curLocation.asLocation().isPresent()) {
							location = curLocation;
							break;
						}
					}
				}
				ServerLocation asLocation;
				if (location == null) {
					asLocation = getDefaultSpawn();
				} else {
					asLocation = location.asLocation().get();
				}
				player.setLocation(plugin.getGame().server().teleportHelper().findSafeLocation(asLocation)
						.orElseGet(asLocation::asHighestLocation));
			}
		});
		return request.buildResponse().type(Response.ResultType.SUCCESS);
	}

	private ServerLocation getDefaultSpawn() {
		// TODO i cannot find a new version of #getSpawnLocation for the life of me and this impl
		//   will not work for the nether (edit: there should be a new util method for nether-
		//   compatible asHighestLocation but i forget where and i sleepy)
		return getDefaultWorld().location(0, 0, 0).asHighestLocation();
	}

	private ServerWorld getDefaultWorld() {
		WorldManager manager = plugin.getGame().server().worldManager();
		Optional<ServerWorld> world = manager.world(ResourceKey.minecraft("world"));
		if (!world.isPresent()) {
			for (ServerWorld iworld : manager.worlds()) {
				if (iworld.worldType().equals(WorldTypes.OVERWORLD.get())) {
					world = Optional.of(iworld);
					break;
				}
			}
		}
		return world.orElseThrow(() -> new IllegalStateException("Couldn't find an overworld world"));
	}
}
