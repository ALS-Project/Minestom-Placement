package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.IntegerState;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class TurtleEggPlacement extends PlacementRule {

    private static final IntegerState EGGS = IntegerState.Of("eggs");

    public TurtleEggPlacement() {
        super(Block.TURTLE_EGG);
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
        var instance = (Instance) placementState.instance();
        var blockPosition = placementState.placePosition();
        var blockFace = placementState.blockFace();
        var selfBlock = new BlockUtils(instance, blockPosition);
        var oppositeBlock = selfBlock.relative(Facing.parse(blockFace.getOppositeFace()));

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
