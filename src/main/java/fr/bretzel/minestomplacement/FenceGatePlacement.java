package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.WallState;
import fr.als.core.block.blockstate.state.BooleanState;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class FenceGatePlacement extends ALSBlockPlacement {

    public FenceGatePlacement(Block block) {
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
        var facing = blockState.get(Facing.class);
        var selfBlock = new ALSBlock(instance, blockPosition);

        switch (facing) {
            case WEST, EAST -> blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.south()) || isWall(selfBlock.north()))));
            case SOUTH, NORTH -> blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.east()) || isWall(selfBlock.west()))));
        }
    }

    public boolean isWall(ALSBlock alsBlock) {
        return isWall(alsBlock.state());
    }

    public boolean isWall(BlockState state) {
        return state instanceof WallState;
    }
}
