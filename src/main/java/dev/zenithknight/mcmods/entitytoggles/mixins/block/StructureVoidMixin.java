package dev.zenithknight.mcmods.entitytoggles.mixins.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StructureVoidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureVoidBlock.class)
public class StructureVoidMixin {
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getShapeMixin(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
        boolean render = false;
//        System.out.println("Getting Shape");
        if (collisionContext instanceof EntityCollisionContext) {
            Entity entity = ((EntityCollisionContext) collisionContext).getEntity();
            if (entity != null) {
                ItemStack itemStack = entity.getWeaponItem();
                if (itemStack.is(Items.STRUCTURE_VOID)) {
//                    System.out.println("Found Structure Void");
                    render = true;
                }
            }
        }
        if (render) {
            cir.setReturnValue(Shapes.block());
        }
    }
}
