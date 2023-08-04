package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.IntegerState;
import fr.bretzel.minestom.utils.math.MathsUtils;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class RotationPlacement extends PlacementRule {

    private final IntegerState ROTATION = IntegerState.Of("rotation", 0, 0, 15);

    public RotationPlacement(Block block) {
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
        var yaw = placementState.playerPosition().yaw();

        if (!block().name().contains("skull")) {
            yaw = yaw + 180;
        }

        blockState.set(ROTATION, MathsUtils.floor((yaw * 16.0F / 360.0F) + 0.5D) & 15);
    }
}
