package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.WallState;
import fr.bretzel.minestom.states.state.BooleanState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class FenceGatePlacement extends PlacementRule {

    public FenceGatePlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return false;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {

    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = placementState.instance();
        var blockPosition = placementState.placePosition();
        var facing = blockState.get(Facing.class);
        var selfBlock = new BlockUtils((Instance) instance, blockPosition);

        switch (facing) {
            case WEST, EAST ->
                    blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.south()) || isWall(selfBlock.north()))));
            case SOUTH, NORTH ->
                    blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.east()) || isWall(selfBlock.west()))));
        }
    }

    public boolean isWall(BlockUtils alsBlock) {
        return isWall(alsBlock.state());
    }

    public boolean isWall(BlockState state) {
        return state instanceof WallState;
    }
}
