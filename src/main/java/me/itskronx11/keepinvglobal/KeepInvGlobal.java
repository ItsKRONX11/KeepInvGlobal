package me.itskronx11.keepinvglobal;

import org.bukkit.plugin.java.JavaPlugin;

public final class KeepInvGlobal extends JavaPlugin {
    private ActiveAnnouncement currentAnnouncement;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("keepinv").setExecutor(new KeepInvCommand(this));
    }
    public ActiveAnnouncement getCurrentAnnouncement() {
        return currentAnnouncement;
    }
    public void setCurrentAnnouncement(ActiveAnnouncement currentAnnouncement) {
        this.currentAnnouncement = currentAnnouncement;
        currentAnnouncement.start();
    }
}
