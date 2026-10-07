package dev.zenithknight.mcmods.entitytoggles.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(WallBlock.class)
public class WallBlockMixin {
//    @ModifyArg(method = "makeShapes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/WallBlock;getShapeForEachState(Ljava/util/function/Function;[Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/util/function/Function;"))
//    private static Function getShapeForEachStateMixin(Function original, @Local float f, @Share("f_rip") LocalFloatRef floatRef) {
//        floatRef.set();
//    }

    @ModifyExpressionValue(method = "method_66472", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;empty()Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private static VoxelShape replaceEmpty(VoxelShape original, @Local BlockState blockState) {
        return Block.column((double)6.0F, (double)0.0F, (double) 16.0F);
    }
}
