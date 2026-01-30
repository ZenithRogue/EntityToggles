package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Villager.class)
public class VillagerMixin {
    @Shadow
    protected void startTrading(Player playerByUUID){}

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void readAdditionalSaveDataMixin(ValueInput valueInput, CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;
        if (!valueInput.read("trade_target", UUIDUtil.CODEC).isEmpty()) {
            UUID playerUUID = valueInput.read("trade_target", UUIDUtil.CODEC).get();
            this.startTrading(villager.level().getPlayerByUUID(playerUUID));
        }
    }

    @WrapWithCondition(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;startTrading(Lnet/minecraft/world/entity/player/Player;)V"))
    private boolean hideTrades(Villager villager, Player player){
        return !villager.getTags().contains("hideTrades");
    }
}
