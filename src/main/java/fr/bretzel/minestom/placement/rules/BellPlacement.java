package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Attachment;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class BellPlacement extends PlacementRule {

    public BellPlacement() {
        super(Block.BELL);
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
        var attachment = blockState.get(Attachment.class);
        var instance = (Instance) updateState.instance();
        var blockPosition = updateState.blockPosition();

        var selfBlock = new BlockUtils(instance, blockPosition);
        var facingBlock = selfBlock.relative(facing);
        var oppositeBlock = selfBlock.relative(facing.opposite());

        if (attachment == Attachment.SINGLE_WALL) {
            if (!facingBlock.block().isSolid()) {
                instance.setBlock(selfBlock.position(), Block.AIR);
            } else if (oppositeBlock.block().isSolid())
                blockState.set(Attachment.DOUBLE_WALL);
        } else if (attachment == Attachment.DOUBLE_WALL) {
            if (!facingBlock.block().isSolid() && oppositeBlock.block().isSolid()) {
                blockState.set(Attachment.SINGLE_WALL);
                blockState.set(facing.opposite());
            } else if (!oppositeBlock.block().isSolid() && facingBlock.block().isSolid()) {
                blockState.set(Attachment.SINGLE_WALL);
            } else if (!oppositeBlock.block().isSolid() && !facingBlock.block().isSolid()) {
                instance.setBlock(selfBlock.position(), Block.AIR);
            }
        }
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = (Instance) placementState.instance();
        var blockFace = placementState.blockFace();
        var blockPosition = placementState.placePosition();

        if (blockFace == BlockFace.BOTTOM) {
            blockState.set(Attachment.CEILING);
        } else if (blockFace == BlockFace.TOP) {
            blockState.set(Attachment.FLOOR);
        } else {
            var facing = blockState.get(Facing.class);
            var selfBlock = new BlockUtils(instance, blockPosition);
            var facingBlock = selfBlock.relative(facing);
            var oppositeBlock = selfBlock.relative(facing.opposite());

            if (facingBlock.block().isSolid() && oppositeBlock.block().isSolid())
                blockState.set(Attachment.DOUBLE_WALL);
            else if (facingBlock.block().isSolid() && !oppositeBlock.block().isSolid())
                blockState.set(Attachment.SINGLE_WALL);
            else if (oppositeBlock.block().isSolid() && !facingBlock.block().isSolid()) {
                blockState.set(Attachment.SINGLE_WALL);
                blockState.set(facing.opposite());
            }
        }
    }
}
