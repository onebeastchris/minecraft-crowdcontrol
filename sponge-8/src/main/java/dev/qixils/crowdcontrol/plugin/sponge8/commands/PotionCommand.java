package dev.qixils.crowdcontrol.plugin.sponge8.commands;

import dev.qixils.crowdcontrol.TimedEffect;
import dev.qixils.crowdcontrol.plugin.sponge8.ImmediateCommand;
import dev.qixils.crowdcontrol.plugin.sponge8.SpongeCrowdControlPlugin;
import dev.qixils.crowdcontrol.socket.Request;
import dev.qixils.crowdcontrol.socket.Response;
import dev.qixils.crowdcontrol.socket.Response.ResultType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.Ticks;

import java.util.Collections;
import java.util.List;

@Getter
public class PotionCommand extends ImmediateCommand {
	private static final int SECONDS = 15;
	private static final int TICKS = 20 * SECONDS;
	private final PotionEffectType potionEffectType;
	private final Ticks duration;
	private final String effectName;
	private final String displayName;

	public PotionCommand(SpongeCrowdControlPlugin plugin, PotionEffectType potionEffectType) {
		super(plugin);
		this.potionEffectType = potionEffectType;
		boolean isMinimal = potionEffectType.isInstant();
		duration = Ticks.of(isMinimal ? 1 : TICKS);
		this.effectName = "potion_" + potionEffectType.key(RegistryTypes.POTION_EFFECT_TYPE).value();
		// TODO for some reason the sponge 7 impl uses title case for potion effect name; test if this is still necessary
		this.displayName = "Apply " + plugin.getTextUtil().asPlain(potionEffectType) + " Potion Effect (" + SECONDS + "s)";
	}

	@NotNull
	@Override
	public Response.Builder executeImmediately(@NotNull List<@NotNull ServerPlayer> players, @NotNull Request request) {
		if (potionEffectType.equals(PotionEffectTypes.JUMP_BOOST.get())
				&& TimedEffect.isActive("disable_jumping", request.getTargets())) {
			return request.buildResponse()
					.type(ResultType.RETRY)
					.message("Cannot apply jump boost while Disable Jump is active");
		}

		PotionEffect.Builder builder = PotionEffect.builder()
				.potionType(potionEffectType)
				.duration(duration);

		for (ServerPlayer player : players) {
			PotionEffect effect = builder.build();
			player.transform(Keys.POTION_EFFECTS, effects -> {
				if (effects == null)
					return Collections.singletonList(effect);

				boolean overridden = false;
				for (int i = 0; i < effects.size(); i++) {
					PotionEffect existingEffect = effects.get(i);
					if (existingEffect.type().equals(potionEffectType)) {
						plugin.getSLF4JLogger().debug("Updating existing effect");
						overridden = true;
						effects.set(i, PotionEffect.builder()
								.from(existingEffect)
								.duration(Ticks.of(Math.max(TICKS, existingEffect.duration().ticks())))
								.amplifier(existingEffect.amplifier() + 1)
								.build());
						break;
					}
				}

				if (!overridden) {
					plugin.getSLF4JLogger().debug("Adding new effect");
					effects.add(effect);
				}

				return effects;
			});
		}

		return request.buildResponse().type(ResultType.SUCCESS);
	}
}
