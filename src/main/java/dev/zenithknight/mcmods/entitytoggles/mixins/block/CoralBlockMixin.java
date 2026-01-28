package dev.zenithknight.mcmods.entitytoggles.mixins.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.CORAL_DRIES;

@Mixin(CoralBlock.class)
public class CoralBlockMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickMixin(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci){
        if (!serverLevel.getGameRules().getBoolean(CORAL_DRIES)) {
            ci.cancel();
        }
    }
}
