package dev.qixils.crowdcontrol.plugin.fabric.commands;

import dev.qixils.crowdcontrol.plugin.fabric.FabricCrowdControlPlugin;
import dev.qixils.crowdcontrol.plugin.fabric.ImmediateCommand;
import dev.qixils.crowdcontrol.socket.Request;
import dev.qixils.crowdcontrol.socket.Response;
import dev.qixils.crowdcontrol.socket.Response.ResultType;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static dev.qixils.crowdcontrol.common.command.CommandConstants.KEEP_INVENTORY_MESSAGE;
import static dev.qixils.crowdcontrol.common.command.CommandConstants.LOSE_INVENTORY_MESSAGE;
import static dev.qixils.crowdcontrol.common.util.sound.Sounds.KEEP_INVENTORY_ALERT;
import static dev.qixils.crowdcontrol.common.util.sound.Sounds.LOSE_INVENTORY_ALERT;

@Getter
public class KeepInventoryCommand extends ImmediateCommand {
	private static final Set<UUID> keepingInventory = Collections.synchronizedSet(new HashSet<>(1));
	private static boolean globalKeepInventory = false;
	private final boolean enable;
	private final String effectName;

	public KeepInventoryCommand(FabricCrowdControlPlugin plugin, boolean enable) {
		super(plugin);
		this.enable = enable;
		this.effectName = "keep_inventory_" + (enable ? "on" : "off");
	}

	public static boolean isKeepingInventory(UUID player) {
		return globalKeepInventory || keepingInventory.contains(player);
	}

	public static boolean isKeepingInventory(Entity player) {
		return isKeepingInventory(player.getUUID());
	}

	private void alert(List<ServerPlayer> players) {
		Audience audience = plugin.playerMapper().asAudience(players);
		audience.sendActionBar(enable ? KEEP_INVENTORY_MESSAGE : LOSE_INVENTORY_MESSAGE);
		audience.playSound((enable ? KEEP_INVENTORY_ALERT : LOSE_INVENTORY_ALERT).get(), Sound.Emitter.self());
	}

	@NotNull
	@Override
	public Response.Builder executeImmediately(@NotNull List<@NotNull ServerPlayer> players, @NotNull Request request) {
		Response.Builder resp = request.buildResponse();

		// TODO: hide/show effects

		if (isGlobalCommandUsable(players, request)) {
			if (globalKeepInventory == enable) {
				return resp.type(ResultType.FAILURE).message("Keep Inventory is already " + (enable ? "enabled" : "disabled"));
			}
			globalKeepInventory = enable;
			alert(players);
			return resp.type(ResultType.SUCCESS);
		}

		List<UUID> uuids = new ArrayList<>(players.size());
		for (ServerPlayer player : players)
			uuids.add(player.getUUID());

		if (enable) {
			if (keepingInventory.addAll(uuids)) {
				alert(players);
				return resp.type(ResultType.SUCCESS);
			} else
				return resp.type(ResultType.FAILURE).message("Streamer(s) already have Keep Inventory enabled");
		} else {
			if (keepingInventory.removeAll(uuids)) {
				alert(players);
				return resp.type(ResultType.SUCCESS);
			} else
				return resp.type(ResultType.FAILURE).message("Streamer(s) already have Keep Inventory disabled");
		}
	}

	// management of this command is handled by mixins
}
