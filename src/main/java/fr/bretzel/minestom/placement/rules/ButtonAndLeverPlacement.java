package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Face;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class ButtonAndLeverPlacement extends PlacementRule {
    public ButtonAndLeverPlacement(Block block) {
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
        if (blockFace == BlockFace.TOP) {
            blockState.set(Face.FLOOR);
        } else if (blockFace == BlockFace.BOTTOM) {
            blockState.set(Face.CEILING);
        } else {
            blockState.set(Face.WALL);
        }
    }
}
