package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractCubeMob.class)
public class AbstractCubeMobMixin {
    @ModifyExpressionValue(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;isDeadOrDying()Z"))
    private boolean splitOnDeath(boolean original) {
        AbstractCubeMob slime = (AbstractCubeMob) (Object) this;
        return original && !slime.entityTags().contains("noSplit");
    }
}