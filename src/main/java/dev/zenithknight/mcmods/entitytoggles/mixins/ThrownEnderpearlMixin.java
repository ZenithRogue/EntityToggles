package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import java.util.logging.Level;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.ENDERMITE_SPAWN;
import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.ENDER_PEARL_DAMAGE;

@Mixin(ThrownEnderpearl.class)
public class ThrownEnderpearlMixin {
    @WrapWithCondition(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean enderpearlHit(ServerPlayer instance, ServerLevel serverLevel, DamageSource damageSource, float f) {
        return serverLevel.getGameRules().getBoolean(ENDER_PEARL_DAMAGE);
    }
    @ModifyExpressionValue(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isSpawningMonsters()Z"))
    private boolean endermiteSpawn(boolean original, @Local ServerLevel serverLevel) {
        return serverLevel.getGameRules().getBoolean(ENDERMITE_SPAWN) && original;
    }
}
