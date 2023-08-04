package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Directional;
import fr.bretzel.minestom.states.state.IntegerState;
import fr.bretzel.minestom.states.state.RedstoneWireHeight;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.block.BlockUtils;

public class RedstonePlacement extends PlacementRule {

    private final IntegerState POWER = IntegerState.Of("power", 0, 0, 15);

    public RedstonePlacement() {
        super(Block.REDSTONE_WIRE);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return true;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        var instance = updateState.instance();
        var blockPosition = updateState.blockPosition();
        var block = new BlockUtils(instance, blockPosition);

        var east = RedstoneWireHeight.NONE;
        var north = RedstoneWireHeight.NONE;
        var south = RedstoneWireHeight.NONE;
        var west = RedstoneWireHeight.NONE;

        final var blockNorth = block.north();
        final var blockSouth = block.south();
        final var blockEast = block.east();
        final var blockWest = block.west();

        int connected = 0;

        if (blockNorth.equals(Block.REDSTONE_WIRE) || blockNorth.below().equals(Block.REDSTONE_WIRE)) {
            connected++;
            north = RedstoneWireHeight.SIDE;
        }
        if (blockSouth.equals(Block.REDSTONE_WIRE) || blockSouth.below().equals(Block.REDSTONE_WIRE)) {
            connected++;
            south = RedstoneWireHeight.SIDE;
        }
        if (blockEast.equals(Block.REDSTONE_WIRE) || blockEast.below().equals(Block.REDSTONE_WIRE)) {
            connected++;
            east = RedstoneWireHeight.SIDE;
        }
        if (blockWest.equals(Block.REDSTONE_WIRE) || blockWest.below().equals(Block.REDSTONE_WIRE)) {
            connected++;
            west = RedstoneWireHeight.SIDE;
        }
        if (blockNorth.above().equals(Block.REDSTONE_WIRE)) {
            connected++;
            north = RedstoneWireHeight.SIDE;
        }
        if (blockSouth.above().equals(Block.REDSTONE_WIRE)) {
            connected++;
            south = RedstoneWireHeight.SIDE;
        }
        if (blockEast.above().equals(Block.REDSTONE_WIRE)) {
            connected++;
            east = RedstoneWireHeight.UP;
        }
        if (blockWest.above().equals(Block.REDSTONE_WIRE)) {
            connected++;
            west = RedstoneWireHeight.UP;
        }

        if (connected == 0) {
            north = RedstoneWireHeight.SIDE;
            south = RedstoneWireHeight.SIDE;
            east = RedstoneWireHeight.SIDE;
            west = RedstoneWireHeight.SIDE;
        } else if (connected == 1) {
            if (north != RedstoneWireHeight.NONE)
                south = RedstoneWireHeight.SIDE;

            if (south != RedstoneWireHeight.NONE)
                north = RedstoneWireHeight.SIDE;

            if (east != RedstoneWireHeight.NONE)
                west = RedstoneWireHeight.SIDE;

            if (west != RedstoneWireHeight.NONE)
                east = RedstoneWireHeight.SIDE;

        }

        blockState.set(Directional.EAST, east);
        blockState.set(Directional.NORTH, north);
        blockState.set(POWER);
        blockState.set(Directional.SOUTH, south);
        blockState.set(Directional.WEST, west);
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {

    }
}
