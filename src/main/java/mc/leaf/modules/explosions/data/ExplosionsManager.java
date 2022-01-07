package mc.leaf.modules.explosions.data;

import mc.leaf.core.async.LeafManager;
import mc.leaf.core.interfaces.ILeafModule;

import java.util.ArrayList;
import java.util.List;

public class ExplosionsManager extends LeafManager<ExplosionMeta> {

    private boolean healEverything = false;

    public ExplosionsManager(ILeafModule module) {

        super(module);
    }

    public void heal() {

        this.healEverything = true;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

        // Restore the world before stopping the module.
        this.getHolder().forEach(ExplosionMeta::placeAll);
        this.getHolder().clear();
    }

    /**
     * When an object implementing interface {@code Runnable} is used to create a thread, starting the thread causes the
     * object's {@code run} method to be called in that separately executing thread.
     * <p>
     * The general contract of the method {@code run} is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        List<ExplosionMeta> replay = new ArrayList<>();

        ExplosionMeta explosion = null;
        while ((explosion = this.getHolder().poll()) != null) {
            if (healEverything) {
                explosion.placeAll();
            } else {
                explosion.placeNext();
            }

            if (explosion.shouldBeKept()) {
                replay.add(explosion);
            }
        }

        this.healEverything = false;
        replay.forEach(this::offer);
    }

}
