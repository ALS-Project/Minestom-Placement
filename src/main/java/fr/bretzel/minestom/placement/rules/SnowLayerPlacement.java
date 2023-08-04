package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.IntegerState;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class SnowLayerPlacement extends PlacementRule {

    private static final IntegerState LAYERS = IntegerState.Of("layers");

    public SnowLayerPlacement() {
        super(Block.SNOW);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var blockPosition = placementState.placePosition();
        var instance = placementState.instance();
        var blockFace = placementState.blockFace();

        var block = instance.getBlock(blockPosition);
        return (block.isAir() || block == block()) && blockFace == BlockFace.TOP;
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
        var blockPosition = placementState.placePosition();
        var instance = (Instance) placementState.instance();
        var blockFace = placementState.blockFace();

        var selfBlock = new BlockUtils(instance, blockPosition);
        var downBlock = selfBlock.down();

        if (blockFace == BlockFace.TOP) {
            if (downBlock.block() == block()) {
                var downState = downBlock.state();
                var layers = downState.get(LAYERS);

                if (layers < 8) {
                    blockState.withBlock(Block.AIR);
                    layers = layers + 1;
                    downState.set(LAYERS, layers);
                    instance.setBlock(downBlock.position(), downState.block());
                }

            } else {
                blockState.set(LAYERS, 1);
            }
        }
    }
}
