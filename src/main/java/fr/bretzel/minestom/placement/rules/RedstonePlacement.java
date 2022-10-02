package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Directional;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.IntegerState;
import fr.bretzel.minestom.states.state.RedstoneWireHeight;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.block.BlockUtils;

public class RedstonePlacement extends PlacementRule {

    private final IntegerState POWER = IntegerState.Of("power", 0, 0, 15);

    public RedstonePlacement() {
        super(Block.REDSTONE_WIRE);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        return true;
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return true;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {
        BlockUtils block = new BlockUtils(instance, blockPosition);

        RedstoneWireHeight east = RedstoneWireHeight.NONE;
        RedstoneWireHeight north = RedstoneWireHeight.NONE;
        RedstoneWireHeight south = RedstoneWireHeight.NONE;
        RedstoneWireHeight west = RedstoneWireHeight.NONE;

        final BlockUtils blockNorth = block.north();
        final BlockUtils blockSouth = block.south();
        final BlockUtils blockEast = block.east();
        final BlockUtils blockWest = block.west();

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
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {

    }
}
