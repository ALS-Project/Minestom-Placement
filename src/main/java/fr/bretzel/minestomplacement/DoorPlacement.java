package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.DoorHalf;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.block.blockstate.state.Hinge;
import fr.als.core.raytrace.RayTrace;
import fr.als.core.raytrace.RayTraceContext;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class DoorPlacement extends ALSBlockPlacement {
    public DoorPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        var self = new ALSBlock(instance, blockPosition);
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
        var upBlock = new ALSBlock(instance, blockPosition).up();
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
