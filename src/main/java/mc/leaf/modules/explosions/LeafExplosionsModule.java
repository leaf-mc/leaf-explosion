package mc.leaf.modules.explosions;

import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.explosions.commands.ExplosionsModuleCommand;
import mc.leaf.modules.explosions.data.ExplosionsListener;
import mc.leaf.modules.explosions.data.ExplosionsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class LeafExplosionsModule implements ILeafModule {

    private final LeafExplosions plugin;
    private final ILeafCore         core;
    private       ExplosionsManager manager;
    private       boolean           enabled = false;

    public LeafExplosionsModule(LeafExplosions plugin, ILeafCore core) {

        this.plugin = plugin;
        this.core   = core;

        this.core.registerModule(this);
    }

    @Override
    public void onEnable() {

        this.manager = new ExplosionsManager(this);
        this.manager.start();

        this.core.getEventBridge().register(this, new ExplosionsListener(this));

        Optional.ofNullable(Bukkit.getPluginCommand("explosion"))
                .ifPresent(pluginCommand -> pluginCommand.setExecutor(new ExplosionsModuleCommand(this)));

        this.enabled = true;
    }

    @Override
    public void onDisable() {

        Optional.ofNullable(Bukkit.getPluginCommand("explosions"))
                .ifPresent(pluginCommand -> pluginCommand.setExecutor(null));
        this.manager.stop();
        this.manager = null;
        this.enabled = false;
    }

    @Override
    public ILeafCore getCore() {

        return this.core;
    }

    @Override
    public String getName() {

        return "Explosion";
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }

    @Override
    public JavaPlugin getPlugin() {

        return this.plugin;
    }

    public ExplosionsManager getManager() {

        return manager;
    }

}
