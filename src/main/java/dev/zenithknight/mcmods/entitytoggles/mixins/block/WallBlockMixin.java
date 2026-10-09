package dev.zenithknight.mcmods.entitytoggles.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(WallBlock.class)
public class WallBlockMixin {

    @ModifyExpressionValue(method = "lambda$makeShapes$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;empty()Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private static VoxelShape replaceEmpty(VoxelShape original, @Local BlockState blockState) {
        return Block.column((double)6.0F, (double)0.0F, (double) 16.0F);
    }
}
