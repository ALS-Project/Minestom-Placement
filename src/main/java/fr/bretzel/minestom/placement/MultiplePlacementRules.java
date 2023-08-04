package fr.bretzel.minestom.placement;

import fr.bretzel.minestom.states.BlockStateManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Supplier;

public class MultiplePlacementRules extends BlockPlacementRule {
    private final ArrayList<PlacementRule> blockPlacements = new ArrayList<>();

    public MultiplePlacementRules(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull PlacementState placementState) {
        //Get the current block state
        var blockState = BlockStateManager.get(block);

        //Block cannot be placed
        for (var placement : getPlacementRules()) {
            if (!placement.canPlace(blockState, placementState)) {
                return getBlock();
            }
        }

        for (var placement : getPlacementRules())
            placement.place(blockState, placementState);

        return blockState.block();
    }

    @Override
    public @NotNull Block blockUpdate(@NotNull UpdateState updateState) {
        var blockState = BlockStateManager.get(block);

        for (var placement : getPlacementRules())
            if (placement.canUpdate(blockState, updateState))
                placement.update(blockState, updateState);

        return blockState.block();
    }

    public void addBlockPlacement(PlacementRule placementRule) {
        blockPlacements.add(placementRule);
    }

    public void addBlockPlacement(Supplier<PlacementRule> placementRule) {
        addBlockPlacement(placementRule.get());
    }

    public ArrayList<PlacementRule> getPlacementRules() {
        return blockPlacements;
    }
}
