package fr.bretzel.minestom.placement;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public abstract class PlacementRule {
    private final Block block;

    public PlacementRule(Block block) {
        this.block = block;
    }

    public Block block() {
        return block;
    }

    /**
     * @param instance      the instance when the block is trying to be placed
     * @param blockFace     the right-clicked face for the player
     * @param blockPosition the block position of the block
     * @param blockState    a blocksate integration
     * @param pl            the player
     * @return true if the block can be placed
     */
    public abstract boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl);

    /**
     * @param instance      the instance when the block is trying to be placed
     * @param blockPosition the block position of the block
     * @param blockState    a blocksate integration
     * @return true if the block can be updated
     */
    public abstract boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState);

    /**
     * @param instance      the instance when the block is trying to be placed
     * @param blockPosition the block position of the block
     * @param blockState    a blocksate integration
     */
    public abstract void update(Instance instance, Point blockPosition, BlockState blockState);

    /**
     * @param instance      the instance when the block is trying to be placed
     * @param blockFace     the right-clicked face for the player
     * @param blockPosition the block position of the block
     * @param blockState    a blocksate integration
     * @param pl            the player
     */
    public abstract void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl);
}
