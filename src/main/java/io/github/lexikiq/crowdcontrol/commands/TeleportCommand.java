package io.github.lexikiq.crowdcontrol.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import io.github.lexikiq.crowdcontrol.ChatCommand;
import io.github.lexikiq.crowdcontrol.CrowdControl;
import io.github.lexikiq.crowdcontrol.utils.RandomUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeleportCommand extends ChatCommand {
    public TeleportCommand(CrowdControl plugin) {
        super(plugin);
    }

    @Override
    public int getCooldownSeconds() {
        return (int) (60*7.5);
    }

    @Override
    public @NotNull String getCommand() {
        return "tp";
    }

    @Override
    public boolean execute(ChannelMessageEvent event, List<Player> players, String... args) {
        for (Player player : players) {
            Location destination = RandomUtil.randomNearbyBlock(player.getLocation(), 3, 15, true, Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
            if (destination == null) {
                continue;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(destination);
                    destination.setY(Math.ceil(destination.getY()));
                    Particle.PORTAL.builder().location(destination).offset(.5d, 1d, .5d).source(player).receivers(75).count(100).spawn();
                    player.getWorld().playSound(destination, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1.0f, 1.0f);
                }
            }.runTask(plugin);
        }
        return true;
    }
}
