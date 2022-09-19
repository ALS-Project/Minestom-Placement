package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomstates.BlockStateManager;
import fr.bretzel.minestomstates.state.Facing;
import fr.als.core.block.shape.BoxManager;
import fr.bretzel.minestomplacement.ALSBlockPlacement;
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

public class MultipleBlockPlacement extends BlockPlacementRule {
    private final ArrayList<ALSBlockPlacement> blockPlacements = new ArrayList<>();

    public MultipleBlockPlacement(@NotNull Block block) {
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

    public void addBlockPlacement(ALSBlockPlacement placementRule) {
        blockPlacements.add(placementRule);
    }

    public void addBlockPlacement(Supplier<ALSBlockPlacement> placementRule) {
        addBlockPlacement(placementRule.get());
    }

    public ArrayList<ALSBlockPlacement> getPlacementRules() {
        return blockPlacements;
    }
}
