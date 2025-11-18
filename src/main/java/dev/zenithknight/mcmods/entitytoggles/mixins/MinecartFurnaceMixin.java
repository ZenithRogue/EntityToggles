package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.world.entity.vehicle.MinecartFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartFurnace.class)
public class MinecartFurnaceMixin {
    @Shadow
    private int fuel;
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tickMixin(CallbackInfo ci) {
        MinecartFurnace furnace = ((MinecartFurnace) (Object) this);
        if(furnace.getTags().contains("coal")){
            this.fuel = 100;
        }
    }
}
