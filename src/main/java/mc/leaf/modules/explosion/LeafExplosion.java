package mc.leaf.modules.explosion;

import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.modules.explosion.commands.ExplosionCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class LeafExplosion extends JavaPlugin {

    public static final String PREFIX = "§l[§aLeaf§bExplosion§r§l]§r";

    @Override
    public void onEnable() {

        Plugin plugin = Bukkit.getPluginManager().getPlugin("LeafCore");
        if (plugin instanceof ILeafCore core) {
            new LeafExplosionModule(this, core);
        } else {
            this.getLogger().severe("Unable to find LeafCore instance.");
        }
    }

    public void registerDisabledCommand() {

        Optional.ofNullable(Bukkit.getPluginCommand("explosion"))
                .ifPresent(pluginCommand -> pluginCommand.setExecutor(new ExplosionCommand()));
    }

}
