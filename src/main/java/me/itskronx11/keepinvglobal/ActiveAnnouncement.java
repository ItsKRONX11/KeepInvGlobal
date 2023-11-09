package me.itskronx11.keepinvglobal;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActiveAnnouncement {
    private List<ScheduledTask> tasks;
    private final KeepInvGlobal main;
    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();
    public ActiveAnnouncement(KeepInvGlobal main) {
        this.main = main;
        this.config = main.getConfig();
    }
    public void start() {
        final GlobalRegionScheduler scheduler = Bukkit.getServer().getGlobalRegionScheduler();
        
        scheduler.execute(main, () -> Bukkit.getWorlds().forEach(world -> world.setGameRule(GameRule.KEEP_INVENTORY, true)));
        announce(config.getString("three-hours"));

        this.tasks = Arrays.asList(scheduler.runDelayed(main, (task) -> announce(config.getString("two-hours")), convertHours(1)),

        scheduler.runDelayed(main, task -> announce(config.getString("one-hour")), convertHours(2)),

        scheduler.runDelayed(main, task -> announce(config.getString("thirty-minutes")), convertHours(2) + convertMinutes(30)),

        scheduler.runDelayed(main, task -> announce(config.getString("fifteen-minutes")), convertHours(2) + convertMinutes(45)),

        scheduler.runDelayed(main, task -> announce(config.getString("five-minutes")), convertHours(2) + convertMinutes(55)),

        scheduler.runDelayed(main, task -> announce(config.getString("one-minute")), convertHours(2) + convertMinutes(59)),

        scheduler.runDelayed(main, task -> announce(config.getString("five-seconds")), convertHours(2) + convertMinutes(59) + convertSeconds(55)),

        scheduler.runDelayed(main, task -> {
            announce(config.getString("finished"));
            Bukkit.getWorlds().forEach(world -> world.setGameRule(GameRule.KEEP_INVENTORY, false));
        }, convertHours(3)));
    }

    public void announce(Component component) {
        Bukkit.getConsoleSender().sendMessage(component);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(component));
    }
    public void announce(String minimessage) {
        this.announce(mm.deserialize(minimessage));
    }
    public void cancel() {
        if (this.tasks != null)
            this.tasks.forEach(ScheduledTask::cancel);
    }

    public static long convertSeconds(long seconds) {
        return seconds * 20;
    }
    public static long convertMinutes(long minutes) {
        return convertSeconds(TimeUnit.MINUTES.toSeconds(minutes));
    }
    public static long convertHours(long hours) {
        return convertSeconds(TimeUnit.HOURS.toSeconds(hours));
    }
}
