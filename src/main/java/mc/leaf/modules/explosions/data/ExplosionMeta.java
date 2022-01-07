package mc.leaf.modules.explosions.data;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ExplosionMeta {

    private final BlockingDeque<BlockState> states = new LinkedBlockingDeque<>();
    private       int                       nextRestore;

    public ExplosionMeta(List<Block> blocks) {

        List<Material> blacklist = Arrays.asList(Material.AIR, Material.TNT);

        blocks.stream()
                .filter(block -> !blacklist.contains(block.getType()))
                .map(Block::getState)
                .forEach(this.states::add);

        this.nextRestore = Bukkit.getCurrentTick() + 200; // 200 ticks = 10 seconds
    }

    /**
     * Restore the next BlockState in the queue.
     */
    public void placeNext() {

        this.placeNext(false);
    }

    /**
     * Restore the next BlockState in the queue.
     *
     * @param force
     *         If set to true, the delay will be ignored.
     */
    public void placeNext(boolean force) {

        if (this.nextRestore > Bukkit.getCurrentTick() && !force) {
            return;
        }

        // Delaying the restoration of the next BlockState of 0.1s.
        this.nextRestore = Bukkit.getCurrentTick() + 2;

        // Get the next BlockState to restore.
        BlockState state = this.states.poll();

        if (state != null) {

            Chunk chunk = state.getChunk();

            // Making sure that the chunk is loaded before changing it.
            if (!chunk.isLoaded()) {
                chunk.load();
            }

            // Restore the BlockState without physic to avoid block to fall (sand, gravel) and water to flow and play a sound.
            state.update(true, false);
            state.getWorld()
                    .playSound(state.getLocation(), Sound.BLOCK_BAMBOO_SAPLING_PLACE, SoundCategory.BLOCKS, 1, 2);


            // Let's save the world... errrm, I mean.. the entities !
            Collection<Entity> entities = state.getWorld().getNearbyEntities(state.getLocation(), 3, 3, 3);

            for (Entity entity : entities) {
                boolean shouldBeTeleported = false;

                Location entityFeetLocation = entity.getLocation().toBlockLocation().toCenterLocation().clone();
                Location entityBodyLocation = entity.getLocation().toBlockLocation().toCenterLocation().clone()
                        .add(0, 1, 0);

                Block feetBlock = state.getWorld().getBlockAt(entityFeetLocation);
                Block bodyBlock = state.getWorld().getBlockAt(entityBodyLocation);

                while (!feetBlock.isPassable() || !bodyBlock.isPassable()) { // While suffocation
                    // Do not suffocate
                    shouldBeTeleported = true;

                    entityFeetLocation = entityFeetLocation.add(0, 1, 0);
                    entityBodyLocation = entityBodyLocation.add(0, 1, 0);

                    feetBlock = state.getWorld().getBlockAt(entityFeetLocation);
                    bodyBlock = state.getWorld().getBlockAt(entityBodyLocation);
                }

                if (shouldBeTeleported) {
                    // Save this entity from suffocation.
                    entity.teleport(entityFeetLocation);
                }
            }

            // This is just for memory consumption purpose
            chunk.unload();
        }
    }

    /**
     * Restore all blocks in this instance.
     */
    public void placeAll() {

        while (!this.states.isEmpty()) {
            this.placeNext(true);
        }
    }

    /**
     * Check if this instance should be kept as there is still more BlockStates to restore.
     *
     * @return True if there is still BlockStates to restore.
     */
    public boolean shouldBeKept() {

        return this.states.size() > 0;
    }

}
