package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FrogspawnBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.FROGSPAWN_HATCH;

@Mixin(FrogspawnBlock.class)
public class FrogspawnBlockMixin {
    @Inject(method = "hatchFrogspawn", at = @At("HEAD"), cancellable = true)
    private void hatchFrogspawnMixin(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (serverLevel.getGameRules().getBoolean(FROGSPAWN_HATCH)) {
            ci.cancel();
        }
    }
}
