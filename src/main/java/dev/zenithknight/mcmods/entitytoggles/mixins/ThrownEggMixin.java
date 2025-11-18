package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.EGGS_HATCH;

@Mixin(ThrownEgg.class)
public class ThrownEggMixin {
    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;"), cancellable = true)
    protected void onHit(HitResult hitResult, CallbackInfo ci) {
        ThrownEgg egg = ((ThrownEgg) (Object) this);
        Level level = egg.level();
        if (level instanceof ServerLevel) {
            if (!((ServerLevel) level).getGameRules().getBoolean(EGGS_HATCH)) {
                ci.cancel();
            }
        }
    }
}
