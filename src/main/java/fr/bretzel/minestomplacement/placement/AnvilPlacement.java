package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomplacement.ALSBlockPlacement;
import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class AnvilPlacement extends ALSBlockPlacement {

    public AnvilPlacement(Block block) {
        super(block);
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
        blockState.set(blockState.get(Facing.class).rotateY());
    }
}
