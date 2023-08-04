package fr.bretzel.minestom.placement.rules;


import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.BlockStateManager;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.Half;
import fr.bretzel.minestom.states.state.StairsShape;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class StairsPlacement extends PlacementRule {

    public StairsPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return true;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        var facing = blockState.get(Facing.class);
        var instance = (Instance) updateState.instance();
        var blockPosition = updateState.blockPosition();

        var selfBlock = new BlockUtils(instance, blockPosition);
        var facingBlock = selfBlock.relative(facing);
        var oppositeBlock = selfBlock.relative(facing.opposite());

        var facingStates = facingBlock.state();

        if (isStairs(facingBlock) && facingStates.get(Half.class) == blockState.get(Half.class)) {
            var facingOfFacing = facingStates.get(Facing.class);
            if (facingOfFacing.getAxis() != facing.getAxis() && isDifferentStairs(blockState, instance, selfBlock, facingOfFacing)) {
                if (facingOfFacing == facing.rotateYCCW())
                    blockState.set(StairsShape.OUTER_LEFT);
                else blockState.set(StairsShape.OUTER_RIGHT);
                return;
            }
        }

        var oppositeStates = oppositeBlock.state();

        if (isStairs(oppositeStates) && blockState.get(Half.class) == oppositeStates.get(Half.class)) {
            var oppositeFacing = oppositeStates.get(Facing.class);
            if (oppositeFacing.getAxis() != facing.getAxis() && isDifferentStairs(blockState, instance, oppositeBlock, oppositeFacing)) {
                if (oppositeFacing == facing.rotateYCCW()) {
                    blockState.set(StairsShape.INNER_LEFT);
                } else blockState.set(StairsShape.INNER_RIGHT);
                return;
            }
        }

        blockState.set(StairsShape.STRAIGHT);
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var blockFace = placementState.blockFace();

        if (blockFace == BlockFace.BOTTOM || blockFace == BlockFace.TOP) {
            if (blockFace == BlockFace.BOTTOM)
                blockState.set(Half.TOP);
            else blockState.set(Half.BOTTOM);
        } else {
            var hit = placementState.cursorPosition();

            if (hit != null) {
                double y = hit.y() - hit.blockY();

                blockState.set((y >= 0.5) ? Half.TOP : Half.BOTTOM);
            }

            blockState.set(blockState.get(Facing.class).opposite());
        }
    }

    public static boolean isStairs(Block block) {
        return block.name().toLowerCase().trim().endsWith("stairs");
    }

    private boolean isDifferentStairs(BlockState states, Instance instance, BlockUtils pos, Facing facing) {
        BlockState blockState = BlockStateManager.get(instance.getBlock(pos.relative(facing.opposite()).position()));
        return !isStairs(blockState) ||
                blockState.get(Facing.class) != states.get(Facing.class) ||
                blockState.get(Half.class) != states.get(Half.class);
    }

    private boolean isStairs(BlockUtils alsBlock) {
        return isStairs(alsBlock.block());
    }

    private boolean isStairs(BlockState blockState) {
        return isStairs(blockState.block());
    }
}
