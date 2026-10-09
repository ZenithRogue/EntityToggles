package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.level.LevelTimeAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Mixin(LevelTimeAccess.class)
//public class LevelTimeAccessMixin {
//    private final Minecraft minecraft = Minecraft.getInstance();
//    @Inject(method = "getMoonPhase", at = @At("HEAD"))
//    void getMoonPhase(CallbackInfoReturnable<Integer> cir) {
//        LevelTimeAccess accessor = (LevelTimeAccess) (Object) this;
//        accessor.
//        cir.setReturnValue(accessor.dimensionType().moonPhase(accessor.dayTime()));
//    }
//}
