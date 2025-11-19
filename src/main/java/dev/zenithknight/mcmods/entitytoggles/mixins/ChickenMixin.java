package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.CHICKENS_LAY_EGGS;

@Mixin(Chicken.class)
public class ChickenMixin {
    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Chicken;dropFromGiftLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Ljava/util/function/BiConsumer;)Z"))
    public boolean layEgg(Chicken chicken, ServerLevel serverLevel, ResourceKey resourceKey, BiConsumer biConsumer, Operation<Boolean> original){
        if ((serverLevel.getGameRules().getBoolean(CHICKENS_LAY_EGGS) || chicken.getTags().contains("layEggs")) && !chicken.getTags().contains("noEggs")) {
            return original.call(chicken, serverLevel, resourceKey, biConsumer);
        } else {
            return false;
        }
    }
}
