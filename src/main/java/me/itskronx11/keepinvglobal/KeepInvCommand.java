package me.itskronx11.keepinvglobal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class KeepInvCommand implements CommandExecutor {
    private final KeepInvGlobal main;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
    public KeepInvCommand(KeepInvGlobal main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ActiveAnnouncement announcement = main.getCurrentAnnouncement();
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("off")) {
                main.setCurrentAnnouncement(null);
                sender.sendMessage("§cKeepInventory disabled.");
                return true;
            } else if (args[0].equalsIgnoreCase("status")) {
                sender.sendMessage("§eKeepInventory status: " + (announcement == null ? "§coff" : "§aon §e" + dateFormat.format(announcement.getStarted() + announcement.getTime())));
                return true;
            }
        }
        if (announcement != null) announcement.cancel();

        main.setCurrentAnnouncement(new ActiveAnnouncement(main, TimeUnit.HOURS.toMillis(3)));
        return true;
    }
}
