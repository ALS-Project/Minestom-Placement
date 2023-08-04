package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class HeadPlacement extends PlacementRule {
    private final Block wall_head;

    public HeadPlacement(Block block, Block wall_head) {
        super(block);

        this.wall_head = wall_head;
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
        var blockFace = placementState.blockFace();

        if (blockFace != BlockFace.TOP && blockFace != BlockFace.BOTTOM) {
            blockState.withBlock(wall_head);
            blockState.set(Facing.parse(blockFace));
        }
    }
}
