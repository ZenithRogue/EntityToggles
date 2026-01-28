package dev.zenithknight.mcmods.entitytoggles.mixins.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

@Mixin(MovingPistonBlock.class)
public class MovingPistonMixin {
    private static CompoundTag movingPistonData = new CompoundTag();
    static {
        movingPistonData.putBoolean("movingPiston", true);
    }
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getShapeMixin(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
        boolean render = false;
//        System.out.println("Getting Shape");
        if (collisionContext instanceof EntityCollisionContext) {
            Entity entity = ((EntityCollisionContext) collisionContext).getEntity();
            if (entity != null) {
                CustomData item = entity.getWeaponItem().getComponents().get(CUSTOM_DATA);
//                System.out.println(item);
                if (item != null && item.matchedBy(movingPistonData)) {
//                    System.out.println("Found Moving Piston");
                    render = true;
                }
            }
        }
        if (render) {
            cir.setReturnValue(Shapes.block());
        } else {
            cir.setReturnValue(Shapes.empty());
        }
    }
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(InteractionResult.FAIL);
        cir.cancel();
    }
}
