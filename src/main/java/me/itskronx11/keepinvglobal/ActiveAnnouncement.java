package me.itskronx11.keepinvglobal;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;

public class ActiveAnnouncement {
    private final KeepInvGlobal main;
    private final FileConfiguration config;
    private final long time;
    private long started;
    private final MiniMessage mm = MiniMessage.miniMessage();
    public ActiveAnnouncement(KeepInvGlobal main, long time) {
        this.main = main;
        this.time = time;
        this.config = main.getConfig();
    }
    public void start() {
        this.started = System.currentTimeMillis();
        if (time >= TimeUnit.HOURS.toMillis(3)) {
            Bukkit.getGlobalRegionScheduler().execute(main, () -> announce(config.getString("three-hours")));
            main.setKeepInventory(true);
        }
        if (time >= TimeUnit.HOURS.toMillis(2))
            schedule("two-hours", time - TimeUnit.HOURS.toMillis(2));
        if (time >= TimeUnit.HOURS.toMillis(1))
            schedule("one-hour", time - TimeUnit.HOURS.toMillis(1));
        if (time >= TimeUnit.MINUTES.toMillis(30))
            schedule("thirty-minutes", time - TimeUnit.MINUTES.toMillis(30));
        if (time >= TimeUnit.MINUTES.toMillis(15))
            schedule("fifteen-minutes", time - TimeUnit.MINUTES.toMillis(15));
        if (time >= TimeUnit.MINUTES.toMillis(5))
            schedule("five-minutes", time - TimeUnit.MINUTES.toMillis(5));
        if (time >= TimeUnit.MINUTES.toMillis(1))
            schedule("one-minute", time - TimeUnit.MINUTES.toMillis(1));
        if (time >= TimeUnit.SECONDS.toMillis(5))
            schedule("five-seconds", time - TimeUnit.SECONDS.toMillis(5));

        schedule(time, () -> {
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
        this.announce(mm.deserialize(config.getString("prefix") + minimessage));
    }
    public void cancel() {
        main.setKeepInventory(false);
        Bukkit.getGlobalRegionScheduler().cancelTasks(main);
    }
    public void schedule(String configPath, long time) {
        this.schedule(time, () -> announce(config.getString(configPath)));
    }
    public void schedule(long delay, Runnable runnable) {
        GlobalRegionScheduler scheduler = Bukkit.getGlobalRegionScheduler();

        long delaySeconds = convertSeconds(TimeUnit.MILLISECONDS.toSeconds(delay));

        scheduler.runDelayed(main, task -> runnable.run(), delaySeconds);
    }

    public static long convertSeconds(long seconds) {
        return seconds * 20;
    }
    public long getTime() {
        return this.time;
    }
    public long getStarted() {
        return this.started;
    }
}