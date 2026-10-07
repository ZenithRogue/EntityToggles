package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Slime.class)
public class SlimeMixin {
    @ModifyExpressionValue(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;isDeadOrDying()Z"))
    private boolean splitOnDeath(boolean original) {
        Slime slime = (Slime) (Object) this;
        return original && !slime.getTags().contains("noSplit");
    }
}