package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.Face;
import fr.bretzel.minestomstates.state.Facing;
import fr.bretzel.minestomplacement.ALSBlockPlacement;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class GrindstonePlacement extends ALSBlockPlacement {

    public GrindstonePlacement() {
        super(Block.GRINDSTONE);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        return true;
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return false;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {

    }

    @Override
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {
        if (blockFace == Facing.UP)
            blockState.set(Face.FLOOR);
        else if (blockFace == Facing.DOWN)
            blockState.set(Face.CEILING);
        else blockState.set(Face.WALL);
    }
}
