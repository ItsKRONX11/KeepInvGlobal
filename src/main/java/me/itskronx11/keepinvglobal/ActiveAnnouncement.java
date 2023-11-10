package me.itskronx11.keepinvglobal;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActiveAnnouncement {
    private final List<ScheduledTask> tasks = new ArrayList<>();
    private final KeepInvGlobal main;
    private final FileConfiguration config;
    private final long end;
    private final MiniMessage mm = MiniMessage.miniMessage();
    public ActiveAnnouncement(KeepInvGlobal main, long end) {
        this.main = main;
        this.end = end;
        this.config = main.getConfig();
    }
    /*
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
            main.setCurrentAnnouncement(null);
        }, convertHours(3)));
    }
     */
    public void start0() {
        long remaining = end - System.currentTimeMillis();

        if (remaining >= TimeUnit.HOURS.toMillis(3))
            schedule("three-hours", end -TimeUnit.HOURS.toMillis(3));
        if (remaining >= TimeUnit.HOURS.toMillis(2))
            schedule("two-hours", end -TimeUnit.HOURS.toMillis(2));
        if (remaining >= TimeUnit.HOURS.toMillis(1))
            schedule("one-hour", end -TimeUnit.HOURS.toMillis(1));
        if (remaining >= TimeUnit.MINUTES.toMillis(30))
            schedule("thirty-minutes", end -TimeUnit.MINUTES.toMillis(30));
        if (remaining >= TimeUnit.MINUTES.toMillis(15))
            schedule("fifteen-minutes", end -TimeUnit.MINUTES.toMillis(15));
        if (remaining >= TimeUnit.MINUTES.toMillis(5))
            schedule("five-minutes", end -TimeUnit.MINUTES.toMillis(5));
        if (remaining >= TimeUnit.MINUTES.toMillis(1))
            schedule("one-minute", end - TimeUnit.MINUTES.toMillis(1));
        if (remaining >= TimeUnit.SECONDS.toMillis(5))
            schedule("five-seconds", end - TimeUnit.SECONDS.toMillis(5));

        schedule(end, () -> {
            announce(config.getString("finished"));
            Bukkit.getWorlds().forEach(world -> world.setGameRule(GameRule.KEEP_INVENTORY, false));
            main.setCurrentAnnouncement(null);
        });



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
    public void schedule(String configPath, long timeMillis) {
        this.schedule(timeMillis, () -> announce(config.getString(configPath)));
    }
    public void schedule(long timeMillis, Runnable runnable) {
        this.tasks.add(Bukkit.getGlobalRegionScheduler().runDelayed(main, task -> runnable.run(), convertSeconds(TimeUnit.MILLISECONDS.toSeconds(timeMillis - System.currentTimeMillis()))));
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
    public static long ticksFromNow(long time) {
        return convertSeconds(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time));
    }
    public long getEnd() {
        return this.end;
    }
}
