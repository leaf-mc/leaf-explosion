package mc.leaf.modules.explosions;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.impl.LeafModule;
import mc.leaf.modules.explosions.commands.ExplosionsModuleCommand;
import mc.leaf.modules.explosions.data.ExplosionsListener;
import mc.leaf.modules.explosions.data.ExplosionsManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LeafExplosionsModule extends LeafModule {

    private ExplosionsManager manager;

    public LeafExplosionsModule(LeafExplosions plugin, ILeafCore core) {

        super(core, plugin);
        this.getCore().registerModule(this);
    }

    @Override
    public void onEnable() {

        this.manager = new ExplosionsManager(this);
        this.manager.start();
    }

    @Override
    public List<Listener> getListeners() {

        return List.of(new ExplosionsListener(this));
    }

    @Override
    public Map<String, PluginCommandImpl> getCommands() {

        return new HashMap<>() {{
            this.put("explosions", new ExplosionsModuleCommand(LeafExplosionsModule.this));
        }};
    }

    @Override
    public void onDisable() {

        this.manager.stop();
        this.manager = null;
    }

    @Override
    public String getName() {

        return "Explosion";
    }

    public ExplosionsManager getManager() {

        return this.manager;
    }

}
