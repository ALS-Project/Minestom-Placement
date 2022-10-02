package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.WallState;
import fr.bretzel.minestom.states.state.BooleanState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class FenceGatePlacement extends PlacementRule {

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
        var selfBlock = new BlockUtils(instance, blockPosition);

        switch (facing) {
            case WEST, EAST -> blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.south()) || isWall(selfBlock.north()))));
            case SOUTH, NORTH -> blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.east()) || isWall(selfBlock.west()))));
        }
    }

    public boolean isWall(BlockUtils alsBlock) {
        return isWall(alsBlock.state());
    }

    public boolean isWall(BlockState state) {
        return state instanceof WallState;
    }
}
