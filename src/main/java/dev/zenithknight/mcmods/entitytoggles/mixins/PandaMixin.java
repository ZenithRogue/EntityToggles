package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Panda.class)
public class PandaMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tickMixin(CallbackInfo ci) {
        Panda panda = ((Panda) (Object) this);
        if(panda.getTags().contains("lazy") && !panda.isOnBack()){
            panda.setOnBack(true);
        }
    }
}
