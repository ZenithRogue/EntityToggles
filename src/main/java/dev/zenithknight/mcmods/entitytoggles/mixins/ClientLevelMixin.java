package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Shadow
    private Minecraft minecraft;
    private static CompoundTag movingPistonData = new CompoundTag();
    static {
        movingPistonData.putBoolean("movingPiston", true);
    }
    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true)
    private void getMarkerParticle (CallbackInfoReturnable<Block> cir) {
        assert this.minecraft.gameMode != null;
        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            assert this.minecraft.player != null;
            CustomData customData = this.minecraft.player.getMainHandItem().getComponents().get(CUSTOM_DATA);
            if (customData != null && customData.matchedBy(movingPistonData)) {
                cir.setReturnValue(Blocks.MOVING_PISTON);
            }
        }
    }
}
