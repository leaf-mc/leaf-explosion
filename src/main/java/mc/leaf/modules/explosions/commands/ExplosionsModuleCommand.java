package mc.leaf.modules.explosions.commands;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.api.command.annotations.Runnable;
import mc.leaf.modules.explosions.LeafExplosionsModule;

public class ExplosionsModuleCommand extends PluginCommandImpl {

    private final LeafExplosionsModule module;

    public ExplosionsModuleCommand(LeafExplosionsModule module) {

        super(module.getCore());
        this.module = module;
    }

    @Runnable(value = "heal")
    public void heal() {

        this.module.getManager().heal();
    }

}
