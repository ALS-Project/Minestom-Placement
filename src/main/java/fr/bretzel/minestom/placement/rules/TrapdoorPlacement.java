package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.Half;
import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.utils.raytrace.RayTrace;
import fr.bretzel.minestom.utils.raytrace.RayTraceContext;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class TrapdoorPlacement extends PlacementRule {
    public TrapdoorPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        return true;
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return false;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {

    }

    @Override
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player player) {
        if (blockFace == Facing.UP) {
            blockState.set(Half.BOTTOM);
        } else if (blockFace == Facing.DOWN) {
            blockState.set(Half.TOP);
        } else {
            var result = RayTrace.rayTraceBlock(new RayTraceContext(player, 5));
            if (result != null && result.getHit() != null && !result.getHitBlock().isAir()) {
                var hit = result.getHit();
                var y = hit.y() - ((int) hit.y());

                blockState.set((y >= 0.45) ? Half.TOP : Half.BOTTOM);
            }
        }

        if (blockFace.getAxis().isHorizontal())
            blockState.set(blockFace);

        if (blockFace == Facing.DOWN || blockFace == Facing.UP)
            blockState.set(blockState.get(Facing.class).opposite());
    }
}
