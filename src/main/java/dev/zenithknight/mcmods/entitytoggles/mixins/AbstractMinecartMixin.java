package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {
    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    public void isPushable(CallbackInfoReturnable<Boolean> cir) {
        AbstractMinecart minecart = ((AbstractMinecart)(Object)this);
        if (minecart.getTags().contains("noCollide")) {
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(true);
        }
    }
}
