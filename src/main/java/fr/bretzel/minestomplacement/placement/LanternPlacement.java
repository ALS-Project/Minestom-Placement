package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.BooleanState;
import fr.bretzel.minestomstates.state.Facing;
import fr.als.core.utils.ALSBlock;
import fr.bretzel.minestomplacement.ALSBlockPlacement;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class LanternPlacement extends ALSBlockPlacement {

    private final BooleanState HANGING = BooleanState.Of("hanging", false);

    public LanternPlacement(Block block) {
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
    public void update(Instance instance, Point blockPosition, BlockState blockState) {
        ALSBlock block = new ALSBlock(instance, blockPosition);

        boolean hanging = blockState.get(HANGING);
        boolean blockUp = block.up().block().isSolid();

        if (!hanging && blockUp)
            blockState.set(HANGING.setValue(true));
        else if (hanging && !blockUp)
            blockState.set(HANGING.setValue(false));
    }

    @Override
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {

    }
}
