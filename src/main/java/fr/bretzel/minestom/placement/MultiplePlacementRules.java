package fr.bretzel.minestom.placement;

import fr.bretzel.minestom.states.BlockStateManager;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.block.BoxManager;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
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
    public @NotNull Block blockUpdate(@NotNull Instance instance, @NotNull Point blockPosition, @NotNull Block currentBlock) {
        var blockState = BlockStateManager.get(currentBlock);

        for (var placement : getPlacementRules())
            if (placement.canUpdate(instance, blockPosition, blockState))
                placement.update(instance, blockPosition, blockState);

        return blockState.block();
    }

    @Override
    public @Nullable Block blockPlace(@NotNull Instance instance, @NotNull Block block, @NotNull BlockFace blockFace, @NotNull Point blockPosition, @NotNull Player player) {
        blockPosition = blockPosition.relative(blockFace);

        //Get the current
        var currentBlock = instance.getBlock(blockPosition);

        //Not the same block
        if (!currentBlock.isAir() && !currentBlock.isLiquid() && !currentBlock.compare(block))
            return currentBlock;

        var shape = BoxManager.get(currentBlock).getBoundingBox();
        var playerBox = player.getBoundingBox();

        //Player in block
        if (shape.intersect(new Vec(playerBox.minX(), playerBox.minY(), playerBox.minZ()), new Vec(playerBox.maxX(), playerBox.maxY(), playerBox.maxZ())))
            return currentBlock;

        //Get the current block state
        var blockState = BlockStateManager.get(block);

        //Block cannot be placed
        for (var placement : getPlacementRules())
            if (!placement.canPlace(instance, Facing.parse(blockFace), blockPosition, blockState, player))
                return getBlock();

        for (var placement : getPlacementRules())
            placement.place(instance, blockState, Facing.parse(blockFace), blockPosition, player);

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
