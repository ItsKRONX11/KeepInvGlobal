package me.itskronx11.keepinvglobal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class KeepInvCommand implements CommandExecutor {
    private final KeepInvGlobal main;
    public KeepInvCommand(KeepInvGlobal main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (main.getCurrentAnnouncement() != null) main.getCurrentAnnouncement().cancel();

        main.setCurrentAnnouncement(new ActiveAnnouncement(main, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3)));
        return true;
    }
}
