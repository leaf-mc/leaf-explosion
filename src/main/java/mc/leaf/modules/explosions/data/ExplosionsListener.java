package mc.leaf.modules.explosions.data;

import mc.leaf.core.events.LeafListener;
import mc.leaf.modules.explosions.LeafExplosionsModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.InventoryHolder;

public class ExplosionsListener extends LeafListener {

    private final LeafExplosionsModule module;

    public ExplosionsListener(LeafExplosionsModule module) {

        this.module = module;
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {

        if (event.getEntity() instanceof TNTPrimed) {
            return;
        }

        this.module.getManager().offer(new ExplosionMeta(event.blockList()));

        // Replace everything by air and empty containers (InventoryHolder)
        event.blockList().stream()
                .filter(block -> !block.getType().equals(Material.TNT))
                .map(Block::getState)
                .forEach(this::handleExplodedBlockState);

        // Drop nothing.
        event.setYield(0);
    }

    private void handleExplodedBlockState(BlockState bs) {
        if (bs instanceof InventoryHolder) {
            // If it's something which hold an inventory, clear it to avoid drops (which can lead to items duplication)
            ((InventoryHolder) bs).getInventory().clear();
        }

        // Removing the block here. Handling it to avoid physics to be applied to block around the explosion.
        bs.setType(Material.AIR);
        bs.update(true, false);
    }
}
