package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.SlabType;
import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.utils.block.BlockUtils;
import fr.bretzel.minestom.utils.raytrace.RayTrace;
import fr.bretzel.minestom.utils.raytrace.RayTraceContext;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class SlabPlacement extends PlacementRule {
    public SlabPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        Block bl = instance.getBlock(blockPosition);
        return bl.isAir() || bl == block();
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return false;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {

    }

    @Override
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player player) {
        var centerBlock = new BlockUtils(instance, blockPosition);
        var downBlock = centerBlock.down();
        var upBlock = centerBlock.up();

        if (blockFace == Facing.UP) {
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

        if (blockFace == Facing.DOWN) {
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

        if (blockFace != Facing.UP && blockFace != Facing.DOWN) {
            var result = RayTrace.rayTraceBlock(new RayTraceContext(player, 6));

            if (result != null && result.getHit() != null && !result.getHitBlock().isAir()) {
                var hit = result.getHit();
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
}
