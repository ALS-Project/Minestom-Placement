package fr.bretzel.minestomplacement;


import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.BlockStateManager;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.block.blockstate.state.Half;
import fr.als.core.block.blockstate.state.StairsShape;
import fr.als.core.raytrace.RayBlockResult;
import fr.als.core.raytrace.RayTrace;
import fr.als.core.raytrace.RayTraceContext;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class StairsPlacement extends ALSBlockPlacement {

    public StairsPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        return true;
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return true;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState states) {
        Facing facing = states.get(Facing.class);

        ALSBlock selfBlock = new ALSBlock(instance, blockPosition);
        ALSBlock facingBlock = selfBlock.relative(facing);
        ALSBlock oppositeBlock = selfBlock.relative(facing.opposite());

        BlockState facingStates = facingBlock.state();

        if (isStairs(facingBlock) && facingStates.get(Half.class) == states.get(Half.class)) {
            Facing facingOfFacing = facingStates.get(Facing.class);
            if (facingOfFacing.getAxis() != facing.getAxis() && isDifferentStairs(states, instance, selfBlock, facingOfFacing)) {
                if (facingOfFacing == facing.rotateYCCW())
                    states.set(StairsShape.OUTER_LEFT);
                else states.set(StairsShape.OUTER_RIGHT);
                return;
            }
        }

        BlockState oppositeStates = oppositeBlock.state();
        if (isStairs(oppositeStates) && states.get(Half.class) == oppositeStates.get(Half.class)) {
            Facing oppositeFacing = oppositeStates.get(Facing.class);
            if (oppositeFacing.getAxis() != facing.getAxis() && isDifferentStairs(states, instance, oppositeBlock, oppositeFacing)) {
                if (oppositeFacing == facing.rotateYCCW()) {
                    states.set(StairsShape.INNER_LEFT);
                } else states.set(StairsShape.INNER_RIGHT);
                return;
            }
        }

        states.set(StairsShape.STRAIGHT);
    }

    public static boolean isStairs(Block block) {
        return block.name().toLowerCase().trim().endsWith("stairs");
    }

    private boolean isDifferentStairs(BlockState states, Instance instance, ALSBlock pos, Facing facing) {
        BlockState blockState = BlockStateManager.get(instance.getBlock(pos.relative(facing.opposite()).position()));
        return !isStairs(blockState) ||
                blockState.get(Facing.class) != states.get(Facing.class) ||
                blockState.get(Half.class) != states.get(Half.class);
    }

    private boolean isStairs(ALSBlock alsBlock) {
        return isStairs(alsBlock.block());
    }

    private boolean isStairs(BlockState blockState) {
        return isStairs(blockState.block());
    }

    @Override
    public void place(Instance instance, BlockState states, Facing blockFace, Point blockPosition, Player player) {
        if (blockFace == Facing.DOWN || blockFace == Facing.UP) {
            if (blockFace == Facing.DOWN)
                states.set(Half.TOP);
            else states.set(Half.BOTTOM);
        } else {

            RayBlockResult result = RayTrace.rayTraceBlock(new RayTraceContext(player, 5));
            if (result != null && result.getHit() != null && !result.getHitBlock().isAir()) {
                Point hit = result.getHit();
                double y = hit.y() - hit.blockY();

                states.set((y >= 0.5) ? Half.TOP : Half.BOTTOM);
            }

            states.set(states.get(Facing.class).opposite());
        }
    }
}
