package mc.leaf.modules.explosion.commands;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.api.command.annotations.Runnable;
import mc.leaf.modules.explosion.LeafExplosionModule;

public class ExplosionModuleCommand extends PluginCommandImpl {

    private final LeafExplosionModule module;

    public ExplosionModuleCommand(LeafExplosionModule module) {

        super(module.getCore());
        this.module = module;
    }

    @Runnable(value = "heal")
    public void heal() {

        this.module.getManager().heal();
    }

}
