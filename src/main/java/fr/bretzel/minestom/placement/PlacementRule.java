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

    public abstract boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl);

    public abstract boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState);

    public abstract void update(Instance instance, Point blockPosition, BlockState blockState);

    public abstract void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl);
}
