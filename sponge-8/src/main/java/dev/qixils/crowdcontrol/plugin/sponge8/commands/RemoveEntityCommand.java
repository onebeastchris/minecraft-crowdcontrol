package dev.qixils.crowdcontrol.plugin.sponge8.commands;

import dev.qixils.crowdcontrol.common.LimitConfig;
import dev.qixils.crowdcontrol.plugin.sponge8.ImmediateCommand;
import dev.qixils.crowdcontrol.plugin.sponge8.SpongeCrowdControlPlugin;
import dev.qixils.crowdcontrol.socket.Request;
import dev.qixils.crowdcontrol.socket.Response.Builder;
import dev.qixils.crowdcontrol.socket.Response.ResultType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;

import static dev.qixils.crowdcontrol.common.CommandConstants.REMOVE_ENTITY_RADIUS;

@Getter
public class RemoveEntityCommand extends ImmediateCommand {
	protected final EntityType<?> entityType;
	private final String effectName;
	private final String displayName;

	public RemoveEntityCommand(SpongeCrowdControlPlugin plugin, EntityType<?> entityType) {
		super(plugin);
		this.entityType = entityType;
		this.effectName = "remove_entity_" + entityType.key(RegistryTypes.ENTITY_TYPE).value();
		this.displayName = "Remove " + plugin.getTextUtil().asPlain(entityType);
	}

	private boolean removeEntityFrom(ServerPlayer player) {
		Vector3d playerPosition = player.position();
		List<Entity> entities = new ArrayList<>(player.world().nearbyEntities(player.position(), REMOVE_ENTITY_RADIUS));
		entities.removeIf(entity -> !entity.type().equals(entityType));

		if (entities.isEmpty())
			return false;

		if (entities.size() > 1) {
			entities.sort((o1, o2) ->
					(int) (o1.position().distanceSquared(playerPosition)
							- o2.position().distanceSquared(playerPosition)));
		}

		entities.get(0).remove();
		return true;
	}

	@NotNull
	@Override
	public Builder executeImmediately(@NotNull List<@NotNull ServerPlayer> players, @NotNull Request request) {
		Builder result = request.buildResponse().type(ResultType.FAILURE)
				.message("No " + plugin.getTextUtil().asPlain(entityType) + "s found nearby to remove");

		LimitConfig config = getPlugin().getLimitConfig();
		int maxVictims = config.getItemLimit(entityType.key(RegistryTypes.ENTITY_TYPE).value());
		int victims = 0;

		// first pass (hosts)
		for (ServerPlayer player : players) {
			if (!config.hostsBypass() && maxVictims > -1 && victims >= maxVictims)
				break;
			if (!isHost(player))
				continue;
			if (removeEntityFrom(player))
				victims++;
		}

		// second pass (guests)
		for (ServerPlayer player : players) {
			if (maxVictims > -1 && victims >= maxVictims)
				break;
			if (isHost(player))
				continue;
			if (removeEntityFrom(player))
				victims++;
		}

		if (victims > 0)
			result.type(ResultType.SUCCESS).message("SUCCESS");

		return result;
	}
}
