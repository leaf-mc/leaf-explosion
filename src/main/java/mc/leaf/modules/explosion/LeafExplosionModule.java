package mc.leaf.modules.explosion;

import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.explosion.commands.ExplosionModuleCommand;
import mc.leaf.modules.explosion.data.ExplosionListener;
import mc.leaf.modules.explosion.data.ExplosionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class LeafExplosionModule implements ILeafModule {

    private final LeafExplosion    plugin;
    private final ILeafCore        core;
    private       ExplosionManager manager;
    private       boolean          enabled = false;

    public LeafExplosionModule(LeafExplosion plugin, ILeafCore core) {

        this.plugin = plugin;
        this.core   = core;

        this.core.registerModule(this);
    }

    @Override
    public void onEnable() {

        this.manager = new ExplosionManager(this);
        this.manager.start();

        this.core.getEventBridge().register(this, new ExplosionListener(this));

        Optional.ofNullable(Bukkit.getPluginCommand("explosion"))
                .ifPresent(pluginCommand -> pluginCommand.setExecutor(new ExplosionModuleCommand(this)));

        this.enabled = true;
    }

    @Override
    public void onDisable() {

        this.manager.stop();
        this.manager = null;
        this.plugin.registerDisabledCommand();
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

    public ExplosionManager getManager() {

        return manager;
    }

}
