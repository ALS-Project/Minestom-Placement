package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.DoorHalf;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.Hinge;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class DoorPlacement extends PlacementRule {
    public DoorPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = placementState.instance();
        var blockPosition = placementState.placePosition();
        var blockFace = placementState.blockFace();
        var self = new BlockUtils((Instance) instance, blockPosition);
        return blockPosition.y() < 255 && self.block().isAir() && self.up().block().isAir() || !(blockFace == BlockFace.BOTTOM);
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
        var instance = (Instance) placementState.instance();
        var blockPosition = placementState.placePosition();

        var facing = blockState.get(Facing.class);
        var upBlock = new BlockUtils(instance, blockPosition).up();
        var upSate = upBlock.state();

        upSate.withBlock(block());

        var hinge = calculateHinge(placementState.cursorPosition(), placementState.playerPosition().yaw());

        var blockFace = placementState.blockFace();

        if (blockFace != BlockFace.BOTTOM && blockFace != BlockFace.TOP) {
            facing = facing.opposite();
        }

        upSate.set(DoorHalf.UPPER);
        upSate.set(hinge);
        upSate.set(facing);

        instance.setBlock(upBlock.position(), upSate.block());

        blockState.set(DoorHalf.LOWER);
        blockState.set(hinge);
        blockState.set(facing);
    }

    public Hinge calculateHinge(Point hit, float yaw) {
        var facing = Facing.fromYaw(yaw);

        var z = hit.z() - ((int) hit.z());
        var x = hit.x() - ((int) hit.x());

        if (z < 0) {
            z = -z;
        }

        if (x < 0) {
            x = -x;
        }

        return switch (facing) {
            case EAST -> z < 0.5 ? Hinge.LEFT : Hinge.RIGHT;
            case WEST -> z > 0.5 ? Hinge.LEFT : Hinge.RIGHT;
            case NORTH -> x < 0.5 ? Hinge.RIGHT : Hinge.LEFT;
            case SOUTH -> x > 0.5 ? Hinge.RIGHT : Hinge.LEFT;
            default -> Hinge.LEFT;
        };
    }
}
