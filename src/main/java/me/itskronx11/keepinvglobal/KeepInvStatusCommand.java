package me.itskronx11.keepinvglobal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class KeepInvStatusCommand implements CommandExecutor {
    private final KeepInvGlobal main;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
    public KeepInvStatusCommand(KeepInvGlobal main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ActiveAnnouncement announcement = main.getCurrentAnnouncement();
        commandSender.sendMessage("§eKeepInventory status: " + (announcement == null ? "§coff" : "§aon §e" + dateFormat.format(announcement.getEnd())));
        return true;
    }
}
