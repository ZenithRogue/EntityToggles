package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.CHICKENS_LAY_EGGS;

@Mixin(Chicken.class)
public class ChickenMixin {
    @WrapWithCondition(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Chicken;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    public boolean layEgg(Chicken chicken, ItemLike itemLike){
        return (chicken.level().getGameRules().getBoolean(CHICKENS_LAY_EGGS) || chicken.getTags().contains("layEggs")) && !chicken.getTags().contains("noEggs");
    }
}
