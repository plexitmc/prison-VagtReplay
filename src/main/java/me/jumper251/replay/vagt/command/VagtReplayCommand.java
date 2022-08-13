package me.jumper251.replay.vagt.command;

import dk.plexhost.core.utils.ColorUtils;
import me.jumper251.replay.vagt.gui.VagtReplayMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VagtReplayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ColorUtils.getColored("&cThis command is player-only."));
        } else if(!sender.hasPermission("vagt.replay")){
            sender.sendMessage(ColorUtils.getColored("&cDu har ikke adgang til denne kommando."));
        } else {
            Player player = (Player) sender;
            VagtReplayMenu.show(player, false);
        }
        return true;
    }
}
