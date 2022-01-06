package mc.leaf.modules.explosion.commands;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.modules.explosion.LeafExplosion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ExplosionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        sender.sendMessage(LeafExplosion.PREFIX + " Please enable this module before using any commands.");
        return true;
    }

}
