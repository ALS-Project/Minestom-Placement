package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.Facing;
import fr.bretzel.minestomstates.state.IntegerState;
import fr.als.core.utils.ALSBlock;
import fr.bretzel.minestomplacement.ALSBlockPlacement;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class TurtleEggPlacement extends ALSBlockPlacement {

    private static final IntegerState EGGS = IntegerState.Of("eggs");

    public TurtleEggPlacement() {
        super(Block.TURTLE_EGG);
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
        var oppositeBlock = selfBlock.relative(blockFace.opposite());

        if (oppositeBlock.block() == block()) {
            var oppositeState = oppositeBlock.state();
            var eggs = oppositeState.get(EGGS);

            if (eggs < 4) {
                blockState.withBlock(Block.AIR);
                eggs = eggs + 1;
                oppositeState.set(EGGS, eggs);
                instance.setBlock(oppositeBlock.position(), oppositeState.block());
            } else {
                if (selfBlock.block().isAir())
                    return;

                eggs = blockState.get(EGGS);

                if (eggs < 4) {
                    eggs = eggs + 1;
                    blockState.set(EGGS, eggs);
                }
            }
        }
    }
}
