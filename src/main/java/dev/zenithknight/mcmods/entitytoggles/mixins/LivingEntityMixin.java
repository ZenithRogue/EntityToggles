package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    public void isPushable(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        if (entity.getTags().contains("noCollide")) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickMixin(CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object) this;
        Set tags = entity.getTags();
        if (tags.contains("sleeping")) {
            entity.setPose(Pose.SLEEPING);
        }
        if (tags.contains("crouching")) {
            entity.setPose(Pose.CROUCHING);
        }
        if (tags.contains("sitting")) {
            entity.setPose(Pose.SITTING);
        }
        if (tags.contains("standing")) {
            entity.setPose(Pose.STANDING);
        }
        if (tags.contains("swimming")) {
            entity.setPose(Pose.SWIMMING);
        }
        if (tags.contains("resetPose")) {
            entity.removeTag("sleeping");
            entity.removeTag("crouching");
            entity.removeTag("sitting");
            entity.removeTag("standing");
            entity.removeTag("swimming");
            entity.removeTag("resetPose");
            entity.setPose(Pose.STANDING);
        }
    }
}
