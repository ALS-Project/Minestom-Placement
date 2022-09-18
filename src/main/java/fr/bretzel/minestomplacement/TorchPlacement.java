package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class TorchPlacement extends ALSBlockPlacement {

    private final Block toPlace;

    public TorchPlacement(Block block, Block toPlace) {
        super(block);
        this.toPlace = toPlace;
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
        if (blockFace == Facing.DOWN || blockFace == Facing.UP)
            return;

        blockState.withBlock(toPlace);
        blockState.set(blockFace);
    }
}
