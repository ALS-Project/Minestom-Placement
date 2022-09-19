package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.Facing;
import fr.als.core.utils.ALSBlock;
import fr.bretzel.minestomplacement.ALSBlockPlacement;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class TwistingVines extends ALSBlockPlacement {

    public TwistingVines() {
        super(Block.TWISTING_VINES);
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
        var selfBlock = new ALSBlock(instance, blockPosition);
        var downBlock = selfBlock.down();

        if (downBlock.block() == Block.TWISTING_VINES) {
            instance.setBlock(downBlock.position(), Block.TWISTING_VINES_PLANT);
        }
    }
}
