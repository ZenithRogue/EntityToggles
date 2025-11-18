package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(targets = "net.minecraft.world.entity.monster.Ghast$GhastShootFireballGoal")
public abstract class GhastShootFireballGoalMixin extends Goal {
    @Shadow
    private Ghast ghast;
//    @Inject(method = "setCharging", at = @At("HEAD"), cancellable = true)
//    public void setChargingMixin(boolean bl, CallbackInfo ci) {
//        Set<String> tags1 = this.getTags();
//        System.out.println(tags1);
//        ci.cancel();
//    }
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tickMixin(CallbackInfo ci) {
        if(ghast.getTags().contains("passive")){
            ci.cancel();
        }
    }
}
