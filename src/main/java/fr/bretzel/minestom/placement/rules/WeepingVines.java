package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class WeepingVines extends PlacementRule {

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
        var selfBlock = new BlockUtils(instance, blockPosition);
        var upBlock = selfBlock.up();

        if (upBlock.block() == Block.WEEPING_VINES) {
            instance.setBlock(upBlock.position(), Block.WEEPING_VINES_PLANT);
        }
    }
}
