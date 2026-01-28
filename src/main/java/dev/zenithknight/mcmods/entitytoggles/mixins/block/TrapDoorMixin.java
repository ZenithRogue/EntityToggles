package dev.zenithknight.mcmods.entitytoggles.mixins.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TrapDoorBlock.class)
public class TrapDoorMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.getTags().contains("noTrapdoor")) {
            List<Marker> markers = level.getEntitiesOfClass(Marker.class, new AABB(blockPos).inflate(0.01));
            boolean cancel = true;
//            System.out.println("Player Blocked");
            for (Marker marker : markers) {
//                System.out.println("Checking Marker");
                if (marker.getTags().contains("trapdoor")) {
                    cancel = false;
                }
            }
            if (cancel) {
                cir.setReturnValue(InteractionResult.FAIL);
                cir.cancel();
            }
        }
    }
}
