package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class WeepingVines extends ALSBlockPlacement {

    public WeepingVines() {
        super(Block.WEEPING_VINES);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        return blockFace == Facing.DOWN;
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
        var upBlock = selfBlock.up();

        if (upBlock.block() == Block.WEEPING_VINES) {
            instance.setBlock(upBlock.position(), Block.WEEPING_VINES_PLANT);
        }
    }
}
