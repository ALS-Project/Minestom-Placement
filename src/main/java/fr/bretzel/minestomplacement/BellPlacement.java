package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.Attachment;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class BellPlacement extends ALSBlockPlacement {

    public BellPlacement() {
        super(Block.BELL);
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
    public void update(Instance instance, Point blockPosition, BlockState blockState) {
        var facing = blockState.get(Facing.class);
        var attachment = blockState.get(Attachment.class);

        var selfBlock = new ALSBlock(instance, blockPosition);
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
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {
        if (blockFace == Facing.DOWN) {
            blockState.set(Attachment.CEILING);
        } else if (blockFace == Facing.UP) {
            blockState.set(Attachment.FLOOR);
        } else {
            var facing = blockState.get(Facing.class);
            var selfBlock = new ALSBlock(instance, blockPosition);
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
