package dev.zenithknight.mcmods.entitytoggles.mixins.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CoralPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.CORAL_DRIES;

@Mixin(CoralPlantBlock.class)
public class CoralPlantBlockMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickMixin(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci){
        if (!serverLevel.getGameRules().getBoolean(CORAL_DRIES)) {
            ci.cancel();
        }
    }

    @Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
    private void onPlaceMixin(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci){
        MinecraftServer server = level.getServer();
        if (server != null && !server.getGameRules().getBoolean(CORAL_DRIES)) {
            ci.cancel();
        }
    }
}
