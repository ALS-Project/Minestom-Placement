package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.Half;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class TrapdoorPlacement extends PlacementRule {
    public TrapdoorPlacement(Block block) {
        super(block);
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
        var facing = Facing.parse(blockFace);

        if (blockFace == BlockFace.TOP) {
            blockState.set(Half.BOTTOM);
        } else if (blockFace == BlockFace.BOTTOM) {
            blockState.set(Half.TOP);
        } else {
            var hit = placementState.cursorPosition();

            if (hit != null) {
                var y = hit.y() - ((int) hit.y());

                blockState.set((y >= 0.45) ? Half.TOP : Half.BOTTOM);
            }
        }

        if (facing.getAxis().isHorizontal())
            blockState.set(facing);

        if (facing == Facing.DOWN || facing == Facing.UP)
            blockState.set(blockState.get(Facing.class).opposite());
    }
}
