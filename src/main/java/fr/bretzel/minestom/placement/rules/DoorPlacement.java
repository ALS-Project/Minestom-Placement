package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.DoorHalf;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.Hinge;
import fr.bretzel.minestom.utils.block.BlockUtils;
import fr.bretzel.minestom.utils.raytrace.RayTrace;
import fr.bretzel.minestom.utils.raytrace.RayTraceContext;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class DoorPlacement extends PlacementRule {
    public DoorPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        var self = new BlockUtils(instance, blockPosition);
        return blockPosition.y() < 255 && self.block().isAir() && self.up().block().isAir() || !(blockFace == Facing.DOWN);
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return false;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {

    }

    @Override
    public void place(Instance instance, BlockState selfState, Facing blockFace, Point blockPosition, Player pl) {
        var facing = selfState.get(Facing.class);
        var upBlock = new BlockUtils(instance, blockPosition).up();
        var upSate = upBlock.state();

        upSate.withBlock(block());

        var hit = RayTrace.rayTraceBlock(new RayTraceContext(pl, 6)).getHit();

        var hinge = calculateHinge(hit, pl);

        if (blockFace != Facing.DOWN && blockFace != Facing.UP) {
            facing = facing.opposite();
        }

        upSate.set(DoorHalf.UPPER);
        upSate.set(hinge);
        upSate.set(facing);

        instance.setBlock(upBlock.position(), upSate.block());

        selfState.set(DoorHalf.LOWER);
        selfState.set(hinge);
        selfState.set(facing);
    }

    public Hinge calculateHinge(Point hit, Player player) {
        var facing = Facing.fromYaw(player.getPosition().yaw());

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
