package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.SlabType;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class SlabPlacement extends PlacementRule {
    public SlabPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var currentBlock = placementState.instance().getBlock(placementState.placePosition());
        return currentBlock.isAir() || currentBlock == block();
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
        var centerBlock = new BlockUtils(instance, placementState.placePosition());
        var blockFace = placementState.blockFace();
        var downBlock = centerBlock.down();
        var upBlock = centerBlock.up();

        if (blockFace == BlockFace.TOP) {
            if (downBlock.block() == block()) {
                var downStates = downBlock.state();
                var slabType = downStates.get(SlabType.class);

                if (slabType == SlabType.BOTTOM) {
                    downStates.set(SlabType.DOUBLE);
                    instance.setBlock(downBlock.position(), downStates.block());
                    blockState.withBlock(Block.AIR);
                    return;
                }
            }
        }

        if (blockFace == BlockFace.BOTTOM) {
            if (upBlock.block().compare(block())) {
                var upStates = upBlock.state();
                var slabType = upStates.get(SlabType.class);

                if (slabType == SlabType.TOP) {
                    blockState.withBlock(Block.AIR);
                    upStates.set(SlabType.DOUBLE);
                    instance.setBlock(upBlock.position(), upStates.block());
                    return;
                }
            }
        }

        if (blockFace != BlockFace.TOP && blockFace != BlockFace.BOTTOM) {
            var hit = placementState.cursorPosition();
            var y = hit.y() - (hit.blockY());
            var currentSlabType = blockState.get(SlabType.class);

            if (y >= 0.5 && currentSlabType == SlabType.BOTTOM) {
                blockState.set(SlabType.TOP);
            } else if (y <= 0.5 && currentSlabType == SlabType.TOP) {
                blockState.set(SlabType.DOUBLE);
            } else {
                blockState.set((y >= 0.5) ? SlabType.TOP : SlabType.BOTTOM);
            }
        }
    }
}
